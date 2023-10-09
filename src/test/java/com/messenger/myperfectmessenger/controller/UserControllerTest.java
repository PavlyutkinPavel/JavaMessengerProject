package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.UserService;
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
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    SecurityService securityService;
    static User user;
    static List<User> users;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @Before("")
//    public void init(){
//        MockitoAnnotations.initMocks(this);
//    }

    @BeforeAll
    public static void beforeAll() {
        user = new User();
        user.setId(10L);
        user.setFirstName("Pavel");
        user.setLastName("Pavl");
        users = new ArrayList<>();
        users.add(user);
    }


    @Test
    public void getUserTest() throws Exception {
        User user1 = new User();
        user1.setId(10L);
        user1.setFirstName("Dima");
        user1.setLastName("Gorohov");
        Mockito.when(userService.getUser(10L, null)).thenReturn(user1);

        mockMvc.perform(get("/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Dima"));
    }

    @Test
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUsersTestWillReturnCollectionOfUsers() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(userService.getUsers(mockPrincipal)).thenReturn(users);

        mockMvc.perform(get("/user").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", Matchers.is("Pavel")))
                .andExpect(jsonPath("$[0].lastName", Matchers.is("Pavl")));
    }

    @Test
    public void createUserTest() throws Exception {
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).createUser(any());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUserTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).updateUser(any());

        mockMvc.perform(put("/user").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).deleteUserById(anyLong());

        mockMvc.perform(delete("/user/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

}