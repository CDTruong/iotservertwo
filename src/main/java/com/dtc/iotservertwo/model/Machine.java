package com.dtc.iotservertwo.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="machine")
public class Machine {	
	private ObjectId id;
	private String machineId;
	private String name;
	private String opStatus;
	private String position;
	private String operator;
	private String posMap;
	private String startHour;
	private String stopHour;
	private int targetOfDay;
}
