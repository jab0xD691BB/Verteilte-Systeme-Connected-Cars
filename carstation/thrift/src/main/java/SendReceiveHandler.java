import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.apache.thrift.TException;
import simpleServer.sendReceive;

public class SendReceiveHandler implements sendReceive.Iface {

  public SendReceiveHandler() {

  }

  @Override
  public void sendCurrent(Map<String, String> sv) throws TException {
    for (Map.Entry<String, String> entry : sv.entrySet()) {
      System.out.println(
          System.currentTimeMillis() + " SensorTyp: " + entry.getKey() + " - Sensorvalue: " + entry
              .getValue());
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      Date date = new Date(timestamp.getTime());

      try {
        if (!entry.getKey().equals("Test")) {
          FileWriter currWriter = new FileWriter(new File("current" + entry.getKey() + "value.txt"),
              true);

          currWriter.write(date + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");

          currWriter.close();
        } else if(entry.getKey().equals("Test")) {
          //TEST
          FileWriter currWriter = new FileWriter(new File("current" + entry.getKey() + "value.txt"),
              true);

          currWriter.write(date + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");

          currWriter.close();

          if (entry.getValue().equals("3")) {
            System.out.println("TEST");
            try (BufferedReader br = new BufferedReader(
                new FileReader("current" + entry.getKey() + "value.txt"))) {
              String line;
              ArrayList<String> stringArr = new ArrayList<>();
              while ((line = br.readLine()) != null) {
                stringArr.add(line.split("\t")[2]);
                System.out.println(line.split("\t")[2]);
              }

              boolean testCheck2 = false;
              if (("0".equals(stringArr.get(0))) && ("1".equals(stringArr.get(1))) && ("2"
                  .equals(stringArr.get(2))) && ("3".equals(stringArr.get(3)))) {
                testCheck2 = true;
              }
              if (stringArr.size() > 4) {
                testCheck2 = false;
              }

              br.close();

              if(testCheck2){
                System.out.println("Test 2 erfolgreich");
                File file = new File("current" + entry.getKey() + "value.txt");
                if (file.delete()) {
                  System.out.println("File deleted");
                }
              }else
              {
                System.out.println("Test 2 fehlgeschlagen");
              }

            }

          }

        }else if(entry.getKey().equals("Test2")){
          FileWriter currWriter = new FileWriter(new File("current" + entry.getKey() + "value.txt"),
              true);

          currWriter.write(date + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");

          currWriter.close();
        }

      } catch (IOException e) {

      }

    }

  }


}
