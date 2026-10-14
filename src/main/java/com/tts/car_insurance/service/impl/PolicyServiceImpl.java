package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.dto.request.PolicyRequest;
import com.tts.car_insurance.dto.response.PolicyResponse;
import com.tts.car_insurance.exception.impl.PolicyNotFoundException;
import com.tts.car_insurance.exception.impl.UserNotFoundException;
import com.tts.car_insurance.exception.impl.VehicleNotFoundException;
import com.tts.car_insurance.model.Policy;
import com.tts.car_insurance.model.User;
import com.tts.car_insurance.model.Vehicle;
import com.tts.car_insurance.repository.PolicyRepository;
import com.tts.car_insurance.repository.UserRepository;
import com.tts.car_insurance.repository.VehicleRepository;
import com.tts.car_insurance.security.UserPrincipal;
import com.tts.car_insurance.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final VehicleRepository vehicleRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final EmailService emailService;

    private static final String CACHE_PREFIX = "policy:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);


    @Override
    public PolicyResponse save(PolicyRequest request) {

        LocalDate userDOB = getUserDOB(request.userId());

        Vehicle vehicle = getVehicleDetails(request.vehicleId());

        double premiumAmount = calculatePremium(vehicle.getCarValue(), vehicle.getYear(),userDOB);

        Policy policy = toEntity(request,premiumAmount);
        Policy savedPolicy = policyRepository.save(policy);
        PolicyResponse policyResponse = toResponse(savedPolicy);

        redisTemplate.opsForValue().set(CACHE_PREFIX+policyResponse.id() ,policyResponse ,CACHE_TTL);

       emailService.sendEmailWithAsync(savedPolicy);

        return policyResponse;
    }



    @Override
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
    public List<PolicyResponse> findAll() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role->role.getAuthority().equals("ROLE_ADMIN"));

        if(isAdmin){
            return policyRepository.findAll().stream()
                    .map(this::toResponse)
                    .toList();
        }

        return policyRepository.findByUserId(principal.id()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PolicyResponse update(String id, PolicyRequest request) {

        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update()
                .set("policyNumber", request.policyNumber())
                .set("userId", request.userId())
                .set("vehicleId", request.vehicleId())
                .set("startDate", request.startDate())
                .set("endDate", request.endDate())
                .set("hasClaim", request.hasClaim())
                .set("updatedAt", LocalDateTime.now());

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        Policy updatedPolicy = mongoTemplate.findAndModify(query, update, options, Policy.class);

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

    private double calculatePremium(double carValue, int carYear, LocalDate userDOB) {

        double premiumAmount = carValue * 0.03;

        int userAge = Period.between(userDOB, LocalDate.now()).getYears();

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



    private Vehicle getVehicleDetails(String vehicleId) {

        String cacheKey = "vehicle:" + vehicleId;

        Vehicle cachedVehicle = (Vehicle) redisTemplate.opsForValue().get(cacheKey);

        if (cachedVehicle != null) {
            return cachedVehicle;
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()-> new VehicleNotFoundException("Vehicle Not Found with the id " + vehicleId));

        redisTemplate.opsForValue().set(cacheKey , vehicle , CACHE_TTL);

        return vehicle;

    }

    private LocalDate getUserDOB(String userId) {

        String cacheKey = "user:"+userId;

        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);

        if(cachedUser != null){
            return cachedUser.getDateOfBirth();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found with the id "+ userId));

        redisTemplate.opsForValue().set(cacheKey , user , CACHE_TTL);

        return user.getDateOfBirth();
    }


}
