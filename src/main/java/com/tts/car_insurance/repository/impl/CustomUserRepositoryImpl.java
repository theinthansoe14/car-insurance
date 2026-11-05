package com.tts.car_insurance.repository.impl;

import com.tts.car_insurance.model.User;
import com.tts.car_insurance.repository.CustomUserRepository;
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
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public User updateUserDetails(String id, String name, String email, String password, LocalDate dateOfBirth, LocalDateTime updatedAt) {

        Query query = new Query(Criteria.where("_id").is(id));


        Update update = new Update()
                .set("name",name)
                .set("email",email)
                .set("password",password)
                .set("dateOfBirth",dateOfBirth)
                .set("updatedAt",updatedAt);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        return mongoTemplate.findAndModify(query,update,options,User.class);
    }
}
