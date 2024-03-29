import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.sql.Timestamp;
import javax.sound.midi.Soundbank;
import org.json.JSONObject;

public class main {

  public static void main(String[] args) throws SocketException {

    Sensor sensor = null;
    int testFlag = 1;

    try {
      Thread.sleep((long) (Math.random() * 1000));

      sensor = new Sensor(args[0], args[1], args[2], args[3]);

    } catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
      sensor = new Sensor("Tank", "127.0.0.1", "9876", "auto");
    }

    if (testFlag == 0) {
      try{
        //funktional
        sensor.sendValueDummy();

        //performance
        for(int i = 0; i < 1000; i++){
          sensor.sendValueDummy();
        }

      }catch (IOException e){
        e.printStackTrace();
      }


      /*String strDummy = "{'sensorType':'Test','sensorPort':'9876','sensorIp':'127.0.0.1','sensorValue':'15'}";
      Sensor sTest = new Sensor("Test", args[1], "9876");
      JSONObject jsonDummy = new JSONObject();

      try {

        JSONObject sendTestJson = sTest.sendTest(strDummy);
        jsonDummy.put("sensorType", "Test");
        jsonDummy.put("sensorIp", "127.0.0.1");
        jsonDummy.put("sensorPort", "9876");
        jsonDummy.put("sensorValue", "15");

        if (sendTestJson.toString().equals(jsonDummy.toString())) {
          System.out.println("Funktionaler Test: JSON hat die Daten richtig formatiert.");
        }

        for (int i = 0; i < 10; i++) {
          long timestampStart = System.currentTimeMillis();
          sensor.sendValue();
          long timestampEnd = System.currentTimeMillis();
          long dauer = timestampEnd - timestampStart;
          System.out.println("Performancetest Aufgabe 2 dauer: " + dauer + " ms");
          Thread.sleep(2000);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      int counter = 0;
      try {

        for (int i = 0; i < 1000; i++) {
          System.out.print(counter + ": ");
          sensor.sendTest(strDummy);
          counter++;
        }
      } catch (Exception e) {

      }

      System.out.println("NICHT FUNKTIONALER TEST");*/


    }

    while (true) {
      try {
        sensor.sendValue();
        Thread.sleep(2000);
      } catch (Exception e) {
        Thread.currentThread().interrupt();
      }
    }



  }

}
