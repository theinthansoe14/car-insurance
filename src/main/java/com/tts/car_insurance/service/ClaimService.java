package com.tts.car_insurance.service;

import com.tts.car_insurance.dto.request.ClaimRequest;
import com.tts.car_insurance.dto.response.ClaimResponse;

import java.util.List;

public interface ClaimService {
    ClaimResponse save(ClaimRequest request);

    ClaimResponse findById(String id);

    List<ClaimResponse> findAll();

    void delete(String id);

    ClaimResponse updateStatus(String id, String status);
}
