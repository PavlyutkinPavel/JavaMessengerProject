package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.FriendRequest;
import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.FriendsListService;
import com.messenger.myperfectmessenger.repository.FriendRequestRepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FriendsListController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FriendListControllerTest {

    @MockBean
    FriendsListService friendsListService;

    @MockBean
    FriendRequestRepository friendRequestRepository;

    @MockBean
    SecurityService securityService;
    static FriendsList friendsList;
    static List<FriendsList> friendsLists;

    static FriendRequest friendRequest;
    static List<FriendRequest> friendRequests;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        friendsList = new FriendsList();
        friendsList.setId(10L);
        friendsList.setFriendName("Pavel");
        friendsList.setIsClose(true);
        friendsLists = new ArrayList<>();
        friendsLists.add(friendsList);

        friendRequest = new FriendRequest();
        friendRequest.setId(10L);
        friendRequest.setFriend("Pavel");
        friendRequest.setSender("Dima");
        friendRequests = new ArrayList<>();
        friendRequests.add(friendRequest);
    }


    @Test
    public void getFriendListTest() throws Exception {
        Mockito.when(friendsListService.getFriendsList(10L, null)).thenReturn(friendsList);

        mockMvc.perform(get("/friend/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendName").value("Pavel"))
                .andExpect(jsonPath("$.isClose").value(true));
    }

    @Test
    public void getFriendListsTestWillReturnCollectionOfUsers() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(friendsListService.getFriendsLists()).thenReturn(friendsLists);

        mockMvc.perform(get("/friend").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].friendName").value("Pavel"))
                .andExpect(jsonPath("$[0].isClose").value(true));
    }

    @Test
    public void findFriendsByIsCloseTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(friendsListService.findFriendsByIsClose(anyBoolean())).thenReturn(friendsLists);

        mockMvc.perform(get("/friend/close").principal(mockPrincipal).param("isClose", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].friendName").value("Pavel"))
                .andExpect(jsonPath("$[0].isClose").value(true));
    }

    @Test
    public void findFriendsByFriendSinceTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        String friendSince = "2023-10-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(friendSince);

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(friendsListService.findFriendsByFriendSince(date, mockPrincipal)).thenReturn(friendsLists);

        mockMvc.perform(get("/friend/friends_since").principal(mockPrincipal).param("friendSince", "2023-10-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void getFriendRequestsTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(friendsListService.getFriendRequests(anyString())).thenReturn(friendRequests);

        mockMvc.perform(get("/friend/friend_requests/admin").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].friend").value("Pavel"))
                .andExpect(jsonPath("$[0].sender").value("Dima"));
    }

    @Test
    public void createFriendListTest() throws Exception {
        FriendsListService mockUS = Mockito.mock(FriendsListService.class);
        Mockito.doNothing().when(mockUS).createFriendsList(any());

        mockMvc.perform(post("/friend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendsList)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createFriendRequestTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");
        FriendsListService mockUS = Mockito.mock(FriendsListService.class);
        Mockito.doNothing().when(mockUS).createFriendRequest("admin", friendRequest, mockPrincipal);

        mockMvc.perform(post("/friend/send_request/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateFriendListTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        FriendsListService mockUS = Mockito.mock(FriendsListService.class);
        Mockito.doNothing().when(mockUS).updateFriendsList(any());

        mockMvc.perform(put("/friend").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendsList)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void acceptFriendRequestTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        FriendsListService mockUS = Mockito.mock(FriendsListService.class);
        Mockito.doNothing().when(mockUS).acceptFriendRequest(anyBoolean(), anyLong());

        mockMvc.perform(put("/friend/accept_request/10").principal(mockPrincipal).param("accepted", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteFriendListTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        FriendsListService mockUS = Mockito.mock(FriendsListService.class);
        Mockito.doNothing().when(mockUS).deleteFriendsList(friendsList);

        mockMvc.perform(delete("/friend/delete_friend").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendsList)))
                .andExpect(status().isNoContent());
    }

}