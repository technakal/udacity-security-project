package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.util.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final static Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		log.info(LogHelper.buildLogString(new String[]{"request to add item ", String.valueOf(request.getItemId()), " to cart"}));
		ApplicationUser user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.info(LogHelper.buildLogString("user not found"));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.info(LogHelper.buildLogString("item not found"));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		log.info(LogHelper.buildLogString("item added to cart"));
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		log.info(LogHelper.buildLogString(new String[]{"request to remove item ", String.valueOf(request.getItemId()), " from cart"}));
		ApplicationUser user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.info(LogHelper.buildLogString("user not found"));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.info(LogHelper.buildLogString("item not found"));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		log.info(LogHelper.buildLogString("item removed from cart"));
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(cart);
	}
		
}
