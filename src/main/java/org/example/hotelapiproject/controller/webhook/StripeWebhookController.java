package org.example.hotelapiproject.controller.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @PostMapping("/stripe")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
        System.out.println("PAYMENT SUCCESS");
        System.out.println(payload);
        return ResponseEntity.ok().build();
    }
}
