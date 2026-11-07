package com.tts.car_insurance.repository.impl;

import com.tts.car_insurance.model.Policy;
import com.tts.car_insurance.repository.CustomPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomPolicyRepositoryImpl implements CustomPolicyRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Policy updatePolicyDetails(String id, String policyNumber, String userId, String vehicleId, LocalDate startDate, LocalDate endDate, boolean hasClaim, LocalDateTime updatedAt) {

        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update()
                .set("policyNumber", policyNumber)
                .set("userId", userId)
                .set("vehicleId", vehicleId)
                .set("startDate", startDate)
                .set("endDate", endDate)
                .set("hasClaim", hasClaim)
                .set("updatedAt", updatedAt);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, update, options, Policy.class);
    }
}
