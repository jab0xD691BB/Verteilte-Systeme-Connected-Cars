import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import provider.providerApi;

public class SendProxy implements Runnable {

  private String name;
  private String ip;
  private int listeningPort;
  private String proxyContainerIp;

  public SendProxy(String n, String ip, int p, String pIp) {
    this.name = n;
    this.ip = ip;
    this.listeningPort = p;
    this.proxyContainerIp = pIp;
  }


  @Override
  public void run() {
    try {
      System.out.println("Server Provider " + name + " running");
      while (true) {
        TTransport transport = new TSocket(this.proxyContainerIp, 9080);
        try {
          transport.open();

          TProtocol protocol = new TBinaryProtocol(transport);

          providerApi.Client client = new providerApi.Client(protocol);

          client.heartbeat(this.name, this.ip, this.listeningPort);

          transport.close();
        } catch (Exception e) {
          System.out.println("cant conn to thrift server");
        } finally {
          Thread.sleep(500);
        }

      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
