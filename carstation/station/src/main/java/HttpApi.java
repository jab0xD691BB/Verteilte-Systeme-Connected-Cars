import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONObject;

public class HttpApi implements Runnable {

  private static int PORT = 3124;

  private ServerSocket tcpServerSocket;


  public HttpApi() throws IOException {
    tcpServerSocket = new ServerSocket(PORT);
  }

  private boolean running = true;

  @Override
  public void run() {
    System.out.println("http server running");
    boolean error = false;
    while (running) {
      Socket connectionSocket = null;
      try {

        connectionSocket = this.tcpServerSocket.accept();
        BufferedReader httpIn = new BufferedReader(
            new InputStreamReader(connectionSocket.getInputStream()));

        //System.out.println(connectionSocket.toString());
        DataOutputStream httpOut = new DataOutputStream(connectionSocket.getOutputStream());

        HttpReq httpReq = new HttpReq(httpIn);

        if (httpReq.path.equals("/")) {
          String body = "<html>\n"
              + "<head>\n"
              + "</head>\n"
              //  + <link rel=\"stylesheet\" type=\"text/css\" href=\"test.css\" media=\"screen\" />\n für css
              + "<body>\n"
              + "    <div id=\"content\">\n"
              + "    <h1>Sensor</h1>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/currentTankvalue\">momentaner tank wert</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/historyTankvalue\">alle tank werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/currentKilometerstandvalue\">momentaner Kilometerstand werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/historyKilometerstandvalue\">alle Kilometerstand werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/currentVerkehrssituationvalue\">momentaner Verkehrssituation werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/historyVerkehrssituationvalue\">alle Verkehrssituation werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/currentavgSpeedvalue\">momentaner avgSpeed werte</a></p>\n"
              + "    <p><a href=\"http://127.0.0.1:3124/values/historyavgSpeedvalue\">alle avgSpeed werte</a></p>\n"
              + "    </div>  \n"
              + "</body>\n"
              + "</html>";
          String header = this
              .writeHttpHeader("200 Ok", Integer.toString(body.length()), "text/html");
          httpOut.writeBytes(header.concat(body));

          // }else{
          //String header = this.writeHttpHeader("404 Not Found", "0");
          //httpOut.writeBytes(header);
        } else if (httpReq.path.equals("/favicon.ico")) {
          int i = 0;
          String fileName = httpReq.path.replace("/", "");
          File f = new File(fileName);
          FileInputStream fin = new FileInputStream(f);

          byte[] fileContent = Files.readAllBytes(f.toPath());
          String body = new String(fileContent);
          String header = this
              .writeHttpHeader("200 Ok", Integer.toString(body.length()), "application/json");
          httpOut.writeBytes(header.concat(body));

        } else if (httpReq.path.contains("values")) {
          String body = getContentOfFile(httpReq);
          String header = this
              .writeHttpHeader("200 Ok", Integer.toString(body.length()), "application/json");
          httpOut.writeBytes(header.concat(body));
        }


      } catch (IOException e) {
        error = true;
      } finally {
        if (connectionSocket != null) {
          try {
            connectionSocket.close();
          } catch (IOException e) {

          }
        }
      }


    }
  }

  private String writeHttpHeader(String status,
      String length, String cType) {  //struggle richtige http string zu bauen wichtig: \r\n
    String httpHeader = "HTTP/1.1 " + status + "\r\n";
    httpHeader += "Content-Length: " + length + "\r\n";
    httpHeader += "Access-Control-Allow-Origin: *\r\n";
    httpHeader += "Content-Type: " + cType + "\r\n\r\n";  // text/html || application/json

    return httpHeader;
  }

  private String getContentOfFile(HttpReq httpReq) {
    JSONObject jsonObject = new JSONObject();

    String[] pathArr = httpReq.path.split("/");
    String dir = pathArr[1];
    String fileName = pathArr[2];

    try {
      File file = new File(dir, fileName + ".txt");
      System.out.println("GET FILE: " + file.getPath());
      Scanner myReader = new Scanner(file);

      System.out.println("can read: " + file.canRead());
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = dateFormat.parse(data.split(" ")[0] + " " + data.split(" ")[1]);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        jsonObject.put(timestamp.toString(),
            data.split(" ")[2]); // 0+1: timestamp 2: value
        System.out.println("data: " + data);
      }

    } catch (FileNotFoundException | ParseException e) {

    }

    JSONObject jsonObjectArr = new JSONObject(); //titel
    jsonObjectArr.put(fileName, jsonObject);

    return jsonObjectArr.toString(2);
  }

  private void printInputStream(BufferedReader bufferedReader) {
    try {
      // Read a text-line form the buffer.
      String streamLine = bufferedReader.readLine();
      // Print the packet information.
      System.out.println("Received some information: " + streamLine);
    } catch (IOException e) {
    }
  }

}
