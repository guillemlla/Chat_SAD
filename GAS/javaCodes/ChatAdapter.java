package com.example.guillemllados.chatv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guillemllados on 9/5/17.
 */

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private ConcurrentHashMap<Integer,MyMessage> myMessages;

    public ChatAdapter(Context context, ConcurrentHashMap<Integer,MyMessage> items) {
        this.context = context;
        this.myMessages = items;
    }

    @Override
    public int getCount() {
        return this.myMessages.size();
    }

    @Override
    public MyMessage getItem(int position) {
        return this.myMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        MyMessage e = this.getItem(position);

        if (true){

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(e.getUser().getUsername().equals(Controller.c.getMe().getUsername())){

                rowView = inflater.inflate(R.layout.chat_bubble_right, parent, false);

            }else{
                rowView = inflater.inflate(R.layout.chat_bubble_left, parent, false);
            }

        }


        TextView tvName = (TextView) rowView.findViewById(R.id.chatUsername);
        TextView tvMessage = (TextView) rowView.findViewById(R.id.chatText);
        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.bubbleLayout);

        tvName.setText(e.getUser().getUsername());
        tvMessage.setText(e.getText());


        return rowView;
    }

}