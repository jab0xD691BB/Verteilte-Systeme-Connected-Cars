import java.net.DatagramPacket;
import java.net.SocketException;

public class main {

  public static void main(String[] args) throws SocketException {
    Sensor s = null;

    try {
      s = new Sensor(args[0], args[1], args[2]);

    } catch (ArrayIndexOutOfBoundsException e) {
      //sensorTyp = "Tank";
      //sensorIp = "127.0.0.1";
      //sensorPort = "9876";
      s = new Sensor("Tank", "127.0.0.1", "9876");
    }

    while (true) {
      try {
        s.sendValue();
        Thread.sleep(2000);
      } catch (Exception e) {
        Thread.currentThread().interrupt();
      }


    }

  }

}
