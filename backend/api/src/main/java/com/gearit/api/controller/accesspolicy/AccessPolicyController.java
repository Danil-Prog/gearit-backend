package com.gearit.api.controller.accesspolicy;


import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.endpoint.Endpoint;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import com.gearit.common.http.filter.PageableRequest;
import com.gearit.common.http.filter.PageableResponse;
import com.gearit.common.http.response.GetAvailableResourcesResponse;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/access-policy")
public class AccessPolicyController {

    private final AccessPolicyService accessPolicyService;

    @Autowired
    public AccessPolicyController(AccessPolicyService accessPolicyService) {
        this.accessPolicyService = accessPolicyService;
    }

    @PostMapping
    public ResponseEntity<PageableResponse<AccessPolicy>> getAccessPolicy(
            @RequestBody PageableRequest<AccessPolicy> request
    ) {
        var accessPolicies = accessPolicyService.getAllAccessPolicies(request);
        var response = PageableResponse.of(accessPolicies.getTotalElements(), accessPolicies.getContent(), request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/resources")
    public ResponseEntity<GetAvailableResourcesResponse> getAvailableResources(
            Authentication authentication
    ) {
        UserProvider userProvider = (UserProvider) authentication.getPrincipal();
        var endpoints = userProvider.getAccessPolicy()
                .getEndpoints()
                .stream()
                .map(Endpoint::getResource)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new GetAvailableResourcesResponse(endpoints));
    }
}
