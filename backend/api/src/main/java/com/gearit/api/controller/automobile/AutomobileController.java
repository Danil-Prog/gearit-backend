package com.gearit.api.controller.automobile;

import com.gearit.api.dto.view.AutomobileView;
import com.gearit.api.entity.auto.Automobile;
import com.gearit.api.entity.auto.AutomobileFactory;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.automobile.AutomobileService;
import com.gearit.common.http.filter.PageableRequest;
import com.gearit.common.http.filter.PageableResponse;
import com.gearit.common.http.request.CreateAutomobileRequest;
import com.gearit.common.http.request.DeleteAutomobilesAuthUserRequest;
import com.gearit.common.http.request.UpdateAutomobileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping
    public ResponseEntity<Void> createAutomobile(
            @RequestBody CreateAutomobileRequest request,
            Authentication auth
    ) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        automobileService.createAutomobile(request, userProvider);
        return ResponseEntity.ok().build();
    }

    /**
     * @see AutomobileView
     */
    @PostMapping("/my")
    public ResponseEntity<?> getAutomobilesAuthUser(
            @RequestBody PageableRequest<Automobile> request,
            Authentication auth
    ) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        var automobilesView = automobileService.getAutomobilesAuthUser(request, userProvider.getId());
        return PageableResponse.toResponseEntity(automobilesView, request);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteAutomobilesAuthUser(
            @RequestBody DeleteAutomobilesAuthUserRequest request,
            Authentication auth
    ) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        automobileService.deleteAutomobileById(request.automobileIds(), userProvider.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/factories-models")
    public ResponseEntity<?> getAutomobileFactoriesModels(
            @RequestBody PageableRequest<AutomobileFactory> request
    ) {
        var automobileFactories = automobileService.getAutomobileFactories(request);
        return PageableResponse.toResponseEntity(automobileFactories, request);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateAutomobile(
            @RequestBody UpdateAutomobileRequest request,
            @PathVariable("id") Long automobileId,
            Authentication auth
    ) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        automobileService.updateAutomobileById(automobileId, request, userProvider);
        return ResponseEntity.ok().build();
    }
}
