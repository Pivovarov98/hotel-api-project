package org.example.hotelapiproject.controller.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.example.hotelapiproject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/stripe")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                              @RequestHeader("Stripe-Signature") String signature) throws SignatureVerificationException {

        Event event = Webhook.constructEvent(payload, signature, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())){

            Session session = (Session) event.getData().getObject();

            paymentService.confirmPayment(session.getId());
        }
        return ResponseEntity.ok().build();
    }
}
