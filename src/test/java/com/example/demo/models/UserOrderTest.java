package com.example.demo.models;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserOrderTest {

  public UserOrder testOrder;
  public ApplicationUser testUser;
  public Item roundTestItem;
  public Item squareTestItem;

  @Before
  public void setup() {
    testUser = new ApplicationUser("john_smith", "password123");
    testOrder = new UserOrder();
    testOrder.setId(99L);
    testOrder.setUser(testUser);
    testOrder.setTotal(new BigDecimal(6.98));

    // create items
    roundTestItem = new Item();
    roundTestItem.setId(0L);
    roundTestItem.setName("Round Test Item");
    roundTestItem.setDescription("A round test item.");
    roundTestItem.setPrice(new BigDecimal(2.99));
    squareTestItem = new Item();
    squareTestItem.setId(1L);
    squareTestItem.setName("Square Test Item");
    squareTestItem.setDescription("A square test item.");
    squareTestItem.setPrice(new BigDecimal(3.99));
    List<Item> testItems = Arrays.asList(roundTestItem, squareTestItem);

    testOrder.setItems(testItems);
  }

  @Test
  public void get_user_order_id_happy_path() throws Exception {
    long id = testOrder.getId();
    assertNotNull(id);
    assertEquals(99L, id);
  }

  @Test
  public void get_user_order_items_happy_path() throws Exception {
    List<Item> items = testOrder.getItems();
    assertNotNull(items);
    assertEquals(2, items.size());
  }

  @Test
  public void get_user_order_total_happy_path() throws Exception {
    BigDecimal total = testOrder.getTotal();
    assertNotNull(total);
    assertEquals(new BigDecimal(6.98), total);
  }

  @Test
  public void create_from_cart_happy_path() throws Exception {
    Cart testCart = new Cart();
    testCart.setId(0L);
    testCart.setUser(testUser);
    testCart.addItem(roundTestItem);
    testCart.setTotal(roundTestItem.getPrice());
    UserOrder testOrder = new UserOrder();
    testOrder.createFromCart(testCart);
    assertNotNull(testOrder);
  }
}
