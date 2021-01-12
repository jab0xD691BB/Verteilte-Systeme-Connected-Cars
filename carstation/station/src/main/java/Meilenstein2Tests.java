import java.util.HashMap;
import java.util.Map;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import simpleServer.sendReceive;

public class Meilenstein2Tests implements Runnable {

  private String containerIp;

  public Meilenstein2Tests(String ip) {
    this.containerIp = ip;
  }

  public Map<String,String> functionalTest(String t, int i) {
    //Vier Nachrichten verschicken und schauen ob diese korrekt Persistieren.
    //Test ob die API funktioniert. (Thrift beschriebene Datei.)
    Map<String, String> map = new HashMap<>();
    map.put(t, "" + i);

    return map;
  }

  @Override
  public void run() {

    System.out.println("Meilenstein 2 Funktionaler-Test && Nicht-funktionler-Test");
    for (int i = 0; i < 4; i++) {
      send(functionalTest("Test", i));
    }
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Meilenstein 2 Performance-Test");

    for (int i = 0; i < 1000; i++) {
      send(functionalTest("Test2",i));
    }

  }

  public void send(Map<String, String> m) {
    TTransport transport = new TSocket(containerIp, 9090);
    try {
      transport.open();

      System.out.println("thrift running");

      TProtocol protocol = new TBinaryProtocol(transport);

      sendReceive.Client client = new sendReceive.Client(protocol);

      client.sendCurrent(m);

      transport.close();
    } catch (TTransportException e) {
      e.printStackTrace();
    } catch (TException e) {
      e.printStackTrace();
    }
  }
}
