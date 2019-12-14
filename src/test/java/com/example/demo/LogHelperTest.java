package com.example.demo;

import com.example.demo.util.LogHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LogHelperTest {

  private final static Logger log = LoggerFactory.getLogger(LogHelperTest.class);

  @Test
  public void logger_returns_single_string() {
    String input = "this logger helper works fine";
    String output = LogHelper.buildLogString(input);
    log.debug(LogHelper.buildLogString(new String[]{"input was: ", input}));
    log.debug(LogHelper.buildLogString(new String[]{"output is: ", output}));
    assertNotNull(output);
    assertEquals(output, "devlog | " + input);
  }

  @Test
  public void logger_returns_multiple_string() {
    String[] input = new String[]{
        "this logger helper works fine",
        "even with multiple strings",
        "see, here's three strings"
    };
    String output = LogHelper.buildLogString(input);
    log.debug(LogHelper.buildLogString(new String[]{"output is: ", output}));
    String expectedOutput = "devlog | this logger helper works fine even with multiple strings see, here's three strings";
    assertNotNull(output);
    assertEquals(output, expectedOutput);
  }
}
