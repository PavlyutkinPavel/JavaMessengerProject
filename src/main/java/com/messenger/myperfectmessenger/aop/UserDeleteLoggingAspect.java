package com.messenger.myperfectmessenger.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserDeleteLoggingAspect {

    @AfterReturning(
            pointcut = "execution(* com.messenger.myperfectmessenger.controller.UserController.deleteUser(..)) && args(id)",
            returning = "result")
    public void logUserDeletion(Long id, ResponseEntity<?> result) {
        if (result != null && result.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("User with ID " + id + " has been deleted.");
        }
    }
}
