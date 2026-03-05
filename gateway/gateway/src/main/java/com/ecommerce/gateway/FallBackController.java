package com.ecommerce.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class FallBackController {

    @GetMapping("/fallback/products")
    public ResponseEntity<List<String>> productsFallBack() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Product service is under Maintenance \uD83D\uDD27 or Unavailable ⛔ try again latter !  "));
    }

    @GetMapping("/fallback/orders")
    public ResponseEntity<List<String>> ordersFallBack() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("order service is under Maintenance \uD83D\uDD27 or Unavailable ⛔ try again latter !  "));
    }

    @GetMapping("/fallback/users")
    public ResponseEntity<List<String>> usersFallBack() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("user service is under Maintenance \uD83D\uDD27 or Unavailable ⛔ try again latter !  "));
    }

    @RequestMapping(value = "/fallback/carts", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<List<String>> cartsFallBack() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("cart service is under Maintenance \uD83D\uDD27 or Unavailable ⛔ try again latter !  "));
    }
}
