import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class HttpReq {

  private ArrayList<String> http;

  public String method = "";
  public String path = "";
  public String file = "";


  boolean first = true;

  public HttpReq(BufferedReader br) throws IOException {


    //System.out.println(br.toString());
    String line;
    while ((line = br.readLine()) != null && !line.equals("")) {
      if (first) {
        first = false;
        String[] url = line.split(" ");

        method = url[0];

        if (url[1].length() > 1) {
          String[] path = url[1].split("/");
          if (path[1].contains(".")) {
            this.path = ".";
            file = path[1];
          } else if (path[1].contains("values")) {
            this.path = path[1];
            this.file = path[2];
          }

        } else {
          this.path = ".";
          this.file = "index.html";
        }

        //System.out.println(line);
      }

      //System.out.println(line);
    }
  }
}
