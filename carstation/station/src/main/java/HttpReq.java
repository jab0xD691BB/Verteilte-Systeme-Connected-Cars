import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class HttpReq {

  private ArrayList<String> http;

  public String method = "";
  public String path = "";

  boolean first = true;

  public HttpReq(BufferedReader br) throws IOException {


    StringBuilder result = new StringBuilder();
    System.out.println(br.toString());
    String line;
    while((line = br.readLine()) != null  && !line.equals("")){
    if(first){
      first = false;
      method = line.split(" ")[0];
      path = line.split(" ")[1];
      System.out.println(line);
    }




      System.out.println(line);
    }
  }
}
