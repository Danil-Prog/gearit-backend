package com.gearit.api.repository;

import com.gearit.api.entity.org.OrganizationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationFeedbackRepository extends
        JpaRepository<OrganizationFeedback, Long>,
        JpaSpecificationExecutor<OrganizationFeedback> {
}
