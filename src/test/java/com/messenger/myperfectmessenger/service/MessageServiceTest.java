package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.repository.MessageRepository;
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
public class MessageServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

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
        Long messageId = 1L;
        String adminUsername = "admin";

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(messageId);
        adminCredentials.setUserRole(Role.ADMIN);

        Message message = new Message();
        message.setId(messageId);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
        Mockito.when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        Message result = messageService.getMessage(messageId, mockPrincipal);

        Assertions.assertEquals(message, result);
    }

    @Test
    public void createUserTest() throws Exception {
        messageService.createMessage(new Message());
        Mockito.verify(messageRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateUserTest() throws Exception {
        messageService.updateMessage(new Message());
        Mockito.verify(messageRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteUserTest() throws Exception {
        messageService.deleteMessageById(ID_VALUE);
        Mockito.verify(messageRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteAllMessagesTest() {
        Mockito.doNothing().when(messageRepository).deleteAll();
        messageService.deleteAllMessages();
        Mockito.verify(messageRepository, Mockito.times(1)).deleteAll();
    }
}