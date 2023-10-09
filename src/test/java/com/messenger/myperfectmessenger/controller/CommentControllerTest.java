package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.Comment;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.CommentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @MockBean
    CommentService commentService;

    @MockBean
    SecurityService securityService;
    static Comment comment;
    static List<Comment> comments;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        comment = new Comment();
        comment.setId(10L);
        comment.setContent("Content");
        comment.setLikes(10L);
        comment.setDislikes(5L);
        comments = new ArrayList<>();
        comments.add(comment);
    }


    @Test
    public void getPostTest() throws Exception {
        Mockito.when(commentService.getComment(10L)).thenReturn(Optional.ofNullable(comment));

        mockMvc.perform(get("/comment/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Content"));
    }

    @Test
    public void getPostsTest() throws Exception {
        Mockito.when(commentService.getComments()).thenReturn(comments);

        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].content").value("Content"));
    }

    @Test
    public void getPostStatisticsTest() throws Exception {

        Mockito.when(commentService.getComment(anyLong())).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/comment/statistics/10"))
                .andExpect(status().isOk());
    }

    @Test
    public void createPostTest() throws Exception {
        CommentService mockUS = Mockito.mock(CommentService.class);
        Mockito.doNothing().when(mockUS).createComment(any());

        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePostTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        CommentService mockUS = Mockito.mock(CommentService.class);
        Mockito.doNothing().when(mockUS).updateComment(any());

        mockMvc.perform(put("/comment/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void putLikeTest() throws Exception {
        CommentService mockUS = Mockito.mock(CommentService.class);
        Mockito.doNothing().when(mockUS).putLike(any());

        mockMvc.perform(put("/comment/like/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isNoContent());
    }
    @Test
    public void putDisLikeTest() throws Exception {
        CommentService mockUS = Mockito.mock(CommentService.class);
        Mockito.doNothing().when(mockUS).putDislike(any());

        mockMvc.perform(put("/comment/dislike/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePostTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        CommentService mockUS = Mockito.mock(CommentService.class);
        Mockito.doNothing().when(mockUS).deleteCommentById(anyLong());

        mockMvc.perform(delete("/comment/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isNoContent());
    }

}