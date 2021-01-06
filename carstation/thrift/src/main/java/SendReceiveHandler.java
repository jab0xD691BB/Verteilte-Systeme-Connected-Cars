import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.apache.thrift.TException;
import simpleServer.sendReceive;

public class SendReceiveHandler implements sendReceive.Iface{

  public SendReceiveHandler() {
  }

  public void ping() throws TException {
    System.out.println("ping()");
  }

  @Override
  public void sendCurrent(Map<String, String> sv) throws TException {
    for(Map.Entry<String, String> entry : sv.entrySet()){
      System.out.println( System.currentTimeMillis() + " SensorTyp: " + entry.getKey() + " - Sensorvalue: " + entry.getValue());

      try{
        FileWriter currWriter = new FileWriter(
            new File("current" + entry.getKey() + "value.txt"));

        currWriter.write(entry.getKey() + "\t" + entry.getValue() + "\n");

        currWriter.close();
      }catch (IOException e){

      }

    }


  }


}
