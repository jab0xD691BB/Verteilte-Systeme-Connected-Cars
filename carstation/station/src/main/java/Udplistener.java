import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.*;


public class Udplistener implements Runnable {

  private DatagramSocket serverSocket;
  private int port;
  private boolean running = true;

  private Map<String, Timestamp> activeSensor;
  private Set<String> sensorActive;

  public Udplistener(int port) throws SocketException {
    this.port = port;
    serverSocket = new DatagramSocket(port);
    activeSensor = new HashMap<>();
    sensorActive = new HashSet<>();
  }


  public void run() {
    System.out.println("udp is listening to port " + port);

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        checkActiveSensor();
      }
    }, 0, 1000);

    while (running) {

      byte[] receiveData = new byte[1024];
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

      try {
        serverSocket.receive(receivePacket);
        String r = new String(receivePacket.getData()).replaceAll("\u0000.*", "");

        JSONObject jsonObject = new JSONObject(r);

        String styp = jsonObject.getString("sensorType");
        String svalue = jsonObject.getString("sensorValue");

        System.out.println("receive: " + r);

        activeSensor.put(styp, new Timestamp(System.currentTimeMillis()));
        sensorActive.add(styp);
        processReceivedData(styp, svalue);

      } catch (Exception e) {
        e.printStackTrace();
        running = false;
      }

    }
  }

  private void processReceivedData(String sensorType, String sensorValue) {
    try {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());

      FileWriter currWriter = new FileWriter(
          new File("values", "current" + sensorType + "value.txt"));
      FileWriter historyWriter = new FileWriter(
          new File("values", "history" + sensorType + "value.txt"), true);

      currWriter.write(timestamp + " " + sensorValue + "\n");
      historyWriter.write(timestamp + " " + sensorValue + "\n");

      currWriter.close();
      historyWriter.close();

    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
    }
  }


  public void checkActiveSensor() {
    Timestamp ts = new Timestamp(System.currentTimeMillis());

    for (Map.Entry<String, Timestamp> p : activeSensor.entrySet()) {
      Timestamp sensorTs = activeSensor.get(p.getKey());
      int s = ts.getSeconds() - sensorTs.getSeconds();
      if (s > 5) {
        sensorActive.remove(p.getKey());    // sensor nach 10 sec aus der liste entfernen
      } else {

      }

      //System.out.println("Sensoractive size:  " + sensorActive.size());
    }

    try {

      FileWriter as = new FileWriter(
          new File("values", "activeSensor.txt"));
      as.write(ts + " " + String.join(";", sensorActive) + "\n");
      as.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
