package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.MessageType;
import com.messenger.myperfectmessenger.security.controller.SecurityController;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.MessageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MessageControllerTest {

    @MockBean
    MessageService messageService;

    @MockBean
    SecurityService securityService;

    @MockBean
    SecurityController securityController;
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
        message = new Message();
        message.setId(10L);
        message.setSender("Pasha");
        message.setContent("Hello");
        message.setType(MessageType.CHAT);
        messages = new ArrayList<>();
        messages.add(message);
    }


    @Test
    public void getMessageTest() throws Exception {
        Mockito.when(messageService.getMessage(10L, null)).thenReturn(message);

        mockMvc.perform(get("/message/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello"))
                .andExpect(jsonPath("$.sender").value("Pasha"));
    }

    @Test
    public void getMessagesTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(messageService.getMessages()).thenReturn(messages);

        mockMvc.perform(get("/message").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].content", Matchers.is("Hello")))
                .andExpect(jsonPath("$[0].sender", Matchers.is("Pasha")));;
    }

    @Test
    public void createMessageTest() throws Exception {
        MessageService mockUS = Mockito.mock(MessageService.class);
        Mockito.doNothing().when(mockUS).createMessage(any());

        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateMessageTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        MessageService mockUS = Mockito.mock(MessageService.class);
        Mockito.doNothing().when(mockUS).updateMessage(any());

        mockMvc.perform(put("/message").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMessageTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        MessageService mockUS = Mockito.mock(MessageService.class);
        Mockito.doNothing().when(mockUS).deleteMessageById(anyLong());

        mockMvc.perform(delete("/message/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAllMessagesTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        MessageService mockUS = Mockito.mock(MessageService.class);
        Mockito.doNothing().when(mockUS).deleteAllMessages();

        mockMvc.perform(delete("/message/delete_all_messages").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isNoContent());
    }

}