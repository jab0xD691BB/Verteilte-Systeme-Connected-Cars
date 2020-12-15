import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class main {

  public static void main(String[] args) throws UnsupportedEncodingException {
    String path = main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    System.out.println("path: " + decodedPath);

    //Udplistener[] udpl = new Udplistener[4]; //2 request auf dem gleichen port geht noch, ab 3 spinnt das programm. mit threads läufts gut
    //int portstart = 9876;
    try {
    /*  for (int i = 0; i < udpl.length; i++) {
        udpl[i] = new Udplistener(portstart);
        Thread t = new Thread(udpl[i]);
        t.start();
        portstart++;
      }*/
      Thread ta = new Thread(new Udplistener(9876));
      ta.start();

    } catch (IOException e) {
      return;
    }

    try {
      Thread t2 = new Thread(new HttpApi());
      t2.start();

    } catch (Exception e) {
      System.exit(1);
    }


  }

}
