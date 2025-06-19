package com.odos.odos_server;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestApi {
  @GetMapping
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("Hello World");
  }
}
