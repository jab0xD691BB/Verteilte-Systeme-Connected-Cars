import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import provider.providerApi;

public class ServerManager{

  public Map<String, Server> servers;
  public static Map<String, String> sv = new HashMap<>();
  public String ipSecondary = "";
  public int portSecondary=0;


  public ServerManager() {
    servers = new LinkedHashMap<>();

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        checkActiveServer();
      }
    }, 0, 500);

  }

  public void checkActiveServer() {
    if (servers.size() > 0 ) {
      servers.get(servers.keySet().toArray()[0]).isPrimary = true;
      ipSecondary = "";
      portSecondary = 0;

      if(servers.size() > 1){
        servers.get(servers.keySet().toArray()[1]).isPrimary = false;
        ipSecondary = servers.get(servers.keySet().toArray()[1]).ip;
        portSecondary =  servers.get(servers.keySet().toArray()[1]).port;
      }

      for (Map.Entry<String, Server> entry : servers.entrySet()) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        //System.out.println(server.name + "\t" + server.timestamp);

        Timestamp serverLastHeartbeatTime = entry.getValue().timestamp;

        long diffMilliSec = currentTime.getTime() - serverLastHeartbeatTime.getTime();

        if (diffMilliSec >= 1000) {
          servers.remove(entry.getKey());
          break;
        }

        System.out.println(entry.getValue().toString());

      }

       //System.out.println("servers size: " + servers.size());
    }else{
      ipSecondary = "";
      portSecondary = 0;
      System.out.println("Zustand: No Server Available - nichts tun");
      //falls ein
      // Server online kommt soll erst anfangen daten zu speichern
      // weil wir davon ausgehen dass immer ein Server online ist
    }
    if(servers.size() == 1){
      ipSecondary = "";
      portSecondary = 0;
      System.out.println("Zustand: Ankommende Daten an Primary schicken");
      //Daten weiterleiten an Primary
    }else if(servers.size() > 1){
      ipSecondary = servers.get(servers.keySet().toArray()[1]).ip;
      portSecondary =  servers.get(servers.keySet().toArray()[1]).port;
      System.out.println("Zustand: Daten an Primay schicken und Primary schickt an Secondary");

      //Wenn ein secondary neu kommt wird die ganze datei unter den Servern geschickt und die einzelnen neu ankommen daten einzeln nach geschickt.
    }


  }

  public void sendToPrimary() throws InterruptedException {
    /*while (true){
      try {
        TTransport transport = new TSocket("127.0.0.1", servers.get(servers.keySet().toArray()[0]).port);
        transport.open();

        System.out.println("send to provider server");

        TProtocol protocol = new TBinaryProtocol(transport);

        providerApi.Client client = new providerApi.Client(protocol);

        client.sendCurrentToPrimary(sv);

        transport.close();
      } catch (Exception e) {
        System.out.println("cant conn to thrift server");
      }
    }*/
  }



}
