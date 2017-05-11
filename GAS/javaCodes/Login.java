package com.example.guillemllados.chatv3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class Login extends AppCompatActivity {

    private EditText usernameView;
    private EditText ipView;
    private MySocket mySocket;
    private TextView errorText;

    private Controller c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        c = new Controller();
        c = Controller.c;

        usernameView = (EditText) findViewById(R.id.username);
        ipView = (EditText) findViewById(R.id.ipView);
        errorText  =(TextView) findViewById(R.id.errorTextView);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usernameView.getText().equals("") && !ipView.getText().equals("")) attemptLogin();
            }
        });
    }

    public void attemptLogin(){

        try{
            ConnectToSocketTask connectToSocketTask = new ConnectToSocketTask(ipView.getText().toString());
            connectToSocketTask.execute();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class ConnectToSocketTask extends AsyncTask<Void, Void, MySocket> {

        private final String ip;

        public ConnectToSocketTask(String ipAdress) {
            ip = ipAdress;
        }
        protected MySocket doInBackground(Void... params) {

            try {

                //InetAddress local =  InetAddress.getByName(ip);
                Socket s = new Socket();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,1222);
                s.connect(inetSocketAddress);
                MySocket ms = new MySocket(s);
                return ms;

            }catch (SocketTimeoutException sExeption){
                return null;
            }catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(MySocket mySocketGet) {
            super.onPostExecute(mySocketGet);
            if(mySocketGet!= null){
                mySocket = mySocketGet;
                UserLoginTaskSend taskSend = new UserLoginTaskSend(usernameView.getText().toString(),mySocket);
                taskSend.execute();
            }else{
                errorText.setText("Server not found");
            }
        }
    }

    public class UserLoginTaskSend extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final MySocket s;

        public UserLoginTaskSend(String username, MySocket s) {
            this.username = username;

            this.s = s;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String toSend = MESSAGE_CODES.CONNECT_TO_SERVER+":"+username+":ENDOFMESSAGE";
                s.println(toSend);
                return true;

            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            UserLoginTaskRecieve taskRecieve = new UserLoginTaskRecieve(mySocket);
            taskRecieve.execute();
        }
    }

    public class UserLoginTaskRecieve extends AsyncTask<Void, Void, String> {

        private final MySocket s;

        public UserLoginTaskRecieve(MySocket s) {
            this.s  = s ;
        }
        @Override
        protected String doInBackground(Void... params) {

            try {

                String message = null;
                while(((message = s.readLine()).equals("-1")));
                String response[] = message.split(":");
                return response[0];


            } catch (Exception e) {
                return "";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(MESSAGE_CODES.ACK) ){

                Controller.c.setMe(new User(usernameView.getText().toString()));
                Controller.c.setMySocket(mySocket);
                errorText.setText("");

                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);

            }else{
                usernameView.setText("");
                ipView.setText("");
                errorText.setText("Username not avaiable");
            }
        }


    }

}
