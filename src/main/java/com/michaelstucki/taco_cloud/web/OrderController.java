package com.michaelstucki.taco_cloud.web;

import com.michaelstucki.taco_cloud.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
// Handles all requests beginning with /orders
@RequestMapping("/orders")
// Persist the TacoOrder bean for the duration of the client's session (bean scope)
@SessionAttributes("tacoOrder")
public class OrderController {

    // Handles Get requests to /orders/current
    @GetMapping("/current")
    public String orderForm() { return "orderForm"; }

    // Handles Post requests to /orders
    @PostMapping
    // TacoOrder instance properties are bound to the submitted form fields of orderForm.html
    public String processOrder(TacoOrder order, SessionStatus sessionStatus) {
        log.info("Order submitted: {}", order);
        // TacoOrder instance is created and put into the session when first taco was created
        //  using design.html and DesignTacoController
        // setComplete() cleans up the session for new order
        sessionStatus.setComplete();
        // Direct browser to root
        return "redirect:/";
    }
}
