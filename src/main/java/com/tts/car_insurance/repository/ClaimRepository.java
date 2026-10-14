package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.Claim;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

public interface ClaimRepository extends MongoRepository<Claim,String> {

    @Query(value = "{ '_id' : ?0 }", delete = true)
    long deleteByClaimId(String id);

    List<Claim> findByUserId(String id);
}
