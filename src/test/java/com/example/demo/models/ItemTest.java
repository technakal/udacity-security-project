package com.example.demo.models;

import com.example.demo.model.persistence.Item;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemTest {

  private Item testItem;

  @Before
  public void setup() {
    testItem = new Item();
    testItem.setId(99L);
    testItem.setName("Test Item");
    testItem.setDescription("A fancy test item, for testing.");
    testItem.setPrice(new BigDecimal(2.99));
  }

  @Test
  public void get_id_happy_path() throws Exception {
    long id = testItem.getId();
    assertNotNull(id);
    assertEquals(99L, id);
  }

  @Test
  public void set_id_happy_path() throws Exception {
    long id = 74L;
    testItem.setId(id);
    long actualId = testItem.getId();
    assertNotNull(actualId);
    assertEquals(74L, actualId);
  }

  @Test
  public void get_price_happy_path() throws Exception {
    BigDecimal price = testItem.getPrice();
    assertNotNull(price);
    assertEquals(new BigDecimal(2.99), price);
  }

  @Test
  public void set_price_happy_path() throws Exception {
    BigDecimal price = new BigDecimal(3.49);
    testItem.setPrice(price);
    BigDecimal actual = testItem.getPrice();
    assertNotNull(actual);
    assertEquals(price, actual);
  }

  @Test
  public void get_description_happy_path() throws Exception {
    String description = testItem.getDescription();
    assertNotNull(description);
    assertEquals("A fancy test item, for testing.", description);
  }

  @Test
  public void set_description_happy_path() throws Exception {
    String description = "A new description.";
    testItem.setDescription(description);
    String actual = testItem.getDescription();
    assertNotNull(actual);
    assertEquals(description, actual);
  }
}
