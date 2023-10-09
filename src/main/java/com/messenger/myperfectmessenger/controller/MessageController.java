package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.exception.MessageNotFoundException;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController //для REST архитектуры
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final SecurityService securityService;

    private final Path ROOT_FILE_PATH = Paths.get("src/main/resources/static/file_messages");

    public MessageController(MessageService messageService, SecurityService securityService) {
        this.messageService = messageService;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            List<Message> messages = messageService.getMessages();
            if (messages.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(messages, HttpStatus.OK);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id, Principal principal) {
        Message message = messageService.getMessage(id, principal);
        if(message != null){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createMessage(@RequestBody Message message) {
        messageService.createMessage(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/file")
    public ResponseEntity<HttpStatus> createFileMessage(@RequestParam("file") MultipartFile file) {
        try{
            Files.copy(file.getInputStream(), this.ROOT_FILE_PATH.resolve(file.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateMessage(@RequestBody Message message, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (Objects.equals(message.getSender(), principal.getName()))){
            messageService.updateMessage(message);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable Long id, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (Objects.equals(messageService.getMessage(id, principal).getSender(), principal.getName()))){
            messageService.deleteMessageById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //very strong!
    @DeleteMapping("/delete_all_messages")
    public ResponseEntity<HttpStatus> deleteAllMessages(Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            messageService.deleteAllMessages();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(
            @Payload Message chatMessage
    ) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(
            @Payload Message chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
