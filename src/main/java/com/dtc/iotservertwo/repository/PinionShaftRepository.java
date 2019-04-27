package com.dtc.iotservertwo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtc.iotservertwo.model.PinionShaft;
import com.dtc.iotservertwo.model.PinionShaftJudgmentValue;
import com.dtc.iotservertwo.model.PinionShaftMeasureValue;

@Repository
public interface PinionShaftRepository extends MongoRepository<PinionShaft, String> {
	@Query(value="{'side' : ?0}", fields="{ _id: 0, time : 1, measureValue : 1}")
	public List<PinionShaftMeasureValue> findBySideAndReturnMeasureValueAndTime(int side, Pageable pageable);
	
	@Query("{'time' : { $gte: ?0, $lte: ?1 } }") 
	public List<PinionShaftJudgmentValue> findByTime(Date startTime, Date stopTime);
}
