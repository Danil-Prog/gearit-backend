package com.gearit.api.service.accesspolicy;

import com.gearit.api.repository.AccessPolicyRepository;
import com.gearit.api.repository.EndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessPolicyService {

    private final EndpointRepository endpointRepository;
    private final AccessPolicyRepository accessPolicyRepository;

    @Autowired
    public AccessPolicyService(
            EndpointRepository endpointRepository,
            AccessPolicyRepository accessPolicyRepository
    ) {
        this.endpointRepository = endpointRepository;
        this.accessPolicyRepository = accessPolicyRepository;
    }
}
