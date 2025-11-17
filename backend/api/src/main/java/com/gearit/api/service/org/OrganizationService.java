package com.gearit.api.service.org;

import com.gearit.api.entity.org.Organization;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.OrganizationRepository;
import com.gearit.api.utils.validator.OrganizationValidator;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.pageable.PageableRequest;
import com.gearit.common.http.request.RegisterOrganizationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public void registerOrganization(RegisterOrganizationRequest request, UserProvider userProvider) {
        var organization = new Organization();
        organization.setFullname(request.fullname());
        organization.setAbbreviatedName(request.abbreviatedName());
        organization.setDescription(request.description());
        organization.setCreatedBy(userProvider);
        organization.setRegisteredAddress(request.registeredAddress());
        organization.setCurrentAddress(request.currentAddress());
        organization.setPhone(request.phone());
        organization.setInn(request.inn());
        organization.setKpp(request.kpp());
        organization.setYear(request.year());

        OrganizationValidator.validate(organization);

        organizationRepository.save(organization);
    }

    public Page<Organization> getOrganizationRequests(PageableRequest<Organization> request) {
        Specification<Organization> specification = request.getSpecification();
        return organizationRepository.findAll(specification, request.getPageRequest());
    }

    /**
     * Создаёт запрос на удаление организации, переводит в соответствующий статус.
     * Запрос ожидает подтверждения от администратора.
     */
    public void createRequestDeleteOrganization(Long organizationId) {
        var organization = getOrganizationByIdOrThrow(organizationId);

        organizationRepository.save(organization);
    }

    private Organization getOrganizationByIdOrThrow(Long id) {
        return organizationRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Error when getting organization",
                        String.format("Organization with ID: %s not found", id)
                ));
    }
}
