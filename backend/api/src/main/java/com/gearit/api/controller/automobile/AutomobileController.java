package com.gearit.api.controller.automobile;

import com.gearit.api.entity.auto.AutomobileFactory;
import com.gearit.api.service.automobile.AutomobileService;
import com.gearit.common.http.filter.PageableRequest;
import com.gearit.common.http.filter.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/automobile")
public class AutomobileController {

    private final AutomobileService automobileService;

    @Autowired
    public AutomobileController(AutomobileService automobileService) {
        this.automobileService = automobileService;
    }

    @PostMapping("/factories-models")
    public ResponseEntity<?> getAutomobileFactoriesModels(
            @RequestBody PageableRequest<AutomobileFactory> request
    ) {
        var automobileFactories = automobileService.getAutomobileFactories(request);
        return PageableResponse.toResponseEntity(automobileFactories, request);
    }
}
