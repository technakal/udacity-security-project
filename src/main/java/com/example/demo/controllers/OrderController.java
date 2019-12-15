package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final static Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("Activity in OrderController.");
		log.info("POST request submitted to /submit/{username}.");
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("POST /submit/{username} failed. CAUSE: User not found.");
			return ResponseEntity.notFound().build();
		}
		Cart cart = user.getCart();
		if(cart.getItems().size() == 0) {
			log.info("POST /submit/{username} failed. CAUSE: Cart is empty.");
			return ResponseEntity.badRequest().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		user.setCart(new Cart());
		userRepository.save(user);
		log.info("POST /submit/{username} succeeded. RESPONSE: 200 OK");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Activity in OrderController.");
		log.info("GET request submitted to /history/{username}.");
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("GET /history/{username} failed. CAUSE: User not found.");
			return ResponseEntity.notFound().build();
		}
		log.info("GET /history/{username} succeeded. RESPONSE: 200 OK");
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
