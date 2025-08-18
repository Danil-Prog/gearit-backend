package com.gearit.api.repository;

import com.gearit.api.entity.enpoint.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
}
