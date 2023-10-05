package com.messenger.myperfectmessenger.exception;

public class FriendNotFoundException extends RuntimeException{
    public FriendNotFoundException(){
        super("Friend not found");
    }
}
