package com.dtc.iotservertwo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineProductionInfo {
	private String startHour;
	private String stopHour;
	private int targetOfDay;
}
