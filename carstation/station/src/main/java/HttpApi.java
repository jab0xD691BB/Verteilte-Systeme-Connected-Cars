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
import java.nio.file.Paths;
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

        DataOutputStream httpOut = new DataOutputStream(connectionSocket.getOutputStream());

        HttpReq httpReq = new HttpReq(httpIn);


        int i = 0;
        if (httpReq.path.equals(".")) {

          if(httpReq.file.equals("favicon.ico")){
            byte[] fileContent = Files.readAllBytes(new File(httpReq.file).toPath());
            String body = new String(fileContent);
            String header = this
                .writeHttpHeader("200 Ok", Integer.toString(body.length()), "application/json");
            httpOut.writeBytes(header.concat(body));

          }else{
            String body = getContentOfFile(httpReq);
            String header = this
                .writeHttpHeader("200 Ok", Integer.toString(body.length()), "text/html");
            httpOut.writeBytes(header.concat(body));
          }

        } else if (httpReq.path.equals("values")) {
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

    try {
      File file = new File(httpReq.path,httpReq.file);
      Scanner myReader = new Scanner(file);
      if (httpReq.path.equals(".")) {


        StringBuilder line = new StringBuilder();
        while (myReader.hasNextLine()) {
          line.append(myReader.nextLine());
        }

        return line.toString();
      } else {
        JSONObject jsonObject = new JSONObject();

        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          if(!httpReq.file.contains("active")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(data.split(" ")[0] + " " + data.split(" ")[1]);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            jsonObject.put(timestamp.toString(),
                data.split(" ")[2]); // 0+1: timestamp 2: value
          }else{
            jsonObject.put("activeSensor", data);
            return  jsonObject.toString();
          }
          System.out.println("data: " + data);
        }

        JSONObject jsonObjectArr = new JSONObject(); //titel
        jsonObjectArr.put(httpReq.file, jsonObject);

        return jsonObjectArr.toString(2);
      }

    } catch (FileNotFoundException | ParseException e) {

    }

    return "";
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
