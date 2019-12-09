package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.util.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		log.info(LogHelper.buildLogString(new String[]{"submitting order for user ", username}));
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info(LogHelper.buildLogString("user not found"));
			return ResponseEntity.notFound().build();
		}
		Cart cart = user.getCart();
		if(cart.getItems().size() == 0) {
			log.info(LogHelper.buildLogString("cart is empty"));
			return new ResponseEntity("Cart is empty!", HttpStatus.OK);
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info(LogHelper.buildLogString(new String[]{"new order saved ", order.toString()}));
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info(LogHelper.buildLogString(new String[]{"request for order history for user ", username}));
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info(LogHelper.buildLogString("user not found"));
			return ResponseEntity.notFound().build();
		}
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
