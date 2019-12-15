package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartControllerTest {

  private static final Logger log = LoggerFactory.getLogger(CartController.class);

  private CartController testCartController;
  private CartRepository testCartRepository = mock(CartRepository.class);
  private ItemRepository testItemRepository = mock(ItemRepository.class);
  private UserRepository testUserRepository = mock(UserRepository.class);
  private Item testItem;
  private Cart testCart;

  @Before
  public void setup() {
    testCartController = new CartController();

    // create item
    testItem = new Item();
    testItem.setId(0L);
    testItem.setName("Test Item");
    testItem.setDescription("A really generic test item.");
    testItem.setPrice(new BigDecimal("2.99"));

    testCart = new Cart();
    testCart.setItems(Collections.singletonList(testItem));

    TestUtils.injectObject(testCartController, "cartRepository", testCartRepository);
    TestUtils.injectObject(testCartController, "userRepository", testUserRepository);
    TestUtils.injectObject(testCartController, "itemRepository", testItemRepository);
  }

  @Test
  public void add_to_cart_happy_path() throws Exception {
    log.debug("Running test: add_to_cart_happy_path");
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(0L);
    request.setQuantity(2);
    log.debug("Stubbing user.");
    when(testUserRepository.findByUsername("john_smith")).thenReturn(new ApplicationUser("john_smith", "password123", testCart));
    final ResponseEntity<Cart> response = testCartController.addToCart(request);
    assertNotNull(response);
  }

  @Test
  public void remove_from_cart_happy_path() throws Exception {
    log.debug("Running test: remove_from_cart_happy_path");
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(testItem.getId());
    request.setQuantity(2);
    final ResponseEntity<Cart> response = testCartController.removeFromCart(request);
    assertNotNull(response);
  }

}
