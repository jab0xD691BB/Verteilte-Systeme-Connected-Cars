import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import provider.providerApi;
import provider.providerApi.Processor;

public class ReceiveServer implements Runnable, providerApi.Iface {


  public providerApi.Processor processor;

  public ServerManager serverManager;

  public ReceiveServer() {

    serverManager = new ServerManager();



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
      TServerTransport serverTransport = new TServerSocket(9080);

      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      System.out.println("Starting the simple carApi server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void heartbeat(String name, String serverIp, int port) throws TException {
    // System.out.println("Receiving Heartbeat from Server " + name);

    Server s = new Server(name, new Timestamp(System.currentTimeMillis()),  true, serverIp, port);
    serverManager.servers.put(name, s);



  }

  @Override
  public void sendCurrentToPrimary(Map<String, String> sv, String secondaryIp, int secondaryPort) throws TException {

  }

  @Override
  public void sendCurrentToSecondary(Map<String, String> sv) throws TException {

  }

  @Override
  public void sendCompleteToSecondary(ByteBuffer data) throws TException {

  }
}
