package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.Post;
import com.messenger.myperfectmessenger.repository.PostRepository;
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
public class PostServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

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
    public void getPostTest() throws Exception {
        Long postId = 1L;

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(postId);
        adminCredentials.setUserRole(Role.ADMIN);

        Post post = new Post();
        Post result = null;
        post.setId(postId);

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Optional<Post> resultOpt = postService.getPost(postId);
        if(resultOpt.isPresent()){
             result = resultOpt.get();
        }

        Assertions.assertEquals(post, result);
    }

    @Test
    public void createPostTest() throws Exception {
        postService.createPost(new Post());
        Mockito.verify(postRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updatePostTest() throws Exception {
        postService.updatePost(new Post());
        Mockito.verify(postRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteUserTest() throws Exception {
        postService.deletePostById(ID_VALUE);
        Mockito.verify(postRepository, Mockito.times(1)).deleteById(anyLong());
    }

}
