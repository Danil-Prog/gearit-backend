package com.gearit.api.service.accesspolicy;

import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.enpoint.Endpoint;
import com.gearit.api.repository.AccessPolicyRepository;
import com.gearit.api.repository.EndpointRepository;
import com.gearit.api.utils.http.PageableRequest;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AccessPolicyService {

    private final AccessPolicyRepository accessPolicyRepository;
    private final EndpointRepository endpointRepository;

    private final Map<String, Set<Endpoint>> accessPolicyMap;

    private final Logger logger = LoggerFactory.getLogger(AccessPolicyService.class);

    @Autowired
    public AccessPolicyService(
            AccessPolicyRepository accessPolicyRepository,
            EndpointRepository endpointRepository
    ) {
        this.accessPolicyRepository = accessPolicyRepository;
        this.endpointRepository = endpointRepository;
        this.accessPolicyMap = new HashMap<>();
    }

    @PostConstruct
    public void initAccessPolicyGroups() {
        logger.info("Loading access policy groups in local cache...");

        updateAccessPolicyInCacheInternal();

        logger.info(
                "Access policy groups is successfully loaded in local cache. Total of access policy groups: {}",
                accessPolicyMap.size()
        );
    }

    public Set<Endpoint> getEndpointsByAccessPolicyName(String accessPolicyName) {
        return accessPolicyMap.get(accessPolicyName);
    }

    public Page<AccessPolicy> getAllAccessPolicies(PageableRequest<AccessPolicy> request) {
        Specification<AccessPolicy> spec = request.toSpecification();
        return accessPolicyRepository.findAll(spec, request.toPageable());
    }

    public AccessPolicy getAccessPolicyById(Long accessPolicyId) {
        return accessPolicyRepository.findById(accessPolicyId).orElse(null);
    }

    public AccessPolicy getAccessPolicyByName(String name) {
        return accessPolicyRepository.findByName(name);
    }

    /**
     * Обновляет внутренний кэш.
     * Кэширование политик доступа необходимо, так как запрос на
     * ...получение доступных пользователю политик является частым.
     */
    private void updateAccessPolicyInCacheInternal() {
        List<AccessPolicy> accessPolicies = accessPolicyRepository.findAll();
        accessPolicyMap.clear();

        accessPolicyMap.putAll(
                accessPolicies.stream().collect(
                        Collectors.toMap(
                                AccessPolicy::getName,
                                AccessPolicy::getEndpoints
                        )
                ));
    }
}
