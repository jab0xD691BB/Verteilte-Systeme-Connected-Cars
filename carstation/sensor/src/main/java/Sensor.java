import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class Sensor {

  private String sensorType;
  private String sensorIp;
  private String sensorPort;
  private String sensorValue;

  private DatagramSocket clientSocket;

  private String rootTopic;

  public Sensor(String sensorType, String sensorIp, String sensorPort, String rT) throws SocketException {
    this.sensorType = sensorType;
    this.sensorIp = sensorIp;
    this.sensorPort = sensorPort;
    this.rootTopic = rT;

    clientSocket = new DatagramSocket();

  }

  public String generateValue() {
    Random rand = new Random();

    if ("Tank".equals(sensorType)) {
      sensorValue = String.valueOf(1 + rand.nextInt(100));
    } else if ("Kilometerstand".equals(sensorType)) {
      sensorValue = String.valueOf(1 + rand.nextInt(100 - 000));
    } else if ("Verkehrssituation".equals(sensorType)) {
      String[] vs = {"frei", "mäßiger Verkehr", "starker Verkehr", "Stau"};
      int randNr = rand.nextInt(3);
      sensorValue = vs[randNr];
    } else if ("avgSpeed".equals(sensorType)) {
      sensorValue = String.valueOf(25 + rand.nextInt(100));
    }

    return sensorValue;
  }

  public void sendValue() throws IOException {
    generateValue();

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("sensorType", sensorType);
    jsonObject.put("sensorIp", sensorIp);
    jsonObject.put("sensorPort", sensorPort);
    jsonObject.put("sensorValue", sensorValue);

    /*
    byte[] sendData = new byte[1024];
    sendData = jsonObject.toString().getBytes();

    InetAddress addr = InetAddress.getByName(sensorIp);

    DatagramPacket dp = new DatagramPacket(sendData, sendData.length, addr, Integer.parseInt(sensorPort));
    clientSocket.send(dp);*/

    try {
      MqttClient client = new MqttClient("tcp://" + sensorIp + ":1883", MqttClient.generateClientId());

      MqttConnectOptions options = new MqttConnectOptions();

      options.setCleanSession(true);

      client.connect(options);

      MqttMessage message = new MqttMessage(jsonObject.toString().getBytes());

      message.setQos(1);

      client.publish(rootTopic + "/values/" + sensorType, message);

      System.out.println("Send: " + jsonObject.toString());

      client.disconnect();

    } catch (MqttException e) {
      e.printStackTrace();
    }

  }

  //test function
  public JSONObject sendTest(String json) throws IOException {
    JSONObject jsonObject = new JSONObject(json);

    jsonObject.put("sensorType", "Test");
    jsonObject.put("sensorIp", "127.0.0.1");
    jsonObject.put("sensorPort", "9876");
    jsonObject.put("sensorValue", "15");

    System.out.println("Send: " + jsonObject.toString());

    byte[] sendData = new byte[1024];
    sendData = jsonObject.toString().getBytes();

    InetAddress addr = InetAddress.getByName(sensorIp);

    DatagramPacket dp = new DatagramPacket(sendData, sendData.length, addr,
        Integer.parseInt(sensorPort));
    clientSocket.send(dp);

    return jsonObject;
  }

  public void sendValueDummy() throws IOException {

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("sensorType", "TestMqtt");
    jsonObject.put("sensorIp", sensorIp);
    jsonObject.put("sensorPort", sensorPort);
    jsonObject.put("sensorValue", "123");


    try {
      MqttClient client = new MqttClient("tcp://" + sensorIp + ":1883", MqttClient.generateClientId());

      MqttConnectOptions options = new MqttConnectOptions();

      options.setCleanSession(true);

      client.connect(options);

      MqttMessage message = new MqttMessage(jsonObject.toString().getBytes());

      message.setQos(1);

      client.publish("values/" + "Test", message);

      System.out.println("Send: " + jsonObject.toString());

      client.disconnect();

    } catch (MqttException e) {
      e.printStackTrace();
    }

  }

}
