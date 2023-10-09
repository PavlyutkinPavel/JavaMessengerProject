package com.messenger.myperfectmessenger;

import com.messenger.myperfectmessenger.controller.ChatController;
import com.messenger.myperfectmessenger.controller.UserController;
import com.messenger.myperfectmessenger.controller.UserProfileController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MyPerfectMessengerApplicationTests {

	@Autowired
	public UserController userController;

	@Autowired
	public UserProfileController userProfileController;
	@Autowired
	public ChatController chatController;

	@Test
	void contextLoads() {
		Assertions.assertThat(userController).isNotNull();
		Assertions.assertThat(userProfileController).isNotNull();
		Assertions.assertThat(chatController).isNotNull();
	}

}
