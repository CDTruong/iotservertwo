package com.dtc.iotservertwo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="pinionshaft")
public class PinionShaft {
	private double measureValue;
	private int side;
	private int judgment;
	private Date time;
}
