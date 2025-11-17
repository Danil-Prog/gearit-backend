package com.gearit.api.service.accesspolicy;

import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.endpoint.Endpoint;
import com.gearit.api.repository.AccessPolicyRepository;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.pageable.PageableRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccessPolicyService {

    private final AccessPolicyRepository accessPolicyRepository;

    private final Map<String, Set<Endpoint>> accessPolicyMap;

    private final Logger logger = LoggerFactory.getLogger(AccessPolicyService.class);

    @Autowired
    public AccessPolicyService(AccessPolicyRepository accessPolicyRepository) {
        this.accessPolicyRepository = accessPolicyRepository;
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
        Specification<AccessPolicy> specification = request.getSpecification();
        return accessPolicyRepository.findAll(specification, request.getPageRequest());
    }

    public AccessPolicy getAccessPolicyByName(String name) {
        return accessPolicyRepository.findByName(name);
    }

    public AccessPolicy getAccessPolicyByIdOrThrow(Long accessPolicyId) {
        return accessPolicyRepository.findById(accessPolicyId)
                .orElseThrow(() ->  new WebClientException(
                        "Failed to get access policy",
                        String.format("Access policy with this id: [%s] does not exist", accessPolicyId)
                ));
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
