import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLDecoder;
import java.util.ArrayList;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import simpleServer.sendReceive;

public class main {

  public static void main(String[] args) throws UnsupportedEncodingException {

    String path = main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    System.out.println("path: " + decodedPath);

    Udplistener udplistener;

    String tContainerIp = args[0];

    //udp listener
    try {
      udplistener = new Udplistener(9876);
      Thread ta = new Thread(udplistener);
      ta.start();

    } catch (IOException e) {
      return;
    }

    //http api
    try {
      Thread t2 = new Thread(new HttpApi());
      t2.start();

    } catch (Exception e) {
      System.exit(1);
    }

    try {
      Runnable t3 = new Runnable() {
        @Override
        public void run() {
          try {
            while (udplistener.running) {
              TTransport transport = new TSocket(tContainerIp, 9090);
              try{
                transport.open();

                System.out.println("thrift running");
                TProtocol protocol = new TBinaryProtocol(transport);
                sendReceive.Client client = new sendReceive.Client(protocol);
                client.send_sendCurrent(udplistener.currentValues);

                transport.close();
              }catch (Exception e){
                System.out.println("cant conn to thrift server");
              }finally {
                Thread.sleep(2000);
              }

            }


          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      new Thread(t3).start();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

}
