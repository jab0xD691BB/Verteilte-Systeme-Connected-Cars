import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import provider.providerApi;

public class main {

  public static void main(String[] args) {

    String name = "Test";
    String ip = "";
    int listeningPort = 0;

    String proxyContainerIp = "";


    try{
      name = args[0];
      ip = args[1];
      listeningPort = Integer.parseInt(args[2]);
      proxyContainerIp = args[3];
    }catch (ArrayIndexOutOfBoundsException e){
      e.printStackTrace();
    }


    Thread t1 = new Thread(new SendProxy(name,ip,listeningPort,proxyContainerIp));
    t1.start();

    Thread t2 = new Thread(new ReceiveProxy(listeningPort));
    t2.start();

  }

}
