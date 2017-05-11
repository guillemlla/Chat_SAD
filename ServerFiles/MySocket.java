import java.net.*;
import java.lang.Exception.*;
import java.io.*;
import java.nio.charset.*;

public class MySocket{

  private OutputStream output;
  private InputStream input;
  private Socket s;

  public MySocket(Socket s){
    this.s = s;
    try{
      output = s.getOutputStream();
      input = s.getInputStream();
    }catch(Exception e){
      e.printStackTrace();
    }
  }



  public void println(String line){

    try{
      byte[] b = new byte[100];
      System.out.println("Line a enviar: "+line);
      b = line.getBytes(StandardCharsets.UTF_8);

      System.out.print("Bytes a enviat: ");
      for(byte bit: b){
        System.out.print(bit+" ");
      }
      output.write(b);
    }catch(Exception e){
      close();
      e.printStackTrace();
    }

  }

  public String readLine(){

    try{
      if(input.available() >0){
        byte[] b = new byte[100];
        input.read(b);
        String line = new String(b, StandardCharsets.UTF_8);


        return line;
      }
    }catch(Exception e){
      close();
      e.printStackTrace();
    }
    return"-1";
  }

  public boolean isClosed(){
    return s.isClosed();
  }

  public void close(){
    try{
      s.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }



}
