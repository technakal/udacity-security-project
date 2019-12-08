package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.h2.command.ddl.CreateUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

  private UserController testUserController;
  private UserRepository testUserRepository = mock(UserRepository.class);
  private CartRepository testCartRepository = mock(CartRepository.class);
  private BCryptPasswordEncoder testBCryptEncoder = mock(BCryptPasswordEncoder.class);


  @Before
  public void setup() {
    testUserController = new UserController();
    TestUtils.injectObject(testUserController, "userRepository", testUserRepository);
    TestUtils.injectObject(testUserController, "cartRepository", testCartRepository);
    TestUtils.injectObject(testUserController, "bCryptPasswordEncoder", testBCryptEncoder);
  }

  @Test
  public void create_user_happy_path() throws Exception {
    when(testBCryptEncoder.encode("test_password")).thenReturn("hashedPassword");
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test_user");
    userRequest.setPassword("test_password");
    userRequest.setConfirmPassword("test_password");
    final ResponseEntity<ApplicationUser> response = testUserController.createUser(userRequest);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    ApplicationUser user = response.getBody();
    assertNotNull(user);
    assertEquals(0, user.getId());
    assertEquals("test_user", user.getUsername());
    assertEquals("hashedPassword", user.getPassword());
  }

  @Test
  public void create_user_no_password() {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test_user");
    final ResponseEntity<ApplicationUser> response = testUserController.createUser(userRequest);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Bad request: create user requires username, password, and confirm password.", response.getBody());
  }

  @Test
  public void create_user_bad_password() {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test_user");
    userRequest.setPassword("passwo");
    userRequest.setConfirmPassword("passwo");
    final ResponseEntity<ApplicationUser> response = testUserController.createUser(userRequest);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Bad request: user password is not long enough.", response.getBody());
  }

  @Test
  public void create_user_mismatch_password() {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test_user");
    userRequest.setPassword("test_password");
    userRequest.setConfirmPassword("cool_password");
    final ResponseEntity<ApplicationUser> response = testUserController.createUser(userRequest);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Bad request: password and confirm password do not match.", response.getBody());
  }


  // todo: test getUserById happy path
  @Test
  public void get_user_by_id_happy_path() {

  }

  // todo: test getUserById not found

  // todo: test getUserByUsername happy path

  // todo: test getUserByUsername not found

}
