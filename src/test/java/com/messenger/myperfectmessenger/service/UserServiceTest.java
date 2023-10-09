package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.repository.UserRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityCredentialsRepository securityCredentialsRepository;

    @BeforeAll
    public static void setBankingService() {
        //имитации
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        //аналог метода в сервисе
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);


    }
    @Test
    public void getUserTest() throws Exception {
        Long userId = 1L;
        String adminUsername = "admin";

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(userId);
        adminCredentials.setUserRole(Role.ADMIN);

        User user = new User();
        user.setId(userId);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUser(userId, mockPrincipal);

        Assertions.assertEquals(user, result);
    }

    @Test
    public void findUserByLastNameTest() {
        User testUser = new User();
        testUser.setLastName("Pavl");

        Mockito.when(userRepository.findByLastName("Pavl")).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByLastName("Pavl");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(testUser, result.get());
    }

    @Test
    public void findUserByFirstNameTest() {
        User testUser = new User();
        testUser.setFirstName("Pavel");

        Mockito.when(userRepository.findByFirstName("John")).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByFirstName("John");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(testUser, result.get());
    }

    @Test
    public void createUserTest() throws Exception {
        userService.createUser(new User());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateUserTest() throws Exception {
        userService.updateUser(new User());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteUserTest() throws Exception {
        userService.deleteUserById(ID_VALUE);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(anyLong());
    }
}