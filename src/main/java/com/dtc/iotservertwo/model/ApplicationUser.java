package com.dtc.iotservertwo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "application-user")
public class ApplicationUser {
	
	private String name;
	private String username;
	private String password;
	private String userId;
	private String token;
	private String email;
	
}
