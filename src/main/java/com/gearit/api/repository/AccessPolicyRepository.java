package com.gearit.api.repository;

import com.gearit.api.entity.accesspolicy.AccessPolicy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessPolicyRepository extends JpaRepository<AccessPolicy, Long> {

    @Query(value = "SELECT accessPolicy FROM AccessPolicy AS accessPolicy WHERE accessPolicy.name = :name")
    Optional<AccessPolicy> getAccessPolicyByName(String name);
}
