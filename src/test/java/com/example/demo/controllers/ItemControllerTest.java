package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

  private final static Logger log = LoggerFactory.getLogger(ItemControllerTest.class);
  private ItemController testItemController;
  private ItemRepository testItemRepository = mock(ItemRepository.class);
  private Item testItem;

  @Before
  public void setup() {
    testItemController = new ItemController();
    testItem = new Item();
    testItem.setId(0L);
    testItem.setName("Round Test Widget");
    testItemRepository.save(testItem);
    TestUtils.injectObject(testItemController, "itemRepository", testItemRepository);
  }

  @Test
  public void get_items_happy_path() throws Exception {
    log.debug("Running get_items_happy_path test");
    final ResponseEntity<List<Item>> response = testItemController.getItems();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void get_items_by_id_happy_path() throws Exception {
    log.debug("Running get_items_by_id_happy_path test");
    log.debug("Stubbing test item.");
    when(testItemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(testItem));
    final ResponseEntity<Item> response = testItemController.getItemById(0L);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void get_items_by_id_not_found() throws Exception {
    log.debug("Running test: get_items_by_id_happy_path");
    final ResponseEntity<Item> response = testItemController.getItemById(0L);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void get_items_by_name_happy_path() throws Exception {
    log.debug("Running test: get_items_by_name_happy_path");
    log.debug("Stubbing item.");
    when(testItemRepository.findByName("Round Test Widget")).thenReturn(Collections.singletonList(testItem));
    final ResponseEntity<List<Item>> response = testItemController.getItemsByName(testItem.getName());
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void get_items_by_name_not_found() throws Exception {
    log.debug("Running test: get_items_by_name_not_found");
    final ResponseEntity<List<Item>> response = testItemController.getItemsByName("Round Test Widget");
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

}
