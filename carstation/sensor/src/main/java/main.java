import java.net.DatagramPacket;
import java.net.SocketException;
import java.sql.Timestamp;
import javax.sound.midi.Soundbank;
import org.json.JSONObject;

public class main {

  public static void main(String[] args) throws SocketException {


    Sensor s = null;
    int testFlag = 0;

    try {
      Thread.sleep((long)(Math.random() * 1000)); //prevent spamming?

      s = new Sensor(args[0], args[1], args[2]);

    } catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
      //sensorTyp = "Tank";
      //sensorIp = "127.0.0.1";
      //sensorPort = "9876";
      s = new Sensor("Tank", "127.0.0.1", "9876");
    }

    if(testFlag == 1) {
      String strDummy = "{'sensorType':'Test','sensorPort':'9876','sensorIp':'127.0.0.1','sensorValue':'15'}";

      try {
        Sensor sTest = new Sensor("Test", "127.0.0.1", "9876");

        JSONObject sendTestJson = sTest.sendTest(strDummy);
        JSONObject jsonDummy = new JSONObject();
        jsonDummy.put("sensorType", "Test");
        jsonDummy.put("sensorIp", "127.0.0.1");
        jsonDummy.put("sensorPort", "9876");
        jsonDummy.put("sensorValue", "15");

        if (sendTestJson.toString().equals(jsonDummy.toString())) {
          System.out.println("Funktionaler Test: JSON hat die Daten richtig formatiert.");
        }

        for (int i = 0; i < 10; i++) {
          long timestampStart = System.currentTimeMillis();
          s.sendValue();
          long timestampEnd = System.currentTimeMillis();
          long dauer = timestampEnd - timestampStart;
          System.out.println("Performancetest Aufgabe 2 dauer: " + dauer + " ms");
          Thread.sleep(2000);
        }
      } catch (Exception e) {

      }

      int counter = 0;
      try {

        for (int i = 0; i < 1000; i++) {
          System.out.print(counter + ": ");
          s.sendTest(strDummy);
          counter++;
        }
      } catch (Exception e) {

      }

      System.out.println("NICHT FUNKTIONALER TEST");

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
