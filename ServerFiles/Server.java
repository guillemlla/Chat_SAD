import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server{



  public static void main(String[] args){

    ConcurrentHashMap<User,MySocket> sockets  = new ConcurrentHashMap<User,MySocket>();

    try{
      ServerSocket serverSocket = new ServerSocket(1222);
      MyServerSocket ss = new MyServerSocket(serverSocket);

        while(true){
          MySocket s = ss.accept();


          new Thread(){
            //Per passar arguments sense constructor
            private MySocket s ;
            private User u;

            public void run(){
                String message;
                String[] messageDiv;
                if(!s.isClosed()){
                  System.out.println("NEW USER");
                  identifyNewUser(s);
                }

                while(!s.isClosed()){
                  if(!((message = s.readLine()).equals("-1"))){

                        messageDiv = message.split(":");

                        switch (messageDiv[0]) {
                          case MESSAGE_CODES.STD_MESSAGE_SEND:{

                            String toSend = MESSAGE_CODES.STD_MESSAGE_RECIVE+":" + messageDiv[1]+":"+u.getUsername()+":ENDOFTHEMESSAGE";
                            System.out.println("Missatge= "+ toSend);
                            for(User userDest : sockets.keySet()){
                                getSocket(userDest).println(toSend);
                                System.out.println("Message= "+toSend+" Sended to= "+userDest.getUsername());
                            }

                            break;
                          }case MESSAGE_CODES.DISCONECT:{

                            s.close();
                            sockets.remove(u);
                            break;
                          }
                        }

                    }
                }

                try{
                  s.close();
                  System.out.println("Cerrada");
                }catch(Exception e){
                  e.printStackTrace();
                }
            }
            public void start(MySocket s){
              this.s = s;
              start();
            }

            public void identifyNewUser(MySocket socket){

              String message;
              this.u = new User("-1");
              while((!socket.isClosed()) && this.u.getUsername().equals("-1")){

                if(!((message = s.readLine()).equals("-1"))){
                  System.out.println("New message= "+message);
                  String[] strDiv = message.split(":");

                  if(strDiv[0].equals(MESSAGE_CODES.CONNECT_TO_SERVER)){
                    System.out.print("Connect to server merssage");
                    String username = strDiv[1];
                    if(!userExist(username)){
                      this.u = new User(username);
                      sockets.put(u,socket);
                      System.out.println("EN ACK SEND a "+u.getUsername());

                      String ACK = MESSAGE_CODES.ACK+":ENDOFTHEMESSAGE";
                      socket.println(ACK);

                    }else{
                      System.out.println("EN NACK SEND a "+u.getUsername());

                      String NACK = MESSAGE_CODES.NACK+":ENDOFTHEMESSAGE";
                      socket.println(NACK);
                    }
                  }
                }
              }
            }

            public boolean userExist(String username){
              for(User u : sockets.keySet()) if(u.getUsername().equals(username)) return true;
              return false;
            }

            public MySocket getSocket(User u){

              return sockets.get(u);

            }

          }.start(s); //Per passar arguments
        }


    }catch(Exception e){
      e.printStackTrace();

    }
  }
}

class MESSAGE_CODES {

  public static final String CONNECT_TO_SERVER = "10";
    public static final String DISCONECT = "15";



    public static final String ACK = "20";
    public static final String NACK = "25";

    public static final String STD_MESSAGE_SEND = "30";
    public static final String STD_MESSAGE_RECIVE = "35";


}
