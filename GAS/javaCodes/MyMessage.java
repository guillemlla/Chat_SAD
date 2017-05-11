package com.example.guillemllados.chatv3;

/**
 * Created by guillemllados on 9/5/17.
 */

public class MyMessage {

    private User user;
    private String text;

    public MyMessage(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
