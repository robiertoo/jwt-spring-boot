package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@GetMapping
	public ResponseEntity<List<User>> all() {
		List<User> users = repository.findAll();
		
		if(users.isEmpty()) return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getById(@PathVariable Integer id) {
		Optional<User> user = repository.findById(id);
		
		if(!user.isPresent()) return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		return ResponseEntity.ok(user.get());
	}
	
	@PostMapping
	public User create(@RequestBody User user) {
		String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encoded);
		return repository.save(user);
	}
	
}
