package com.gearit.api.repository;

import com.gearit.api.entity.accesspolicy.AccessPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessPolicyRepository extends JpaRepository<AccessPolicy, Long> {
}
