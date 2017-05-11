import java.net.*;
import java.io.*;

public class MyServerSocket{

  private Socket s;
  private ServerSocket ss;

  public MyServerSocket(ServerSocket ss){
    this.ss =ss;
  }

  public MySocket accept(){
    try{

      Socket s = ss.accept();
      while(s == null) s = ss.accept();

      MySocket ms = new MySocket(s);
      return ms;
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }





}
