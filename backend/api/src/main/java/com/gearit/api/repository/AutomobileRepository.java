package com.gearit.api.repository;

import com.gearit.api.entity.auto.Automobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AutomobileRepository extends
        JpaRepository<Automobile, Long>,
        JpaSpecificationExecutor<Automobile> {
}
