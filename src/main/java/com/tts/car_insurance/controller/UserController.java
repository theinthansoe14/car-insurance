package com.tts.car_insurance.controller;

import com.tts.car_insurance.dto.request.UserRequest;
import com.tts.car_insurance.dto.request.VehicleRequest;
import com.tts.car_insurance.dto.response.UserResponse;
import com.tts.car_insurance.dto.response.VehicleResponse;
import com.tts.car_insurance.service.UserService;
import com.tts.car_insurance.service.VehicleService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User & Vehicle Management", description = "Endpoints for managing users and their vehicles")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final VehicleService vehicleService;

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves user details by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse> findById(
            @Parameter(description = "Unique ID of the user", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Operation(summary = "Update user", description = "Updates an existing user's details by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse> update(
            @Parameter(description = "Unique ID of the user to update", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody UserRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, request));
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique ID of the user to delete", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id
    ){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Add vehicle to user", description = "Creates and registers a new vehicle associated with a specific user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<VehicleResponse> createVehicle(
            @Parameter(description = "Unique ID of the user to associate the vehicle with", example = "65a4b3c21f00000000000001", required = true)
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @Valid @RequestBody VehicleRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(id, request));
    }
}