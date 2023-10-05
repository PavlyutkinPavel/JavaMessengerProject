package com.messenger.myperfectmessenger.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(){
        super("Comment not found");
    }
}
