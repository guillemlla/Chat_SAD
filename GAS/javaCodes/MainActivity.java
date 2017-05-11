package com.example.guillemllados.chatv3;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    private ListView listaMensages;
    private ChatAdapter adaptadorChats;
    private Controller c;
    private Button send;
    private EditText message;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = Controller.c;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Conversations");


        listaMensages = (ListView) findViewById(R.id.chatListView);
        adaptadorChats = new ChatAdapter(this, c.getMyMessages());

        MessageReciver messageReciver = new MessageReciver(this,c,adaptadorChats,c.getMySocket());
        messageReciver.start();

        listaMensages.setAdapter(adaptadorChats);

        send = (Button) findViewById(R.id.buttonSend);
        message = (EditText) findViewById(R.id.ETMessages);
    }

    @Override
    protected void onPause() {
        super.onPause();

        DisconnectTask disconnectTask = new DisconnectTask(c.getMySocket());
        disconnectTask.execute();

    }

    public void onSendClick(View v){

        if(!message.getText().toString().equals("")) {
            SendMessageTask sTask = new SendMessageTask(message.getText().toString(),c.getMySocket());
            sTask.execute(true);
            message.setText("");
        }

    }


    public class SendMessageTask extends AsyncTask<Boolean, Void, Boolean> {

        private final MySocket s;
        private String text;

        public SendMessageTask(String text,MySocket s) {
            this.text = text;
            this.s = s;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            try {

                String toSend = MESSAGE_CODES.STD_MESSAGE_SEND+":"+text+":ENDOFMESSAGE";
                s.println(toSend);
                return true;

            } catch (Exception e) {
                return false;
            }

        }

    }

    public class DisconnectTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final MySocket s;

        public DisconnectTask(MySocket s) {
            this.username = c.getMe().getUsername();

            this.s = s;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String toSend = MESSAGE_CODES.DISCONECT+":"+username+":ENDOFMESSAGE";
                s.println(toSend);
                return true;

            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
             Controller.c = new Controller();
        }
    }
}
