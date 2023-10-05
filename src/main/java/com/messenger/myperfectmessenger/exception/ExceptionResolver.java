package com.messenger.myperfectmessenger.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ExceptionResolver {
    @ExceptionHandler(value = UserNotFoundException.class)
    ResponseEntity<HttpStatus> userNotFoundException(){
        log.info("UserNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ChatNotFoundException.class)
    ResponseEntity<HttpStatus> chatNotFoundException(){
        log.info("ChatNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MessageNotFoundException.class)
    ResponseEntity<HttpStatus> messageNotFoundException(){
        log.info("MessageNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = FriendNotFoundException.class)
    ResponseEntity<HttpStatus> friendNotFoundException(){
        log.info("FriendNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PostNotFoundException.class)
    ResponseEntity<HttpStatus> postNotFoundException(){
        log.info("PostNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CommentNotFoundException.class)
    ResponseEntity<HttpStatus> commentNotFoundException(){
        log.info("CommentNotFound exception!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<HttpStatus> illegalArgumentException(){
        log.info("IllegalArgument exception!!!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = OptimisticLockingFailureException.class)
    ResponseEntity<HttpStatus> optimisticLockingFailureException(){
        log.info("OptimisticLockingFailure exception!!!");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = IOException.class)
    ResponseEntity<HttpStatus> IOException(){
        log.info("IO exception!!!, failed to update profile image");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
