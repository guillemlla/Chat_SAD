package com.example.guillemllados.chatv3;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by guillemllados on 9/5/17.
 */

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

            byte[] b = line.getBytes(StandardCharsets.UTF_8);
            output.write(b);
        }catch(Exception e){
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