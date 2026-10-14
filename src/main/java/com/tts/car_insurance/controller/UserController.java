package com.tts.car_insurance.controller;


import com.tts.car_insurance.dto.request.UserRequest;
import com.tts.car_insurance.dto.request.VehicleRequest;
import com.tts.car_insurance.dto.response.UserResponse;
import com.tts.car_insurance.dto.response.VehicleResponse;
import com.tts.car_insurance.service.UserService;
import com.tts.car_insurance.service.VehicleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final VehicleService vehicleService;


    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse> findById(@PathVariable @NotBlank(message = "ID cannot be blank")String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse>update(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody UserRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ){
        userService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<VehicleResponse> createVehicle(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody VehicleRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(id,request));
    }
}
