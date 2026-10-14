package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.model.Policy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Async
    public void sendEmailWithAsync(Policy policy){

        try {
            Thread.sleep(3000);

            log.info("==================================================");
            log.info("📧 [SEPARATED ASYNC MOCK EMAIL SERVICE]");
            log.info("To: {}", policy.getUserId());
            log.info("Policy Number: {} successfully created!", policy.getPolicyNumber());
            log.info("Start Date: {} | End Date: {}", policy.getStartDate(), policy.getEndDate());
            log.info("==================================================");
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("Async email Service Interrupted" , e);
        }
    }
}
