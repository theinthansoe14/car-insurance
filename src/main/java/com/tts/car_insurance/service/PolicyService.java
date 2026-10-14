package com.tts.car_insurance.service;

import com.tts.car_insurance.dto.request.PolicyRequest;
import com.tts.car_insurance.dto.response.PolicyResponse;

import java.util.List;

public interface PolicyService {
    PolicyResponse save(PolicyRequest request);

    PolicyResponse findById(String id);

    List<PolicyResponse> findAll();

    PolicyResponse update(String id, PolicyRequest request);

    void delete(String id);
}
