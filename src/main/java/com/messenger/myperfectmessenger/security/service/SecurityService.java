package com.messenger.myperfectmessenger.security.service;


import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.UserRepository;
import com.messenger.myperfectmessenger.security.domain.RegistrationDTO;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import com.messenger.myperfectmessenger.security.JwtUtils;
import com.messenger.myperfectmessenger.security.domain.AuthRequest;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class SecurityService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final JwtUtils jwtUtils;

    private final User user;

    private final Collection<Chat> chats;

    private final Collection<FriendsList> friendsLists;
    private final SecurityCredentials securityCredentials;

    private final UserRepository userRepository;

    public SecurityService(PasswordEncoder passwordEncoder, SecurityCredentialsRepository securityCredentialsRepository, JwtUtils jwtUtils, User user, Collection<Chat> chats, Collection<FriendsList> friendsLists, SecurityCredentials securityCredentials, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.securityCredentialsRepository = securityCredentialsRepository;
        this.jwtUtils = jwtUtils;
        this.user = user;
        this.chats = chats;
        this.friendsLists = friendsLists;
        this.securityCredentials = securityCredentials;
        this.userRepository = userRepository;
    }

    public String generateToken(AuthRequest authRequest){
        //1. get User by login
        //2. check passwords
        //3. generate token by login
        //4. if all is bad then return empty string ""
        Optional<SecurityCredentials> credentials = securityCredentialsRepository.findByUserLogin(authRequest.getLogin());
        if (credentials.isPresent() && passwordEncoder.matches(authRequest.getPassword(),credentials.get().getUserPassword())){
            return jwtUtils.generateJwtToken(authRequest.getLogin());
        }
        return "";
    }

    //откатываемся при любых ошибках и не сохраняем ничего
    @Transactional(rollbackFor = Exception.class) //либо над классом(все методы будут помечены тогда), либо над методом
    public void registration(RegistrationDTO registrationDTO){
        //Проверить если в бд
        //1. parse DTO
        //2. create UserInfo+SecurityCredentials
        //3. make transaction and execution
        //Optional<SecurityCredentials> securityCredentials1 = securityCredentialsRepository.findByUserLogin(registrationDTO.getUserLogin())

        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setCreatedAt(LocalDateTime.now());
        User userResult = userRepository.save(user);

        securityCredentials.setUserLogin(registrationDTO.getUserLogin());
        securityCredentials.setUserPassword(passwordEncoder.encode(registrationDTO.getUserPassword()));
        securityCredentials.setUserRole(Role.USER);
        securityCredentials.setUserId(userResult.getId());
        securityCredentialsRepository.save(securityCredentials);
    }

    public Boolean checkIfAdmin(String login){
        Optional<SecurityCredentials> credentials = securityCredentialsRepository.findByUserLogin(login);
        if (credentials.isPresent() && credentials.get().getUserRole().toString() == "ADMIN"){
            return true;
        }else{
            return false;
        }
    }

    public String getUserByLogin(String login){
        SecurityCredentials credentials = securityCredentialsRepository.findByUserLogin(login).orElseThrow(UserNotFoundException::new);
        if (credentials != null){
            return credentials.getUserLogin();
        }else{
            return "";
        }
    }

    public Long getUserIdByLogin(String login){
        String username  = getUserByLogin(login);
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(username).orElseThrow(UserNotFoundException::new);
        if (credentials != null){
            return credentials.getUserId();
        }else{
            return 0L;
        }
    }
}
