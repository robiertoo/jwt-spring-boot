package com.example.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	

	@PostMapping("/login")
	public User login(@RequestParam("username") String username, @RequestParam("password") String password) {		
		User user = userRepository.findByUsername(username);
		
		if(new BCryptPasswordEncoder().matches(password, user.getPassword())) {
			String token = getJWTToken(user);
			User userToken = new User();
			userToken.setId(user.getId());
			userToken.setUsername(username);
			userToken.setRole(user.getRole());
			userToken.setToken(token);		
			return userToken;			
		}
		
		return new User();		
	}

	private String getJWTToken(User user) {
		String secretKey = "mySecretKey";
		String authority = "";
		
		if(user.getRole().getId() == 1) authority = "ADMIN";
		if(user.getRole().getId() == 2) authority = "COMMON_USER";
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(authority);
		
		String token = Jwts
				.builder()
				.setId(user.getId().toString())
				.setSubject(user.getUsername())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 60))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}
