package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<ApplicationUser> findById(@PathVariable Long id) {
		log.info("Activity in UserController.");
		log.info("GET request submitted to /user/id/{id}.");
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<ApplicationUser> findByUserName(@PathVariable String username) {
		log.info("Activity in UserController.");
		log.info("GET request submitted to /user/{username}.");
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("GET /user/{username} failed. CAUSE: User not found.");
			return ResponseEntity.notFound().build();
		}
		log.info("GET /user/{username} succeeded. RESPONSE: 200 OK");
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<ApplicationUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Activity in UserController.");
		log.info("POST request submitted to /user/create.");
		ApplicationUser user = new ApplicationUser();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(
				createUserRequest.getUsername() == null ||
  			createUserRequest.getPassword() == null ||
				createUserRequest.getConfirmPassword() == null
		) {
			log.info("POST /user/create failed. CAUSE: Request is missing required fields.");
			return ResponseEntity.badRequest().build();
		} else if(createUserRequest.getPassword().length() < 8) {
			log.info("POST /user/create failed. CAUSE: Password doesn't meet complexity requirements.");
			return ResponseEntity.badRequest().build();
		} else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.info("POST /user/create failed. CAUSE: Password and confirm password don't match.");
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("GET /user/create succeeded. RESPONSE: 200 OK");
		return ResponseEntity.ok(user);
	}
	
}
