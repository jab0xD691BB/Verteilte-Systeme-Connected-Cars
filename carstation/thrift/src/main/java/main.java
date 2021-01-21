import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import simpleServer.sendReceive;
import simpleServer.sendReceive.Processor;

public class main{



  public static void main(String[] args) {


    Thread station = new Thread(new ReceiveStation());
    station.start();


  }



}
