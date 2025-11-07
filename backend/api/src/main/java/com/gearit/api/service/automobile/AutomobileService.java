package com.gearit.api.service.automobile;

import com.gearit.api.dto.view.AutomobileView;
import com.gearit.api.entity.auto.Automobile;
import com.gearit.api.entity.auto.AutomobileBodyType;
import com.gearit.api.entity.auto.AutomobileFactory;
import com.gearit.api.entity.auto.AutomobileModel;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.AutomobileFactoryRepository;
import com.gearit.api.repository.AutomobileRepository;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.filter.Filter;
import com.gearit.common.http.filter.FilterCondition;
import com.gearit.common.http.filter.PageableRequest;
import com.gearit.common.http.request.CreateAutomobileRequest;
import com.gearit.common.utils.EnumConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomobileService {

    private final AutomobileFactoryRepository automobileFactoryRepository;
    private final AutomobileRepository automobileRepository;

    @Autowired
    public AutomobileService(
            AutomobileFactoryRepository automobileFactoryRepository,
            AutomobileRepository automobileRepository
    ) {
        this.automobileFactoryRepository = automobileFactoryRepository;
        this.automobileRepository = automobileRepository;
    }

    public Page<AutomobileFactory> getAutomobileFactories(PageableRequest<AutomobileFactory> request) {
        Specification<AutomobileFactory> specification = request.getSpecification();
        return automobileFactoryRepository.findAll(specification, request.toPageable());
    }

    public Page<AutomobileView> getAutomobilesAuthUser(PageableRequest<Automobile> request, Long userProviderId) {
        request.addFilter(new Filter(
                Automobile.Fields.userProviderId,
                FilterCondition.EQUALS,
                userProviderId
        ));

        Specification<Automobile> specification = request.getSpecification();
        return automobileRepository.findAll(specification, request.toPageable()).map(AutomobileView::from);
    }

    public void createAutomobile(
            CreateAutomobileRequest request,
            UserProvider userProvider
    ) {
        Long factoryId = request.factoryId();
        Long modelId = request.modelId();

        AutomobileBodyType automobileBodyType = EnumConverter.fromEnum(AutomobileBodyType.class, request.bodyType());

        AutomobileFactory automobileFactory = getAutomobileFactoryByIdOrThrow(factoryId);

        AutomobileModel automobileModel = automobileFactory.getModels()
                .stream()
                .filter(model -> model.getId().equals(modelId))
                .findFirst()
                .orElseThrow(() ->
                        new WebClientException(
                                "Error when getting the automobile model",
                                "Couldn't find automobile model in the specified manufacturer"
                        ));

        var auto = new Automobile();

        auto.setFactory(automobileFactory);
        auto.setModel(automobileModel);
        auto.setType(automobileBodyType);
        auto.setLicense(request.license());
        auto.setColor(request.color());
        auto.setUserProviderId(userProvider.getId());

        automobileRepository.save(auto);
    }

    public void deleteAutomobileById(List<Long> ids, Long userProviderId) {
        List<Automobile> automobiles = automobileRepository.findAllById(ids);

        automobiles.forEach(automobile -> {
            if (!automobile.getUserProviderId().equals(userProviderId)) {
                throw new WebClientException(
                        "Failed to delete auto",
                        "Automobile being deleted is not the property of the current user"
                );
            }
        });

        automobileRepository.deleteAll(automobiles);
    }

    private AutomobileFactory getAutomobileFactoryByIdOrThrow(Long id) {
        return automobileFactoryRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Error when getting the automobile manufacturer",
                        String.format("Automobile manufacturer with ID: %s not found", id)
                ));
    }

    private Automobile getAutomobileByIdOrThrow(Long id) {
        return automobileRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Error when getting car",
                        "Automobile with ID: " + id + " not found"
                ));
    }
}
