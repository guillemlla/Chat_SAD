package com.example.guillemllados.chatv3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guillemllados on 9/5/17.
 */

public class Controller {

    private MySocket s;
    private ArrayList<User> users;
    private ConcurrentHashMap<Integer,MyMessage> myMessages;
    private User me;
    private MessageReciver messageReciver;

    public static Controller c;

    public Controller(){
        c = this;
        users = new ArrayList<User>();
        myMessages = new ConcurrentHashMap<Integer,MyMessage>();
        //initialize();


    }

    public MySocket getMySocket() {
        return s;
    }
    public void setMySocket(MySocket s) {
        this.s = s;
    }
    public User getMe() {
        return me;
    }
    public void setMe(User me) {
        this.me = me;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    /*public ArrayList<MyMessage> getMyMessages() {
        return myMessages.values();
    }*/
    public void putMessage(MyMessage m){
        myMessages.put(myMessages.size(),m);
    }
    public ConcurrentHashMap<Integer,MyMessage> getMyMessages(){
        return myMessages;
    }

    public void initialize(){

        User u1 = new User("ferran");
        User u2 = new User("sebas");
        User u3 = new User("marta");
        User u4 = new User("pere");

        MyMessage m = new MyMessage(u1,"Hola");
        putMessage(m);
        m = new MyMessage(me,"Que tal");
        putMessage(m);
        m = new MyMessage(u2,"Com vas?");
        putMessage(m);
        m = new MyMessage(u3,"Molt be");
        putMessage(m);
        m = new MyMessage(u1,"HEHEHEHE");
        putMessage(m);

    }


}
