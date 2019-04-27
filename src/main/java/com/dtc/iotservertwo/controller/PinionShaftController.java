package com.dtc.iotservertwo.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtc.iotservertwo.model.MachineProductionData;
import com.dtc.iotservertwo.model.MachineProductionInfo;
import com.dtc.iotservertwo.model.PinionShaft;
import com.dtc.iotservertwo.model.PinionShaftJudgmentValue;
import com.dtc.iotservertwo.model.PinionShaftLeftRightMeasureValue;
import com.dtc.iotservertwo.model.PinionShaftMeasureValue;
import com.dtc.iotservertwo.repository.MachinesRepository;
import com.dtc.iotservertwo.repository.PinionShaftRepository;


@RestController
@RequestMapping("api")
public class PinionShaftController {
	
	@Autowired
	PinionShaftRepository pinionShaftRepository;

	@Autowired
	MachinesRepository machinesRepository;
	
	@GetMapping("pinionshaft")
	public List<PinionShaft> get() {
		return pinionShaftRepository.findAll();
	}
	
	@PostMapping("pinionshaft")
	public PinionShaft save(@RequestBody PinionShaft pinionShaftObj) {
		Date date = new Date();
		pinionShaftObj.setTime(date);
		pinionShaftRepository.save(pinionShaftObj);
		return pinionShaftObj;
	}
	
	@GetMapping("pinionshaftmeasurevalueleft")
	public List<PinionShaftMeasureValue> getMeasureValueLeft() {
		return pinionShaftRepository.findBySideAndReturnMeasureValueAndTime(0, PageRequest.of(0, 50, Sort.by(Direction.DESC, "time")));
	}
	
	@GetMapping("pinionshaftmeasurevalueright")
	public List<PinionShaftMeasureValue> getMeasureValueRight() {
		return pinionShaftRepository.findBySideAndReturnMeasureValueAndTime(1,PageRequest.of(0, 50, Sort.by(Direction.DESC, "time")));
	}
	
	@GetMapping("pinionshaft-left-right")
	public PinionShaftLeftRightMeasureValue getMeasureLeftRight() {
		PinionShaftLeftRightMeasureValue pinionLeftRightMeasureValue = new PinionShaftLeftRightMeasureValue();
		pinionLeftRightMeasureValue.setLeft(getMeasureValueLeft());
		pinionLeftRightMeasureValue.setRight(getMeasureValueRight());
		return pinionLeftRightMeasureValue;
	}
	
