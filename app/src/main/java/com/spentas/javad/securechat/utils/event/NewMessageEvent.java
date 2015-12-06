package com.spentas.javad.securechat.utils.event;

import com.spentas.javad.securechat.model.Message;

/**
 * Created by javad on 11/21/2015.
 */
public class NewMessageEvent {
    private Message msg;
    public NewMessageEvent(Message msg){
        this.msg = msg;
    }
    public Message getMsg (){
        return msg;
    }
}
