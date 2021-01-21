import java.sql.Timestamp;

public class Server{

  public String name;
  public Timestamp timestamp;
  public boolean isPrimary = false;
  public boolean isAlive = false;
  public String ip;
  public int port;

  public Server(String name, Timestamp timestamp, boolean isAlive, String ip, int port) {
    this.name = name;
    this.timestamp = timestamp;
    this.isAlive = isAlive;
    this.ip = ip;
    this.port = port;

  }


  public String toString(){
    return "Name: " + name + "\tTime: " + timestamp + "\tPrimary: " + isPrimary + "\tAlive: " +isAlive;
  }

}
