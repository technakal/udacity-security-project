package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.util.LogHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartControllerNegativeTest {

  private static final Logger log = LoggerFactory.getLogger(CartController.class);

  private CartController testCartController;
  private CartRepository testCartRepository = mock(CartRepository.class);

  @Autowired
  private UserRepository testUserRepository;

  @Autowired
  private ItemRepository testItemRepository;

  private Item testItem;
  private Cart testCart;
  private ApplicationUser testUser;

  @Before
  public void setup() {
    testCartController = new CartController();

    // create test user
    testUser = new ApplicationUser();
    testUser.setId(0L);
    testUser.setUsername("john_smith");
    testUser.setPassword("password123");
    testUserRepository.save(testUser);

    // create item
    testItem = new Item();
    testItem.setId(0L);
    testItem.setName("Test Item");
    testItem.setDescription("A really generic test item.");
    testItem.setPrice(new BigDecimal(2.99));
    testItemRepository.save(testItem);

    testCart = new Cart();
    testCart.addItem(testItem);
    testCart.setUser(testUser);
    log.debug(LogHelper.buildLogString(testCart.toString()));

    TestUtils.injectObject(testCartController, "cartRepository", testCartRepository);
    TestUtils.injectObject(testCartController, "userRepository", testUserRepository);
    TestUtils.injectObject(testCartController, "itemRepository", testItemRepository);
  }

  @Test
  public void add_to_cart_no_user() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    final ResponseEntity<Cart> response = testCartController.addToCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void add_to_cart_item_not_found() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(99L);
    request.setQuantity(2);
    final ResponseEntity<Cart> response = testCartController.addToCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void remove_from_cart_no_user() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    final ResponseEntity<Cart> response = testCartController.removeFromCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void remove_from_cart_item_not_found() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(99L);
    request.setQuantity(2);
    final ResponseEntity<Cart> response = testCartController.removeFromCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

}
