package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

  private OrderController testOrderController;
  private OrderRepository testOrderRepository = mock(OrderRepository.class);
  private UserRepository testUserRepository = mock(UserRepository.class);
  private ApplicationUser testUser;
  private Cart testCart;
  private Item testItem;
  private UserOrder testOrder;

  @Before
  public void setup() {
    // set up our controller and mocks
    testOrderController = new OrderController();
    TestUtils.injectObject(testOrderController, "orderRepository", testOrderRepository);
    TestUtils.injectObject(testOrderController, "userRepository", testUserRepository);

    // create some reusable dummy data
    testUser = new ApplicationUser("john_smith", "password");
    testCart = new Cart();
    testItem = new Item();
    testItem.setId(0L);
    testItem.setName("Test Item");
    testItem.setDescription("A really generic test item.");
    testItem.setPrice(new BigDecimal(2.99));
    testCart.addItem(testItem);
    TestUtils.injectObject(testUser, "cart", testCart);

    // create test order dummy data
    testOrder = new UserOrder();
    testOrder.setId(0L);
    testOrder.setUser(testUser);
    testOrder.setItems(Arrays.asList(testItem, testItem, testItem));
    testOrder.setTotal(new BigDecimal(8.97));
  }

  @Test
  public void submit_order_happy_path() throws Exception {
    when(testUserRepository.findByUsername("john_smith")).thenReturn(testUser);
    final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, testCart.getItems().size());
    assertEquals(new BigDecimal(2.99), testCart.getTotal());
  }

  @Test
  public void submit_order_no_user() throws Exception {
    final ResponseEntity<UserOrder> response = testOrderController.submit(null);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void submit_order_no_items() throws Exception {
    when(testUserRepository.findByUsername("john_smith")).thenReturn(testUser);
    testUser.getCart().removeItem(testItem);
    final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Cart is empty!", response.getBody());
    assertEquals(0, testCart.getItems().size());
  }

  @Test
  public void get_history_happy_path() throws Exception {
    when(testUserRepository.findByUsername("john_smith")).thenReturn(null);
    final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(testUser.getUsername());
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void get_history_no_user() throws Exception {
    final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(null);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

}
