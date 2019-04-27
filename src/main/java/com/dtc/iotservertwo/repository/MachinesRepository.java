package com.dtc.iotservertwo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtc.iotservertwo.model.Machine;
import com.dtc.iotservertwo.model.MachineProductionInfo;

@Repository
public interface MachinesRepository extends MongoRepository<Machine, String> {
	
	@Query(value="{'machineId' : ?0}", fields="{ _id: 0, startHour: 1, stopHour: 1, targetOfDay: 1}")
	public List<MachineProductionInfo> findByMachineIdAndReturnStartHourAndStopHourAndTargetOfDay(String machineId);

}
