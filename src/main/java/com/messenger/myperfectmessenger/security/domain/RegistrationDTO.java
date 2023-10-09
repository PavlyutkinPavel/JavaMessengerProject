package com.messenger.myperfectmessenger.security.domain;

import lombok.Data;

@Data
public class RegistrationDTO {

    private String firstName;

    private String lastName;

    private String userLogin;

    private String userPassword;
}
