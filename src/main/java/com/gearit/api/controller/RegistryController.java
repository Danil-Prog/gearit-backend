package com.gearit.api.controller;

import com.gearit.api.controller.request.*;
import com.gearit.api.controller.response.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registry")
public class RegistryController {

    @PostMapping
    public ResponseEntity<RegistryResponse> registry(@RequestBody RegistryRequest registryRequest) {
        return ResponseEntity.ok(new RegistryResponse());
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new LoginResponse());
    }
}
