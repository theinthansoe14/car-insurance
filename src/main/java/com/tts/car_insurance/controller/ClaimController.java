package com.tts.car_insurance.controller;

import com.tts.car_insurance.dto.request.ClaimRequest;
import com.tts.car_insurance.dto.response.ClaimResponse;
import com.tts.car_insurance.service.ClaimService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Claim Management", description = "Endpoints for managing insurance claims")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    @Operation(summary = "Create a new claim", description = "Allows users to create a claim for their insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Claim created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClaimResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') and #request.userId() == authentication.principal.id")
    public ResponseEntity<ClaimResponse> create(@Valid @RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.save(request));
    }


    @Operation(summary = "Get claim by ID", description = "Retrieves claim details by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClaimResponse.class))),
            @ApiResponse(responseCode = "404", description = "Claim not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<ClaimResponse> findById(
            @Parameter(description = "Unique ID of the claim", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(claimService.findById(id));
    }


    @Operation(summary = "Get all claims", description = "Retrieves a list of all insurance claims")
    @ApiResponse(responseCode = "200", description = "List of claims retrieved successfully",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClaimResponse.class))))
    @GetMapping
    public ResponseEntity<List<ClaimResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(claimService.findAll());
    }

    @Operation(summary = "Update claim status", description = "Allows ADMIN to update the status of an existing claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClaimResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or Status", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ClaimResponse> updateStatus(
            @Parameter(description = "Unique ID of the claim to update", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "Id cannot be blank") String id,
            @Parameter(description = "New status to be applied", example = "APPROVED", required = true)
            @RequestParam @NotBlank(message = "Status cannot be blank") String status
            ){
        return ResponseEntity.status(HttpStatus.OK).body(claimService.updateStatus(id , status));
    }


    @Operation(summary = "Delete claim", description = "Deletes a claim by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Claim deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Claim not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') and #request.userId() == authentication.principal.id")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique ID of the claim to delete", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ) {
        claimService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
