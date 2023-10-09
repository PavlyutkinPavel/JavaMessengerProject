package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.UserFriendRelationRepository;
import com.messenger.myperfectmessenger.repository.UserProfileRepository;
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
public class UserProfileServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private SecurityCredentialsRepository securityCredentialsRepository;
    @Mock
    private UserFriendRelationRepository userFriendRelationRepository;

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
    public void getUserProfileTest() throws Exception {
        Long userId = 1L;
        String adminUsername = "admin";

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(userId);
        adminCredentials.setUserRole(Role.ADMIN);

        UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
        Mockito.when(userProfileRepository.findById(userId)).thenReturn(Optional.of(userProfile));

        UserProfile result = userProfileService.getUserProfile(userId, mockPrincipal);

        Assertions.assertEquals(userProfile, result);
    }

    @Test
    public void createUserProfileTest() throws Exception {
        userProfileService.createUserProfile(new UserProfile());
        Mockito.verify(userProfileRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateUserProfileTest() throws Exception {
        userProfileService.updateUserProfile(new UserProfile());
        Mockito.verify(userProfileRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteUserTest() throws Exception {
        userProfileService.deleteUserProfileById(ID_VALUE);
        Mockito.verify(userProfileRepository, Mockito.times(1)).deleteById(anyLong());
    }

//    @Test
//    public void testGetFriendUserProfile() {
//        UserProfile testUserProfile = new UserProfile();
//        testUserProfile.setId(1L);
//        testUserProfile.setUserId(2L);
//        Long userId = 1L;
//        String adminUsername = "admin";
//        SecurityCredentials adminCredentials = new SecurityCredentials();
//        adminCredentials.setUserId(userId);
//        adminCredentials.setUserRole(Role.ADMIN);
//
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn("admin");
//
//        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
//
//        Mockito.when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUserProfile));
//
//        Mockito.when(userFriendRelationRepository.findByUser_IdAndFriend_Id(2L, 1L)).thenReturn(Optional.empty());
//
//        Optional<UserProfile> result = userProfileService.getFriendUserProfile(1L, mockPrincipal);
//
//        Assertions.assertTrue(result.isPresent());
//        Assertions.assertEquals(testUserProfile, result.get());
//    }

    @Test
    public void testGetFriendUserProfileUserNotFound() {

        Long userId = 1L;
        String adminUsername = "admin";
        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(userId);
        adminCredentials.setUserRole(Role.ADMIN);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));

        Mockito.when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileService.getFriendUserProfile(1L, mockPrincipal));
    }
}