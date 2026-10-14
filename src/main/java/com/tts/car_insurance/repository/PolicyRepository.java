package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.Policy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PolicyRepository extends MongoRepository<Policy,String> {

    @Query(value = "{ '_id' : ?0 }", delete = true)
    long deleteByPolicyId(String id);

    List<Policy> findByUserId(String userId);
}
