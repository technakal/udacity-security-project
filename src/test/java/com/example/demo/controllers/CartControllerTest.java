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
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

  private CartController testCartController;
  private CartRepository testCartRepository = mock(CartRepository.class);
  private UserRepository testUserRepository = mock(UserRepository.class);
  private ItemRepository testItemRepository = mock(ItemRepository.class);
  private Item testItem;
  private Cart testCart;
  private ApplicationUser testUser;

  @Before
  public void setup() {
    testCartController = new CartController();
    // create item
    testItem = new Item();
    testItem.setId(0L);
    testItem.setName("Test Item");
    testItem.setDescription("A really generic test item.");
    testItem.setPrice(new BigDecimal(2.99));

    // create dummy cart
    testCart = new Cart();
    testCart.setId(0L);
    testCart.setUser(new ApplicationUser("john_smith", "password"));
    testCart.setItems(Arrays.asList(testItem));
    testCart.setTotal(new BigDecimal(2.99));
    TestUtils.injectObject(testCartController, "cartRepository", testCartRepository);
    TestUtils.injectObject(testCartController, "userRepository", testUserRepository);
    TestUtils.injectObject(testCartController, "itemRepository", testItemRepository);
  }

  @Test
  public void add_to_cart_happy_path() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(testItem.getId());
    request.setQuantity(2);
    when(testCartRepository.findByUser(testUser)).thenReturn(testCart);
    final ResponseEntity<Cart> response = testCartController.addToCart(request);
    assertNotNull(response);
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
    request.setItemId(99);
    request.setQuantity(2);
    final ResponseEntity<Cart> response = testCartController.addToCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void remove_from_cart_happy_path() throws Exception {
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("john_smith");
    request.setItemId(testItem.getId());
    request.setQuantity(2);
    when(testCartRepository.findByUser(testUser)).thenReturn(testCart);
    final ResponseEntity<Cart> response = testCartController.removeFromCart(request);
    assertNotNull(response);
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
    request.setItemId(99);
    request.setQuantity(2);
    final ResponseEntity<Cart> response = testCartController.removeFromCart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

}
