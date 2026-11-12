package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.dto.request.PolicyRequest;
import com.tts.car_insurance.dto.response.PolicyResponse;
import com.tts.car_insurance.dto.response.UserResponse;
import com.tts.car_insurance.dto.response.VehicleResponse;
import com.tts.car_insurance.exception.impl.*;
import com.tts.car_insurance.model.Policy;
import com.tts.car_insurance.model.User;
import com.tts.car_insurance.repository.PolicyRepository;
import com.tts.car_insurance.repository.UserRepository;
import com.tts.car_insurance.service.PolicyService;
import com.tts.car_insurance.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final EmailService emailService;
    private final VehicleService vehicleService;

    private static final String CACHE_PREFIX = "policy:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);


    @Override
    public PolicyResponse save(PolicyRequest request) {

        LocalDate userDOB = getUserDOB(request.userId());

        int userAge = Period.between(userDOB, LocalDate.now()).getYears();

        if(userAge<18){
            throw new UnderAgeException("User age must be over 18");
        }

        VehicleResponse vehicle = vehicleService.getVehicleAndVerifyOwnership(request.vehicleId(),request.userId());

        double premiumAmount = calculatePremium(vehicle.carValue(), vehicle.year(), userAge);

        Policy policy = toEntity(request, premiumAmount);

        try {
            Policy savedPolicy = policyRepository.save(policy);
            PolicyResponse policyResponse = toResponse(savedPolicy);

            redisTemplate.opsForValue().set(CACHE_PREFIX + policyResponse.id(), policyResponse, CACHE_TTL);

            emailService.sendEmailWithAsync(savedPolicy);

            return policyResponse;
        }catch (DuplicateKeyException e){
            throw new PolicyNumberAlreadyExistException("Policy number :" +request.policyNumber()+ " is already exist" ) ;
        }
    }
    //policy:557ryjg



    @Override
    @PostAuthorize("hasRole('ADMIN') or returnObject.userId == authentication.principal.id")
    public PolicyResponse findById(String id) {
        String cacheKey = CACHE_PREFIX+id;
        PolicyResponse cachedPolicy =  (PolicyResponse)redisTemplate.opsForValue().get(cacheKey);

        if(cachedPolicy !=null){
            return cachedPolicy;
        }

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found with id: " + id));

        PolicyResponse policyResponse = toResponse(policy);

        redisTemplate.opsForValue().set(cacheKey , policyResponse , CACHE_TTL);

        return policyResponse;
    }

    @Override
    public List<PolicyResponse> findAllByUserId(String userId) {

        return policyRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PolicyResponse update(String id, PolicyRequest request) {

        vehicleService.getVehicleAndVerifyOwnership(request.vehicleId(),request.userId());

       Policy updatedPolicy = policyRepository.updatePolicyDetails(
               id,
               request.policyNumber(),
               request.userId(),
               request.vehicleId(),
               request.startDate(),
               request.endDate(),
               request.hasClaim(),
               LocalDateTime.now()

        );

        if (updatedPolicy == null) {
            throw new PolicyNotFoundException("Policy not found with id: " + id);
        }

        PolicyResponse policyResponse = toResponse(updatedPolicy);
        redisTemplate.opsForValue().set(CACHE_PREFIX + id , policyResponse , CACHE_TTL);

        return policyResponse;
    }

    @Override
    public void delete(String id) {

     long deleteCount =  policyRepository.deleteByPolicyId(id);

     if(deleteCount>0){
         redisTemplate.delete(CACHE_PREFIX+id);
     }else throw new PolicyNotFoundException("Policy not found with id: " + id);

    }

    @Scheduled(fixedRate = 120000)
    public void autoExpirePolicies(){
        LocalDate today = LocalDate.now();

        Query query = new Query(
                Criteria.where("endDate").lt(today)
                        .and ("status").is("ACTIVE")
        );

        Update update = new Update()
                .set("status","EXPIRED")
                .set("updatedAt",LocalDateTime.now());

         mongoTemplate.updateMulti(query,update,Policy.class);
    }

    private Policy toEntity(PolicyRequest request,double premiumAmount) {
        return Policy.builder()
                .policyNumber(request.policyNumber())
                .userId(request.userId())
                .vehicleId(request.vehicleId())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .premiumAmount(premiumAmount)
                .status("ACTIVE")
                .hasClaim(request.hasClaim())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getUserId(),
                policy.getVehicleId(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getPremiumAmount(),
                policy.getStatus(),
                policy.isHasClaim(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }

    private double calculatePremium(double carValue, int carYear, int userAge) {

        double premiumAmount = carValue * 0.03;

        if (userAge < 20) {
            premiumAmount *= 1.2;
        }

        int currentYear = LocalDate.now().getYear();
        int carAge = currentYear - carYear;

        if (carAge > 5) {
            premiumAmount *= 1.3;
        }

        return premiumAmount;
    }

    private LocalDate getUserDOB(String userId) {

        String cacheKey = "user:"+userId;

        UserResponse cachedUser = (UserResponse) redisTemplate.opsForValue().get(cacheKey);

        if(cachedUser != null){
            return cachedUser.dateOfBirth();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found with the id "+ userId));

        UserResponse userResponse = toResponseUser(user);
        redisTemplate.opsForValue().set(cacheKey , userResponse , CACHE_TTL);

        return user.getDateOfBirth();
    }


    private UserResponse toResponseUser(User user){

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getDateOfBirth(),
                user.getCreatedAt(),
                user.getUpdatedAt()

        );

    }

}
