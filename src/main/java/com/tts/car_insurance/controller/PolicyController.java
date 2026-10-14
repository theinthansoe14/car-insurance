package com.tts.car_insurance.controller;

import com.tts.car_insurance.dto.request.PolicyRequest;
import com.tts.car_insurance.dto.response.PolicyResponse;
import com.tts.car_insurance.service.PolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/policies")
public class PolicyController {


    private final PolicyService policyService;

    @PostMapping
    @PreAuthorize("hasRole('USER') and #request.userId() == authentication.principal.id")
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody PolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.save(request));
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.body.userId() == authentication.principal.id")
    public ResponseEntity<PolicyResponse> findById(@PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> findAll(
            @AuthenticationPrincipal Principal principal
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @policySecurityService.isPolicyOwner(#id, authentication.principal.id)")
    public ResponseEntity<PolicyResponse> update(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody PolicyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @policySecurityService.isPolicyOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ) {
        policyService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
