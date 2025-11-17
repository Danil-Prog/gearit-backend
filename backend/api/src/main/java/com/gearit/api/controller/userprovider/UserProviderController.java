package com.gearit.api.controller.userprovider;

import com.gearit.api.dto.view.UserProviderView;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.user.UserProviderService;
import com.gearit.common.http.pageable.PageableRequest;
import com.gearit.common.http.pageable.PageableResponse;
import com.gearit.common.http.request.BlockUserProvidersRequest;
import com.gearit.common.http.request.UpdateAccessPolicyUserProviderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-provider")
public class UserProviderController {

    private final UserProviderService userProviderService;

    @Autowired
    public UserProviderController(UserProviderService userProviderService) {
        this.userProviderService = userProviderService;
    }

    /**
     * @see UserProviderView
     */
    @PostMapping
    public ResponseEntity<?> getUserProviders(
            @RequestBody PageableRequest<UserProvider> request
    ) {
        var userViews = userProviderService.getUserProviders(request);
        return PageableResponse.toResponseEntity(userViews, request);
    }

    @PostMapping("/access-policy")
    public ResponseEntity<Void> updateAccessPolicyUserProvider(
            @RequestBody UpdateAccessPolicyUserProviderRequest request
    ) {
        userProviderService.updateAccessPolicyUserProvider(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block")
    public ResponseEntity<Void> blockUserProviders(
            @RequestBody BlockUserProvidersRequest request
    ) {
        userProviderService.blockUserProviders(request);
        return ResponseEntity.ok().build();
    }
}
