package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public List<User> all() {
		return repository.findAll();
	}
	
	@GetMapping("{id}")
	public Optional<User> getById(@PathVariable Integer id) {
		return repository.findById(id);
	}
	
	@PostMapping
	public User create(User user) {
		String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encoded);
		return repository.save(user);
	}
	
}
