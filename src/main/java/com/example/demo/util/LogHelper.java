package com.example.demo.util;

import com.example.demo.model.persistence.ApplicationUser;
import org.springframework.http.ResponseEntity;

public class LogHelper {

  public static String buildLogString(String message) {
    String logMessage = "devlog | ";
    return logMessage + message.trim();
  }

  public static String buildLogString(String[] messages) {
    String logMessage = "devlog | ";
    for (String message : messages) {
      logMessage += message + " ";
    }
    return logMessage.trim();
  }

}
