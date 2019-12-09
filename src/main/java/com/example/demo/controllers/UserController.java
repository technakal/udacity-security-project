package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.util.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		log.info(LogHelper.buildLogString(new String[]{"request for user with id ", String.valueOf(id)}));
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<ApplicationUser> findByUserName(@PathVariable String username) {
		log.info(LogHelper.buildLogString(new String[]{"request for user with username ", username}));
		ApplicationUser user = userRepository.findByUsername(username);
		if(user == null) {
			log.info(LogHelper.buildLogString("user not found"));
			return ResponseEntity.notFound().build();
		}
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<ApplicationUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
		ApplicationUser user = new ApplicationUser();
		user.setUsername(createUserRequest.getUsername());

		log.info(LogHelper.buildLogString(new String[]{"username set to value " + user.getUsername()}));

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(
				createUserRequest.getUsername() == null ||
  			createUserRequest.getPassword() == null ||
				createUserRequest.getConfirmPassword() == null
		) {
			String missingDataError = "Bad request: create user requires username, password, and confirm password.";
			log.info(LogHelper.buildLogString(missingDataError));
			return new ResponseEntity(missingDataError, HttpStatus.BAD_REQUEST);
		} else if(createUserRequest.getPassword().length() < 8) {
			String badPasswordError = "Bad request: user password is not long enough.";
			log.info(LogHelper.buildLogString(badPasswordError));
			return new ResponseEntity(badPasswordError, HttpStatus.BAD_REQUEST);
		} else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			String passwordMismatchError = "Bad request: password and confirm password do not match.";
			log.info(LogHelper.buildLogString(passwordMismatchError));
			return new ResponseEntity(passwordMismatchError, HttpStatus.BAD_REQUEST);
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info(LogHelper.buildLogString(new String[]{"new user saved ", user.toString()}));
		log.info(LogHelper.buildLogString("response 200 OK sent"));
		return ResponseEntity.ok(user);
	}
	
}
