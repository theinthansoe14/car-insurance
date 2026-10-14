package com.tts.car_insurance.security;

import com.tts.car_insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("policySecurityService")
@RequiredArgsConstructor
public class PolicySecurityService {

    private final PolicyRepository policyRepository;

    public boolean isPolicyOwner(String policyId, String currentUserId) {
        return policyRepository.findById(policyId)
                .map(policy -> policy.getUserId().equals(currentUserId))
                .orElse(false);
    }
}