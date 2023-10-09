package com.messenger.myperfectmessenger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.security.filter.JwtAuthenticationFilter;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.UserProfileService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserProfileControllerTest {

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    SecurityService securityService;
    static UserProfile userProfile;
    static List<UserProfile> usersProfiles;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeAll
    public static void beforeAll() {
        userProfile = new UserProfile();
        userProfile.setId(10L);
        userProfile.setProfileDescription("MyProfile");
        usersProfiles = new ArrayList<>();
        usersProfiles.add(userProfile);
    }


    @Test
    public void getUserProfileTest() throws Exception {

        Mockito.when(userProfileService.getUserProfile(10L, null)).thenReturn(userProfile);

        mockMvc.perform(get("/user_profile/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileDescription").value("MyProfile"));
    }

    @Test
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUserProfilesTestWillReturnCollectionOfUsers() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        Mockito.when(userProfileService.getUserProfiles()).thenReturn(usersProfiles);

        mockMvc.perform(get("/user_profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].profileDescription", Matchers.is("MyProfile")));
    }

    @Test
    public void getFriendUserProfileTest() throws Exception{
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        UserProfileService mockUS = Mockito.mock(UserProfileService.class);
        Mockito.when(mockUS.getFriendUserProfile(10L, mockPrincipal)).thenReturn(Optional.ofNullable(userProfile));

        mockMvc.perform(get("/user_profile/friend/10").principal(mockPrincipal))
                .andExpect(status().isNotFound());
//                .andExpect(jsonPath("$.profileDescription").value("MyProfile"));
    }

    @Test
    public void createUserProfileTest() throws Exception {
        UserProfileService mockUS = Mockito.mock(UserProfileService.class);
        Mockito.doNothing().when(mockUS).createUserProfile(any());

        mockMvc.perform(post("/user_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfile)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUserProfileTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        UserProfileService mockUS = Mockito.mock(UserProfileService.class);
        Mockito.doNothing().when(mockUS).updateUserProfile(any());

        mockMvc.perform(put("/user_profile").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfile)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserProfileTest() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityService.checkIfAdmin(mockPrincipal.getName())).thenReturn(true);
        UserProfileService mockUS = Mockito.mock(UserProfileService.class);
        Mockito.doNothing().when(mockUS).deleteUserProfileById(anyLong());

        mockMvc.perform(delete("/user_profile/10").principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfile)))
                .andExpect(status().isNoContent());
    }

}