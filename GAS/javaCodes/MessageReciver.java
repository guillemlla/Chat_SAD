package com.example.guillemllados.chatv3;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

/**
 * Created by guillemllados on 9/5/17.
 */

public class MessageReciver extends Thread {

    Controller c;
    MySocket s;
    Activity a;
    ChatAdapter adapter;

    public MessageReciver(MainActivity a, Controller c,ChatAdapter adapter, MySocket s) {
        this.c = c;
        this.s = s;
        this.a = a;
        this.adapter = adapter;
    }

    @Override
    public void run() {

        c = Controller.c;

        String message;
        s = c.getMySocket();
        while(!s.isClosed()) {
            if (!((message = s.readLine()).equals("-1"))) {

                String[] messageDiv = message.split(":");

                switch (messageDiv[0]) {
                    case MESSAGE_CODES.STD_MESSAGE_RECIVE: {

                        User u = new User(messageDiv[2]);
                        final MyMessage m = new MyMessage(u,messageDiv[1]);
                        if(!c.getUsers().contains(u)) c.getUsers().add(u);


                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                c.getMyMessages().add(m);
                                ListView listaMensages = (ListView) a.findViewById(R.id.chatListView);
                                adapter.notifyDataSetChanged();
                                listaMensages.invalidateViews();

                                listaMensages.smoothScrollToPosition(listaMensages.getCount());
                            }
                        });


                        break;

                    }
                }

            }
        }

    }



}
