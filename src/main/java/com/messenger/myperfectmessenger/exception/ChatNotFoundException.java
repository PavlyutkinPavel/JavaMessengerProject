package com.messenger.myperfectmessenger.exception;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException(){
            super("Chat not found");
        }
}

