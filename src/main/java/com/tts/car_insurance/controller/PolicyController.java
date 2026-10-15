package com.tts.car_insurance.controller;

import com.tts.car_insurance.dto.request.PolicyRequest;
import com.tts.car_insurance.dto.response.PolicyResponse;
import com.tts.car_insurance.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Policy Management", description = "Endpoints for managing insurance policies")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    @Operation(summary = "Create a new policy", description = "Allows users to create a new insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Policy created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') and #request.userId() == authentication.principal.id")
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody PolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.save(request));
    }

    @Operation(summary = "Get policy by ID", description = "Retrieves policy details by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.body.userId() == authentication.principal.id")
    public ResponseEntity<PolicyResponse> findById(
            @Parameter(description = "Unique ID of the policy", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.findById(id));
    }

    @Operation(summary = "Get all policies", description = "Retrieves a list of all insurance policies")
    @ApiResponse(responseCode = "200", description = "List of policies retrieved successfully",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PolicyResponse.class))))
    @GetMapping
    public ResponseEntity<List<PolicyResponse>> findAll(
            @AuthenticationPrincipal Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.findAll());
    }

    @Operation(summary = "Update policy", description = "Updates an existing policy by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @policySecurityService.isPolicyOwner(#id, authentication.principal.id)")
    public ResponseEntity<PolicyResponse> update(
            @Parameter(description = "Unique ID of the policy to update", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody PolicyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.update(id, request));
    }

    @Operation(summary = "Delete policy", description = "Deletes an insurance policy by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Policy deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @policySecurityService.isPolicyOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique ID of the policy to delete", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ) {
        policyService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}