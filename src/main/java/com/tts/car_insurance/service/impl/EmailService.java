package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.model.Policy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Async
    @Retryable(
            value = { RuntimeException.class ,InterruptedException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
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

    @Recover
    public void recoverEmailSend(RuntimeException e, Policy policy) {
        log.error("==================================================");
        log.error("❌ [EMAIL RECOVER FALLBACK]");
        log.error("Failed to send email for policy: {} after maximum retries.", policy.getPolicyNumber());
        log.error("Reason: {}", e.getMessage());
        log.error("==================================================");

    }
}
