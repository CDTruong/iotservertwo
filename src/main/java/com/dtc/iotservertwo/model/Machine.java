package com.dtc.iotservertwo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="machine")
public class Machine {
	private String machineId;
	private String name;
	private int opStatus;
	private String position;
	private String operator;	
}
