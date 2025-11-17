package com.gearit.api.repository;

import com.gearit.api.entity.org.OrganizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRequestRepository extends
        JpaRepository<OrganizationRequest, Long>,
        JpaSpecificationExecutor<OrganizationRequest> {
}
