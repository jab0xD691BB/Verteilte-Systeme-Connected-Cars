import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Timestamp;
import org.json.*;


public class Udplistener implements Runnable {

  private DatagramSocket serverSocket;
  private int port;
  private boolean running = true;

  public Udplistener(int port) throws SocketException {
    this.port = port;
    serverSocket = new DatagramSocket(port);

  }


  public void run() {
    System.out.println("udp is listening to port " + port);
    while (running) {
      System.out.flush();
      byte[] receiveData = new byte[1024];
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

      try {
        serverSocket.receive(receivePacket);
        String r = new String(receivePacket.getData()).replaceAll("\u0000.*", "");

        JSONObject jsonObject = new JSONObject(r);

        System.out.println("receive: " + r);

        processReceivedData(jsonObject.getString("sensorType"),
            jsonObject.getString("sensorValue"));

      } catch (Exception e) {
        e.printStackTrace();
        running = false;
      }

    }
  }

  private void processReceivedData(String sensorType, String sensorValue) {
    try {
      FileWriter currWriter = new FileWriter(
          new File("values", "current" + sensorType + "value.txt"));
      FileWriter historyWriter = new FileWriter(
          new File("values", "history" + sensorType + "value.txt"), true);

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      currWriter.write(timestamp + " " + sensorValue + "\n");
      historyWriter.write(timestamp + " " + sensorValue + "\n");

      currWriter.close();
      historyWriter.close();

    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
    }
  }
}
