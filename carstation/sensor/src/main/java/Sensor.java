import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import org.json.JSONObject;

public class Sensor {

  private String sensorType;
  private String sensorIp;
  private String sensorPort;
  private String sensorValue;

  private DatagramSocket clientSocket;

  public Sensor(String sensorType, String sensorIp, String sensorPort) throws SocketException {
    this.sensorType = sensorType;
    this.sensorIp = sensorIp;
    this.sensorPort = sensorPort;

    clientSocket = new DatagramSocket();

  }

  public String generateValue(){
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

   /* StringBuilder jsonFormat = new StringBuilder(); "" fehlt bei den werten
    jsonFormat.append("{")
        .append("sensorType").append(":").append(sensorType).append(",")
        .append("sensorIp").append(":").append(sensorIp).append(",")
        .append("sensorPort").append(":").append(sensorPort).append(",")
        .append("sensorValue").append(":").append(sensorValue)
        .append("}");
    */

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("sensorType", sensorType);
    jsonObject.put("sensorIp", sensorIp);
    jsonObject.put("sensorPort", sensorPort);
    jsonObject.put("sensorValue", sensorValue);

    System.out.println("Send: " + jsonObject.toString());

    byte[] sendData = new byte[1024];
    sendData = jsonObject.toString().getBytes();

    InetAddress addr = InetAddress.getByName(sensorIp);

    DatagramPacket dp = new DatagramPacket(sendData, sendData.length, addr, Integer.parseInt(sensorPort));
    clientSocket.send(dp);
  }
}
