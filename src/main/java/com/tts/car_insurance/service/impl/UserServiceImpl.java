package com.tts.car_insurance.service.impl;


import com.tts.car_insurance.dto.request.UserRequest;
import com.tts.car_insurance.dto.response.UserResponse;
import com.tts.car_insurance.exception.impl.EmailAlreadyExistException;
import com.tts.car_insurance.exception.impl.UserNotFoundException;
import com.tts.car_insurance.model.User;
import com.tts.car_insurance.repository.UserRepository;
import com.tts.car_insurance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final String CACHE_PREFIX = "user:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    @Override
    public UserResponse save(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.email())){
            throw new EmailAlreadyExistException(userRequest.email()+" already exist with the ");
        }

        User user = toEntity(userRequest);
        User savedUser = userRepository.save(user);

        UserResponse userResponse= toResponse(savedUser);

        redisTemplate.opsForValue().set(
                CACHE_PREFIX + userResponse.id(),
                userResponse ,
                CACHE_TTL
        );

        return userResponse;
    }

    @Override
    public UserResponse findById(String id) {

        String cacheKey = CACHE_PREFIX + id;
       UserResponse cachedUser = (UserResponse) redisTemplate.opsForValue().get(cacheKey);

       if(cachedUser !=null){
           return cachedUser;
       }

       User user = userRepository.findById(id)
               .orElseThrow(()->new UserNotFoundException("User Not Found with the id :"+id));
       UserResponse userResponse = toResponse(user);

       redisTemplate.opsForValue().set(cacheKey,userResponse,CACHE_TTL);

       return userResponse;

    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(String id, UserRequest request) {

        Query query = new Query(Criteria.where("_id").is(id));


        Update update = new Update()
                .set("name", request.name())
                .set("email", request.email())
                .set("password", passwordEncoder.encode(request.password()))
                .set("dateOfBirth", request.dateOfBirth())
                .set("updatedAt", LocalDateTime.now());

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        User updatedUser = mongoTemplate.findAndModify(query,update,options,User.class);

        if (updatedUser == null) {
            throw new UserNotFoundException("User Not Found with the id :" + id);
        }

        UserResponse userResponse = toResponse(updatedUser);

        redisTemplate.opsForValue().set(CACHE_PREFIX+id ,userResponse ,CACHE_TTL);

        return userResponse;
    }

    @Override
    public void delete(String id) {
      long deleteCount =  userRepository.deleteUserById(id);

      if(deleteCount>0){
          redisTemplate.delete(CACHE_PREFIX + id);
      }else throw new UserNotFoundException("User not found with id"+id );


    }

    private UserResponse toResponse(User user){

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


    private User toEntity(UserRequest request){
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .dateOfBirth(request.dateOfBirth())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
