package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.repository.ChatRepository;
import com.messenger.myperfectmessenger.repository.UserChatRelationRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserChatRelationRepository userChatRelationRepository;

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
    public void getChatTest() throws Exception {
        Long chatId = 1L;
        String adminUsername = "admin";

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(1L);
        adminCredentials.setUserRole(Role.ADMIN);

        Chat chat = new Chat();
        chat.setId(chatId);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        Chat result = chatService.getChat(chatId, mockPrincipal);

        Assertions.assertEquals(chat, result);
    }

    @Test
    public void createChatTest() throws Exception {
        chatService.createChat(new Chat());
        Mockito.verify(chatRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateChatTest() throws Exception {
        chatService.updateChat(new Chat());
        Mockito.verify(chatRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteChatTest() throws Exception {
        chatService.deleteChatById(ID_VALUE);
        Mockito.verify(chatRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    public void getChatsTest() {
        Chat chat1 = new Chat();
        chat1.setId(1L);
        Chat chat2 = new Chat();
        chat2.setId(2L);

        List<Chat> chatList = new ArrayList<>();
        chatList.add(chat1);
        chatList.add(chat2);

        Mockito.when(chatRepository.findAll(Sort.by("id"))).thenReturn(chatList);

        List<Chat> result = chatService.getChats();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(chat1, result.get(0));
        Assertions.assertEquals(chat2, result.get(1));
    }

    @Test
    public void getChatsEmptyListTest() {
        Mockito.when(chatRepository.findAll(Sort.by("id"))).thenReturn(Collections.emptyList());

        List<Chat> result = chatService.getChats();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void сlearAllMessagesTest() {
        Long chatId = 1L;
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setMessages(new ArrayList<>());

        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        chatService.clearAllMessages(chatId);

        Mockito.verify(chatRepository, Mockito.times(1)).findById(chatId);
        Mockito.verify(chatRepository, Mockito.times(1)).save(chat);
        Assertions.assertTrue(chat.getMessages().isEmpty());
    }

    @Test
    public void deleteMessageTest() {
        Long chatId = 1L;
        Long messageId = 2L;
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setMessages(new ArrayList<>());

        Message message = new Message();
        message.setId(messageId);
        chat.getMessages().add(message);

        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        chatService.deleteMessage(chatId, messageId);

        Mockito.verify(chatRepository, Mockito.times(1)).findById(chatId);
        Mockito.verify(chatRepository, Mockito.times(1)).save(chat);
        Assertions.assertTrue(chat.getMessages().isEmpty());
    }


    @Test
    public void deleteUserTest() {
        Long userId = 1L;
        Long chatId = 2L;

        chatService.deleteUser(userId, chatId);

        Mockito.verify(userChatRelationRepository, Mockito.times(1)).removeUserByIdAndChatId(userId, chatId);
    }

    @Test
    public void findMessageTest() {
        Long chatId = 1L;
        Long messageId = 2L;
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");

        SecurityCredentials credentials = new SecurityCredentials();
        credentials.setUserId(1L);
        credentials.setUserRole(Role.ADMIN);

        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setMessages(new ArrayList<>());

        Message message = new Message();
        message.setId(messageId);
        chat.getMessages().add(message);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(principal.getName())).thenReturn(Optional.of(credentials));
        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        Message result = chatService.findMessage(chatId, messageId, principal);

        Assertions.assertEquals(message, result);
    }

    @Test
    public void getChatMessagesTest() {
        Long chatId = 1L;
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");

        SecurityCredentials credentials = new SecurityCredentials();
        credentials.setUserId(1L);
        credentials.setUserRole(Role.ADMIN);

        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setMessages(new ArrayList<>());

        Message message1 = new Message();
        message1.setId(1L);
        Message message2 = new Message();
        message2.setId(2L);
        chat.getMessages().add(message1);
        chat.getMessages().add(message2);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(principal.getName())).thenReturn(Optional.of(credentials));
        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        List<Message> result = chatService.getChatMessages(chatId, principal);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(message1, result.get(0));
        Assertions.assertEquals(message2, result.get(1));
    }

    @Test
    public void updateChatNameTest() {
        Long chatId = 1L;
        String newChatName = "New Chat Name";

        Chat chat = new Chat();
        chat.setId(chatId);

        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        chatService.updateChatName(chatId, newChatName);

        Mockito.verify(chatRepository, Mockito.times(1)).findById(chatId);
        Mockito.verify(chatRepository, Mockito.times(1)).saveAndFlush(chat);

        Assertions.assertEquals(newChatName, chat.getChatName());
    }

    @Test
    public void updateChatDescriptionTest() {
        Long chatId = 1L;
        String newChatDescription = "New Chat Description";

        Chat chat = new Chat();
        chat.setId(chatId);

        Mockito.when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        chatService.updateChatDescription(chatId, newChatDescription);

        Mockito.verify(chatRepository, Mockito.times(1)).findById(chatId);
        Mockito.verify(chatRepository, Mockito.times(1)).saveAndFlush(chat);

        Assertions.assertEquals(newChatDescription, chat.getDescription());
    }





}