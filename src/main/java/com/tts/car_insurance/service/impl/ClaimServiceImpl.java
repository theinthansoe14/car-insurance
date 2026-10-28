package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.dto.request.ClaimRequest;
import com.tts.car_insurance.dto.response.ClaimResponse;
import com.tts.car_insurance.dto.response.PolicyResponse;
import com.tts.car_insurance.exception.impl.ClaimNotFoundException;
import com.tts.car_insurance.exception.impl.PolicyNotFoundException;
import com.tts.car_insurance.model.Claim;
import com.tts.car_insurance.repository.ClaimRepository;
import com.tts.car_insurance.security.UserPrincipal;
import com.tts.car_insurance.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    private static final String CACHE_PREFIX ="claim:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    @Override
    public ClaimResponse save(ClaimRequest request) {
        Claim claim = toEntity(request);
        Claim savedClaim = claimRepository.save(claim);

        ClaimResponse claimResponse = toResponse(savedClaim);

        redisTemplate.opsForValue().set(CACHE_PREFIX + claimResponse.id() , claimResponse ,CACHE_TTL);

        return claimResponse;
    }

    @Override
    public ClaimResponse findById(String id) {
        String cacheKey = CACHE_PREFIX+id;
        ClaimResponse cachedClaim =  (ClaimResponse)redisTemplate.opsForValue().get(cacheKey);

        if(cachedClaim !=null){
            return cachedClaim;
        }

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ClaimNotFoundException("Claim not found with id: " + id));

        ClaimResponse claimResponse = toResponse(claim);

        redisTemplate.opsForValue().set(cacheKey , claimResponse , CACHE_TTL);

        return claimResponse;
    }

    @Override
    public List<ClaimResponse> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role->role.getAuthority().equals("ROLE_ADMIN"));

        if(isAdmin){
            return claimRepository.findAll().stream()
                    .map(this::toResponse)
                    .toList();
        }

        return claimRepository.findByUserId(principal.id()).stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    public void delete(String id) {

        long deleteCount = claimRepository.deleteByClaimId(id);

        if(deleteCount>0){
            redisTemplate.delete(CACHE_PREFIX+id);
        }else throw new ClaimNotFoundException("Claim not found with id:" + id);


    }

    @Override
    public ClaimResponse updateStatus(String id, String status) {

        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update()
                .set("status", status)
                .set("updatedAt", LocalDateTime.now());

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        Claim updatedClaim = mongoTemplate.findAndModify(query, update, options, Claim.class);

        if (updatedClaim == null) {
            throw new PolicyNotFoundException("Claim  not found with id: " + id);
        }

        ClaimResponse claimResponse = toResponse(updatedClaim);
        redisTemplate.opsForValue().set(CACHE_PREFIX + id , claimResponse , CACHE_TTL);

        return claimResponse;
    }

    private Claim toEntity(ClaimRequest request){

        double deductible = request.repairCost() * 0.1;
        double payoutAmount = request.repairCost() - deductible;

        return Claim.builder()
                .userId(request.userId())
                .claimDate(request.claimDate())
                .claimNumber(request.claimNumber())
                .deductible(deductible)
                .description(request.description())
                .payoutAmount(payoutAmount)
                .repairCost(request.repairCost())
                .policyId(request.policyId())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ClaimResponse toResponse(Claim claim){
        return new ClaimResponse(
                claim.getId(),
                claim.getUserId(),
                claim.getClaimNumber(),
                claim.getPolicyId(),
                claim.getRepairCost(),
                claim.getDeductible(),
                claim.getPayoutAmount(),
                claim.getDescription(),
                claim.getStatus(),
                claim.getClaimDate(),
                claim.getCreatedAt(),
                claim.getUpdatedAt()
        );
    }
}
