package com.gearit.api.controller.userprovider;

import com.gearit.api.dto.view.UserProviderView;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.user.UserProviderService;
import com.gearit.api.utils.http.PageableRequest;
import com.gearit.api.utils.http.PageableResponse;
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

    @PostMapping
    public ResponseEntity<PageableResponse<UserProviderView>> getUserProviders(
            @RequestBody PageableRequest<UserProvider> request
    ) {
        var userViews = userProviderService.getUserProviders(request);
        var response = new PageableResponse<>(userViews.getTotalElements(), userViews.getContent(), request);
        return ResponseEntity.ok(response);
    }
}
