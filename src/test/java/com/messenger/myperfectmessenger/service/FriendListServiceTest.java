package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.domain.FriendRequest;
import com.messenger.myperfectmessenger.repository.FriendsListRepository;
import com.messenger.myperfectmessenger.repository.FriendRequestRepository;
import com.messenger.myperfectmessenger.repository.UserFriendRelationRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FriendListServiceTest {

    private final Long ID_VALUE = 28L;
    @InjectMocks
    private FriendsListService friendsListService;

    @Mock
    private FriendsListRepository friendsListRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserFriendRelationRepository userFriendRelationRepository;

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
    public void getFriendTest() throws Exception {
        Long friendId = 1L;
        String adminUsername = "admin";

        SecurityCredentials adminCredentials = new SecurityCredentials();
        adminCredentials.setUserId(friendId);
        adminCredentials.setUserRole(Role.ADMIN);

        FriendsList friendsList = new FriendsList();
        friendsList.setId(friendId);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);

        Mockito.when(securityCredentialsRepository.findUserIdByLogin(adminUsername)).thenReturn(Optional.of(adminCredentials));
        Mockito.when(friendsListRepository.findById(friendId)).thenReturn(Optional.of(friendsList));

        FriendsList result = friendsListService.getFriendsList(friendId, mockPrincipal);

        Assertions.assertEquals(friendsList, result);
    }

    @Test
    public void getFriendsListsTest() {
        List<FriendsList> friendsLists = new ArrayList<>();
        Mockito.when(friendsListRepository.findAll()).thenReturn(friendsLists);
        List<FriendsList> result = friendsListService.getFriendsLists();
        Mockito.verify(friendsListRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(friendsLists, result);
    }

    @Test
    public void createFriendTest() throws Exception {
        friendsListService.createFriendsList(new FriendsList());
        Mockito.verify(friendsListRepository, Mockito.times(1)).save(any());
    }
    @Test
    public void createFriendRequestTest() throws Exception {
        String adminUsername = "admin";
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(adminUsername);
        friendsListService.createFriendRequest("Name", new FriendRequest(), mockPrincipal);
        Mockito.verify(friendRequestRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateFriendTest() throws Exception {
        friendsListService.updateFriendsList(new FriendsList());
        Mockito.verify(friendsListRepository, Mockito.times(1)).updateFriendsList(any());
    }

    @Test
    public void deleteUserTest() throws Exception {
        friendsListService.deleteFriendsList(new FriendsList());
        Mockito.verify(friendsListRepository, Mockito.times(1)).deleteFriendsList(any());
    }

    @Test
    public void testAcceptFriendRequestAccepted() {
        // Создаем объект FriendRequest для возвращения заглушкой
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(1L);

        // Мокируем вызов метода findById() у репозитория
        Mockito.when(friendRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(friendRequest));

        // Вызываем метод acceptFriendRequest с accepted = true
        friendsListService.acceptFriendRequest(true, 1L);

        // Проверяем, что метод findById() у репозитория был вызван один раз
        Mockito.verify(friendRequestRepository, Mockito.times(1)).findById(1L);
        // Проверяем, что у объекта FriendRequest установлен флаг isAccepted в true
        Assertions.assertTrue(friendRequest.getIsAccepted());
    }

    @Test
    public void acceptFriendRequestRejectedTest() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(1L);

        Mockito.when(friendRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(friendRequest));

        friendsListService.acceptFriendRequest(false, 1L);

        Mockito.verify(friendRequestRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(friendRequestRepository, Mockito.times(1)).delete(friendRequest);
    }
}