package com.dtc.iotservertwo.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtc.iotservertwo.model.Machine;
import com.dtc.iotservertwo.model.MachineProductionData;
import com.dtc.iotservertwo.model.MachineProductionInfo;
import com.dtc.iotservertwo.repository.MachinesRepository;

@RestController
@RequestMapping("api")
public class MachineController {
	
	@Autowired
	MachinesRepository machinesRepository;
	
	@GetMapping("machines")
	public List<Machine> get() {
		return machinesRepository.findAll();
	}
	
	@GetMapping("machine/{id}")
	public Machine getMachineById(@PathVariable("id") String id) {
		Machine machineById = machinesRepository.findByMachineId(id);
		return machineById;
	}
	
	@GetMapping("pinionshaft-production-info")
	public List<MachineProductionData> getProductionTargetData() throws ParseException {
		Date date = new Date();
		SimpleDateFormat todayDayFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		String todayDayStr = todayDayFormat.format(date);

		List<MachineProductionData> productionDataList = new ArrayList<MachineProductionData>();
		List<MachineProductionInfo> productionInfo = machinesRepository.findByMachineIdAndReturnStartHourAndStopHourAndTargetOfDay("pinionshaft");
		
		MachineProductionData productionDataStartTarget = new MachineProductionData();		
		productionDataStartTarget.setTime(dateFormat.parse(todayDayStr + "T" + productionInfo.get(0).getStartHour()));
		productionDataStartTarget.setProducts(0);
		productionDataList.add(productionDataStartTarget);
		
		MachineProductionData productionDataStopTarget = new MachineProductionData();
		productionDataStopTarget.setTime(dateFormat.parse(todayDayStr + "T" + productionInfo.get(0).getStopHour()));
		productionDataStopTarget.setProducts(productionInfo.get(0).getTargetOfDay());
		productionDataList.add(productionDataStopTarget);
		
		return productionDataList;
	}
	
	@PostMapping("operation-status")
	public Machine updateOperationStatus(@RequestBody Machine machine) {
		Machine readyUpdateMachine = machinesRepository.findByMachineId(machine.getMachineId());
		readyUpdateMachine.setOpStatus(machine.getOpStatus());
		machinesRepository.save(readyUpdateMachine);
		return readyUpdateMachine;
	}

}
