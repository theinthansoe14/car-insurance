package com.tts.car_insurance.controller;

import com.tts.car_insurance.dto.request.ClaimRequest;
import com.tts.car_insurance.dto.response.ClaimResponse;
import com.tts.car_insurance.service.ClaimService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    @PreAuthorize("hasRole('USER') and #request.userId() == authentication.principal.id")
    public ResponseEntity<ClaimResponse> create(@Valid @RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.save(request));
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<ClaimResponse> findById(@PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(claimService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(claimService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClaimResponse> updateStatus(
            @PathVariable @NotBlank(message = "Id cannot be blank") String id,
            @RequestParam @NotBlank(message = "Status cannot be blank") String status
            ){
        return ResponseEntity.status(HttpStatus.OK).body(claimService.updateStatus(id , status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ) {
        claimService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
