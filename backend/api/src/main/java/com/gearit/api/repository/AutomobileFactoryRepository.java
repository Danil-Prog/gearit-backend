package com.gearit.api.repository;

import com.gearit.api.entity.auto.AutomobileFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AutomobileFactoryRepository extends
        JpaRepository<AutomobileFactory, Long>,
        JpaSpecificationExecutor<AutomobileFactory> {
}
