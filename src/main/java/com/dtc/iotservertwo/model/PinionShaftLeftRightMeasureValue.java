package com.dtc.iotservertwo.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinionShaftLeftRightMeasureValue {
	private List<PinionShaftMeasureValue> left;
	private List<PinionShaftMeasureValue> right;	
}
