import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import simpleServer.sendReceive;
import simpleServer.sendReceive.Processor;

public class main{

  public static SendReceiveHandler sendReceiveHandler;

  public static sendReceive.Processor processor;

  public static void main(String[] args) {

    try{

      sendReceiveHandler = new SendReceiveHandler();
      processor = new Processor(sendReceiveHandler);

        Runnable simple = new Runnable() {
        @Override
        public void run() {
          simple(processor);
        }
      };

       new Thread(simple).start();

    }catch (Exception e){
      e.printStackTrace();
    }

  }

  public static void simple(sendReceive.Processor processor){
    try{
      TServerTransport serverTransport = new TServerSocket(9090);

      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      System.out.println("Starting the simple carApi server...");
      server.serve();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
