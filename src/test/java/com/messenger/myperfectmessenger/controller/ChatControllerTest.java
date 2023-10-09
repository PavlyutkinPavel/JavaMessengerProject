package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.MessageType;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.ChatService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChatControllerTest {

    @MockBean
    ChatService chatService;

    @MockBean
    SecurityService securityService;
    @MockBean
    SecurityCredentialsRepository securityCredentialsRepository;
    static Chat chat;
    static List<Chat> chats;

    static Message message;
    static List<Message> messages;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        chat = new Chat();
        chat.setId(10L);
        chat.setChatName("Name");
        chat.setDescription("Description");
        chats = new ArrayList<>();
        chats.add(chat);

        message = new Message();
        message.setId(10L);
        message.setSender("Pasha");
        message.setContent("Hello");
        message.setChats(chat);
        message.setType(MessageType.CHAT);
        messages = new ArrayList<>();
        messages.add(message);
    }


    @Test
    public void getChatTest() throws Exception {
        Mockito.when(chatService.getChat(10L, null)).thenReturn(chat);

        mockMvc.perform(get("/chat/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatName").value("Name"));
    }

    @Test
    public void getChatsTestWillReturnCollectionOfChats() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(chatService.getChats()).thenReturn(chats);

        mockMvc.perform(get("/chat").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].chatName", Matchers.is("Name")))
                .andExpect(jsonPath("$[0].description", Matchers.is("Description")));;
    }

    @Test
    public void getChatListsTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(chatService.getChatList(Mockito.anyLong())).thenReturn(chats);

        mockMvc.perform(get("/chat/chat_list_of_user/admin").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].chatName", Matchers.is("Name")))
                .andExpect(jsonPath("$[0].description", Matchers.is("Description")));;
    }

    @Test
    public void getChatMessagesTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(chatService.getChatMessages(10L, mockPrincipal)).thenReturn(messages);

        mockMvc.perform(get("/chat/messages/10").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].content", Matchers.is("Hello")))
                .andExpect(jsonPath("$[0].sender", Matchers.is("Pasha")));;
    }

    @Test
    public void findMessageTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(chatService.findMessage(10L, 10L,mockPrincipal)).thenReturn(message);

        mockMvc.perform(get("/chat/10/find_message/10").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello"))
                .andExpect(jsonPath("$.sender").value("Pasha"));
    }


    @Test
    public void createChatTest() throws Exception {
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).createChat(any());

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateChatTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).updateChat(any());

        mockMvc.perform(put("/chat").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateChatNameTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).updateChatName(anyLong(), anyString());

        mockMvc.perform(put("/chat/update_name/10").principal(mockPrincipal).param("newChatName", "newChatName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateChatDescriptionTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).updateChatDescription(anyLong(), anyString());

        mockMvc.perform(put("/chat/update_description/10").principal(mockPrincipal).param("newChatDescription", "newChatDescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteChatTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).deleteChatById(anyLong());

        mockMvc.perform(delete("/chat/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void clearAllMessagesTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).clearAllMessages(anyLong());

        mockMvc.perform(delete("/chat/clear_messages/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMessageTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).deleteMessage(anyLong(), anyLong());

        mockMvc.perform(delete("/chat/10/delete_message/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserFromChatTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).deleteUser(anyLong(), anyLong());

        mockMvc.perform(delete("/chat/10/delete_user/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void leaveChatTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        ChatService mockUS = Mockito.mock(ChatService.class);
        Mockito.doNothing().when(mockUS).deleteUser(anyLong(), anyLong());

        mockMvc.perform(delete("/chat/10/leave_chat/admin").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isNoContent());
    }

}