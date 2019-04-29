package com.dtc.iotservertwo.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinionShaftProductionValue {
	private List<MachineProductionData> infoProductionData;
	private List<MachineProductionData> ngProductionData;
	private List<MachineProductionData> okProductionData;
}
