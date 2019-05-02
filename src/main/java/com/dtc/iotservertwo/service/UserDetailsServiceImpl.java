package com.dtc.iotservertwo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dtc.iotservertwo.model.ApplicationUser;
import com.dtc.iotservertwo.repository.ApplicationUserRepository;
import static java.util.Collections.emptyList;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	ApplicationUserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		ApplicationUser appUser = applicationUserRepository.findByUsername(username);
		ApplicationUser appUser = applicationUserRepository.findByEmail(username);
		if(appUser == null) {
			throw new UsernameNotFoundException(username);
		}
		
//		return new User(appUser.getUsername(), appUser.getPassword(), emptyList());
		return new User(appUser.getEmail(), appUser.getPassword(), emptyList());
	}
	
	
}
