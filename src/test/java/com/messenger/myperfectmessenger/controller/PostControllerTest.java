package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.Post;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.PostService;
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
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @MockBean
    PostService postService;

    @MockBean
    SecurityService securityService;
    static Post post;
    static List<Post> posts;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        post = new Post();
        post.setId(10L);
        post.setTitle("Title");
        post.setLikes(10L);
        post.setDislikes(5L);
        post.setComments(20L);
        posts = new ArrayList<>();
        posts.add(post);
    }


    @Test
    public void getPostTest() throws Exception {
        Mockito.when(postService.getPost(10L)).thenReturn(Optional.ofNullable(post));

        mockMvc.perform(get("/post/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void getPostsTest() throws Exception {
        Mockito.when(postService.getPosts()).thenReturn(posts);

        mockMvc.perform(get("/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Title")));
    }

    @Test
    public void getPostStatisticsTest() throws Exception {

        Mockito.when(postService.getPost(anyLong())).thenReturn(Optional.of(post));

        mockMvc.perform(get("/post/statistics/10"))
                .andExpect(status().isOk());
    }

    @Test
    public void createPostTest() throws Exception {
        PostService mockUS = Mockito.mock(PostService.class);
        Mockito.doNothing().when(mockUS).createPost(any());

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePostTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        PostService mockUS = Mockito.mock(PostService.class);
        Mockito.doNothing().when(mockUS).updatePost(any());

        mockMvc.perform(put("/post").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void putLikeTest() throws Exception {
        PostService mockUS = Mockito.mock(PostService.class);
        Mockito.doNothing().when(mockUS).putLike(any());

        mockMvc.perform(put("/post/like/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNoContent());
    }
    @Test
    public void putDisLikeTest() throws Exception {
        PostService mockUS = Mockito.mock(PostService.class);
        Mockito.doNothing().when(mockUS).putDislike(any());

        mockMvc.perform(put("/post/dislike/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePostTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        PostService mockUS = Mockito.mock(PostService.class);
        Mockito.doNothing().when(mockUS).deletePostById(anyLong());

        mockMvc.perform(delete("/post/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNoContent());
    }

}