import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import simpleServer.sendReceive;
import simpleServer.sendReceive.Processor;

public class ReceiveStation  implements Runnable{

  public SendReceiveHandler sendReceiveHandler;

  public sendReceive.Processor processor;

  public ReceiveStation() {

  }

  @Override
  public void run() {
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

  public void simple(sendReceive.Processor processor){
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
