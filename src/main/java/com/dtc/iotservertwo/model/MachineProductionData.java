package com.dtc.iotservertwo.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineProductionData {
	private Date time;
	private int products;
}
