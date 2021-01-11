import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.*;

public class Udplistener implements Runnable, MqttCallback {

  private DatagramSocket serverSocket;
  private int port;
  public boolean running = true;

  private Map<String, Timestamp> activeSensor;
  private Set<String> sensorActive;

  public Map<String, String> currentValues;

  private String brokerIp = "127.0.0.1";
  private String brokerPort = "1883";
  private String[] topics = {"values/Tank", "values/Kilometerstand", "values/Verkehrssituation",
      "values/avgSpeed"};


  public Udplistener(int port) throws SocketException {
    this.port = port;
    serverSocket = new DatagramSocket(port);
    activeSensor = new HashMap<>();
    sensorActive = new HashSet<>();
    currentValues = new HashMap<>();
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


    try {
      MqttClient client = new MqttClient("tcp://" + brokerIp + ":" + brokerPort, "Station");

      MqttConnectOptions options = new MqttConnectOptions();

      options.setCleanSession(false);

      client.setCallback(this);

      client.connect(options);

      for (String topic : topics) {
        client.subscribe(topic);
      }

    } catch (MqttException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }
  }


  public void checkActiveSensor() {
    Timestamp ts = new Timestamp(System.currentTimeMillis());

    for (Map.Entry<String, Timestamp> p : activeSensor.entrySet()) {

      Timestamp sensorTs = activeSensor.get(p.getKey());

      int s = ts.getSeconds() - sensorTs.getSeconds();

      if (s > 5) {
        sensorActive.remove(p.getKey());    // sensor nach 5
      }

    }

    try {
      FileWriter activeSensorFile = new FileWriter(new File("values", "activeSensor.txt"));

      activeSensorFile.write(ts + " " + String.join(";", sensorActive) + "\n");

      activeSensorFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void setBrokerIp(String brokerIp) {
    this.brokerIp = brokerIp;
  }

  @Override
  public void connectionLost(Throwable throwable) {

  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    System.out.println("Station: " + new String(mqttMessage.getPayload()));

    String jsonMsg = new String(mqttMessage.getPayload());

    JSONObject jsonObject = new JSONObject(jsonMsg);

    String styp = jsonObject.getString("sensorType");
    String svalue = jsonObject.getString("sensorValue");

    //fill map rdy to send
    currentValues.put(styp, svalue);

    //persist in txt file
    activeSensor.put(styp, new Timestamp(System.currentTimeMillis()));
    sensorActive.add(styp);
    processReceivedData(styp, svalue);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

  }

}
