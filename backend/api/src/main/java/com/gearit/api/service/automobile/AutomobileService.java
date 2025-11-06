package com.gearit.api.service.automobile;

import com.gearit.api.entity.auto.AutomobileFactory;
import com.gearit.api.repository.AutomobileFactoryRepository;
import com.gearit.common.http.filter.PageableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AutomobileService {

    private final AutomobileFactoryRepository automobileFactoryRepository;

    @Autowired
    public AutomobileService(AutomobileFactoryRepository automobileFactoryRepository) {
        this.automobileFactoryRepository = automobileFactoryRepository;
    }

    public Page<AutomobileFactory> getAutomobileFactories(PageableRequest<AutomobileFactory> request) {
        Specification<AutomobileFactory> specification = request.getSpecification();
        return automobileFactoryRepository.findAll(specification, request.toPageable());
    }
}
