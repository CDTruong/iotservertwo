package com.dtc.iotservertwo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtc.iotservertwo.model.ApplicationUser;
import com.dtc.iotservertwo.repository.ApplicationUserRepository;

@RestController
@RequestMapping("users")
public class ApplicationUserController {

	@Autowired
	ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping("sign-up")
	public void signUp(@RequestBody ApplicationUser appUser) {
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		applicationUserRepository.save(appUser);
	}	
	
}
