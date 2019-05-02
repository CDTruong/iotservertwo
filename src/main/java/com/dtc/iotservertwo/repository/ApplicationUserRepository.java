package com.dtc.iotservertwo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dtc.iotservertwo.model.ApplicationUser;

@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
	
	public ApplicationUser findByUsername(String username);
	
	public ApplicationUser findByEmail(String email);
	
}
