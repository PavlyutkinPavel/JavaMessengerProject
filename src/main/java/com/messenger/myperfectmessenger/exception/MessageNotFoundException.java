package com.messenger.myperfectmessenger.exception;

import com.messenger.myperfectmessenger.domain.Message;

public class MessageNotFoundException extends RuntimeException{
    public MessageNotFoundException(){
        super("Message not found");
    }
}
