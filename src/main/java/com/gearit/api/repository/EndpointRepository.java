package com.gearit.api.repository;

import com.gearit.api.entity.enpoint.Endpoint;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    Set<Endpoint> findAllByResourceIn(Collection<String> resources);
}
