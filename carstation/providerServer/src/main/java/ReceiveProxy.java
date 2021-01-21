import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import provider.providerApi;
import provider.providerApi.Iface;
import provider.providerApi.Processor;

public class ReceiveProxy implements Runnable, providerApi.Iface {

  public providerApi.Processor processor;
  private int port;

  private boolean persistentOnce = false;

  private Set<String> fileNames;

  public ReceiveProxy(int p) {
    this.port = p;
    this.fileNames = new HashSet<>();

  }

  @Override
  public void run() {
    try {

      this.processor = new Processor(this);

      Runnable simple = new Runnable() {
        @Override
        public void run() {
          simple(processor);
        }
      };

      new Thread(simple).start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void simple(providerApi.Processor processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(this.port);

      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      System.out.println("Starting the simple providerApi server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void heartbeat(String name, String serverIp, int port) throws TException {

  }

  //wichtig
  @Override
  public void sendCurrentToPrimary(Map<String, String> sv, String secondaryIp ,int secondaryPort) throws TException {
    System.out.println("Ankommende Daten: ");

    processReceiveData(sv, secondaryPort);

    if (secondaryPort > 0) {

      if (!persistentOnce) {
        System.out.println("Komplette Weiterleitung zum Secondary wird vorbereitet");
        try {
          TTransport transport = new TSocket(secondaryIp, secondaryPort);
          transport.open();

          System.out.println("send to secondary server");

          TProtocol protocol = new TBinaryProtocol(transport);

          providerApi.Client client = new providerApi.Client(protocol);

          for (String f : fileNames) {
            System.out.println("Sende " + f);
            String wholeFile;

            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
              StringBuilder sb = new StringBuilder();
              String line = br.readLine();

              sb.append(f);
              sb.append(System.lineSeparator());

              while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
              }
              wholeFile = sb.toString();
            }

            ByteBuffer buffer = ByteBuffer.wrap(wholeFile.getBytes());
            client.sendCompleteToSecondary(buffer);


          }

          transport.close();
        } catch (Exception e) {
          System.out.println("cant conn to secondary server");
        }

        persistentOnce = true;
      } else {

        System.out.println("Einzelne Weiterleitung zum Secondary wird vorbereitet");

        try {
          TTransport transport = new TSocket(secondaryIp, secondaryPort);
          transport.open();

          System.out.println("send to secondary server");

          TProtocol protocol = new TBinaryProtocol(transport);

          providerApi.Client client = new providerApi.Client(protocol);

          client.sendCurrentToSecondary(sv);

          transport.close();
        } catch (Exception e) {
          System.out.println("cant conn to secondary server");
        }

      }


    } else if (secondaryPort == 0 && persistentOnce) {
      persistentOnce = false;
    }


  }

  @Override
  public void sendCurrentToSecondary(Map<String, String> sv) throws TException {
    processReceiveData(sv, 0);
  }

  @Override
  public void sendCompleteToSecondary(ByteBuffer data) throws TException {
    String wholeFile = StandardCharsets.UTF_8.decode(data).toString();
    System.out.println(wholeFile);

    String fileName = wholeFile.split("\\n")[0];

    File file = new File(fileName);
    if (file.delete()) {
      System.out.println("File deleted");
    }

    System.out.println("Filename: " + fileName);
    fileNames.add(fileName);
    String cleanWholeFile = wholeFile.substring(wholeFile.indexOf("\n") + 1);



    try {
      FileWriter currWriter = new FileWriter(new File(fileName),
          true);

      currWriter.write(cleanWholeFile);

      currWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public void processReceiveData(Map<String, String> sv, int secondaryPort) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Date date = new Date(timestamp.getTime());

    for (Map.Entry<String, String> entry : sv.entrySet()) {
      System.out.println(entry.getKey() + "\t" + entry.getValue() + "\t Port:" + secondaryPort);

      try {
        String fileName = "current" + entry.getKey() + "value.txt";
        FileWriter currWriter = new FileWriter(new File(fileName),
            true);

        fileNames.add(fileName);

        currWriter.write(date + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");

        currWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }


    }
  }

}
