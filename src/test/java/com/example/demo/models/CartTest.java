package com.example.demo.models;

import com.example.demo.model.persistence.Cart;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CartTest {

  private Cart testCart;

  @Before
  public void setup() {
    testCart = new Cart();
    testCart.setId(2L);
  }

  @Test
  public void get_id_happy_path() throws Exception {
    long id = testCart.getId();
    assertEquals(2L, id);
  }

}