	@GetMapping("pinionshaft-judgment")
	public List<PinionShaftJudgmentValue> getJudgmentValue() throws ParseException {
		
		Date date = new Date();
		SimpleDateFormat todayDayFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String todayDayStr = todayDayFormat.format(date);
		List<MachineProductionInfo> pinionShaftProductionInfo = machinesRepository.findByMachineIdAndReturnStartHourAndStopHourAndTargetOfDay("pinionshaft");		
		return pinionShaftRepository.findByTime(dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStartHour()), dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStopHour()));
	}
	
	@GetMapping("pinionshaft-judgment-ok")
	public List<MachineProductionData> getOkJudgment() throws ParseException {
		
		List<MachineProductionData> pinionShaftProductionOkJudgmentList = new ArrayList<MachineProductionData>();
		
		Date date = new Date();
		SimpleDateFormat todayDayFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String todayDayStr = todayDayFormat.format(date);

		List<MachineProductionInfo> pinionShaftProductionInfo = machinesRepository.findByMachineIdAndReturnStartHourAndStopHourAndTargetOfDay("pinionshaft");
		int startHourInt = Integer.parseInt(firstTwo(pinionShaftProductionInfo.get(0).getStartHour()));
		int stopHourInt = Integer.parseInt(firstTwo(pinionShaftProductionInfo.get(0).getStopHour()));

		Date[] startHoursList = getStartHoursList(todayDayStr, startHourInt, stopHourInt);
		Date[] stopHoursList = getStopHoursList(todayDayStr, startHourInt, stopHourInt);
		
		List<PinionShaftJudgmentValue> pinionShaftJudgmentValueList = pinionShaftRepository.findByTime(dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStartHour()), dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStopHour()));
		int productCount=0;
		for (int i=0; i<=stopHourInt-startHourInt; i++) {			
			for(int j=0; j<pinionShaftJudgmentValueList.size(); j++) {				
				if(pinionShaftJudgmentValueList.get(j).getTime().after(startHoursList[i]) && pinionShaftJudgmentValueList.get(j).getTime().before(stopHoursList[i]) && pinionShaftJudgmentValueList.get(j).getJudgment() == 1 ) {
					productCount++;
				}
			}
			MachineProductionData pinionShaftProductionData = new MachineProductionData();
			pinionShaftProductionData.setProducts(productCount);
			pinionShaftProductionData.setTime(startHoursList[i]);
			pinionShaftProductionOkJudgmentList.add(pinionShaftProductionData);
		}	
		
		return pinionShaftProductionOkJudgmentList;
	}
	
	@GetMapping("pinionshaft-judgment-ng")
	public List<MachineProductionData> getNgJudgment() throws ParseException {
		
		List<MachineProductionData> pinionShaftProductionNgJudgmentList = new ArrayList<MachineProductionData>();
		
		Date date = new Date();
		SimpleDateFormat todayDayFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String todayDayStr = todayDayFormat.format(date);

		List<MachineProductionInfo> pinionShaftProductionInfo = machinesRepository.findByMachineIdAndReturnStartHourAndStopHourAndTargetOfDay("pinionshaft");
		int startHourInt = Integer.parseInt(firstTwo(pinionShaftProductionInfo.get(0).getStartHour()));
		int stopHourInt = Integer.parseInt(firstTwo(pinionShaftProductionInfo.get(0).getStopHour()));

		Date[] startHoursList = getStartHoursList(todayDayStr, startHourInt, stopHourInt);
		Date[] stopHoursList = getStopHoursList(todayDayStr, startHourInt, stopHourInt);
		
		List<PinionShaftJudgmentValue> pinionShaftJudgmentValueList = pinionShaftRepository.findByTime(dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStartHour()), dateFormat.parse(todayDayStr + "T" + pinionShaftProductionInfo.get(0).getStopHour()));

		int productCount=0;
		for (int i=0; i<=stopHourInt-startHourInt; i++) {
			for(int j=0; j<pinionShaftJudgmentValueList.size(); j++) {				
				if(pinionShaftJudgmentValueList.get(j).getTime().after(startHoursList[i]) && pinionShaftJudgmentValueList.get(j).getTime().before(stopHoursList[i]) && pinionShaftJudgmentValueList.get(j).getJudgment() == 0 ) {
					productCount++;
				}
			}
			MachineProductionData pinionShaftProductionData = new MachineProductionData();
			pinionShaftProductionData.setProducts(productCount);
			pinionShaftProductionData.setTime(startHoursList[i]);
			pinionShaftProductionNgJudgmentList.add(pinionShaftProductionData);
		}	
		
		return pinionShaftProductionNgJudgmentList;
	}
	
	@GetMapping("pinionshaft-judgment-ok-ng")
	public List<List<MachineProductionData>> pinionShaftProductOkNgJudgment() throws ParseException {
		List<List<MachineProductionData>> pinionShaftProductOkNgJudgmentList = new ArrayList<List<MachineProductionData>>();
		pinionShaftProductOkNgJudgmentList.add(getOkJudgment());
		pinionShaftProductOkNgJudgmentList.add(getNgJudgment());
		return pinionShaftProductOkNgJudgmentList;
	}
	
	public String firstTwo(String str) {
	    return str.length() < 2 ? str : str.substring(0, 2);
	}
	
	public Date[] getStartHoursList(String todayStr, int startHour, int stopHour) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date[] startHoursList = new Date[stopHour-startHour+1];
		for (int i = startHour; i <= stopHour; i++) {
			if (i<10) {
				String hourStr = "0" + Integer.toString(i) + ":00:00";
				startHoursList[i-startHour] = dateFormat.parse(todayStr + "T" + hourStr);
			} else {
				String hourStr = Integer.toString(i) + ":00:00";
				startHoursList[i-startHour] = dateFormat.parse(todayStr + "T" + hourStr);
			}
		}
		return startHoursList;
	}
	
	public Date[] getStopHoursList(String todayStr, int startHour, int stopHour) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date[] stopHoursList = new Date[stopHour-startHour+1];
		for (int i = startHour; i <= stopHour; i++) {
			if (i<10) {
				String hourStr = "0" + Integer.toString(i) + ":59:00";
				stopHoursList[i-startHour] = dateFormat.parse(todayStr + "T" + hourStr);
			} else {
				String hourStr = Integer.toString(i) + ":59:00";
				stopHoursList[i-startHour] = dateFormat.parse(todayStr + "T" + hourStr);
			}
		}
		return stopHoursList;
	}
}
