package com.gearit.api.service.org;

import com.gearit.api.entity.org.Organization;
import com.gearit.api.entity.org.OrganizationFeedback;
import com.gearit.api.entity.org.OrganizationRequest;
import com.gearit.api.entity.org.OrganizationRequestStatus;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.OrganizationFeedbackRepository;
import com.gearit.api.repository.OrganizationRepository;
import com.gearit.api.repository.OrganizationRequestRepository;
import com.gearit.api.utils.validator.OrganizationRequestValidator;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.pageable.PageableRequest;
import com.gearit.common.http.pageable.filter.Filter;
import com.gearit.common.http.pageable.filter.FilterCondition;
import com.gearit.common.http.request.NewFeedbackRequest;
import com.gearit.common.http.request.NewOrganizationRequest;
import com.gearit.common.http.request.UpdateOrganizationRequestStatusRequest;
import com.gearit.common.utils.EnumConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gearit.api.entity.org.OrganizationAddress.fromDto;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRequestRepository organizationRequestRepository;
    private final OrganizationFeedbackRepository organizationFeedbackRepository;

    @Autowired
    public OrganizationService(
            OrganizationRepository organizationRepository,
            OrganizationRequestRepository organizationRequestRepository,
            OrganizationFeedbackRepository organizationFeedbackRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationRequestRepository = organizationRequestRepository;
        this.organizationFeedbackRepository = organizationFeedbackRepository;
    }

    public void newOrganizationRequest(NewOrganizationRequest request, UserProvider userProvider) {
        var organizationRequest = new OrganizationRequest();

        organizationRequest.setFullname(request.fullname());
        organizationRequest.setAbbreviatedName(request.abbreviatedName());
        organizationRequest.setDescription(request.description());
        organizationRequest.setCreatedBy(userProvider);
        organizationRequest.setRegisterAddress(fromDto(request.registeredAddress()));
        organizationRequest.setCurrentAddress(fromDto(request.currentAddress()));
        organizationRequest.setPhone(request.phone());
        organizationRequest.setInn(request.inn());
        organizationRequest.setKpp(request.kpp());
        organizationRequest.setCreatedAtOrganization(request.createdAtOrganization());
        organizationRequest.setCreatedAtOrganizationRequest(Instant.now());

        OrganizationRequestValidator.validate(organizationRequest);

        organizationRequestRepository.save(organizationRequest);
    }

    public void updateOrganizationStatus(Long id, UpdateOrganizationRequestStatusRequest request) {
        var organizationRequest = getOrganizationRequestByIdOrThrow(id);
        organizationRequest.setStatus(EnumConverter.toEnum(OrganizationRequestStatus.class, request.status()));

        organizationRequestRepository.save(organizationRequest);
    }

    public Page<OrganizationRequest> getOrganizationRequests(
            PageableRequest<OrganizationRequest> request,
            UserProvider userProvider
    ) {
        request.addFilter(new Filter(
                "createdBy.id",
                FilterCondition.EQUALS,
                userProvider.getId()
        ));

        Specification<OrganizationRequest> specification = request.getSpecification();
        return organizationRequestRepository.findAll(specification, request.getPageRequest());
    }

    /**
     * Создаёт запрос на удаление организации, переводит в соответствующий статус.
     * Запрос ожидает подтверждения от администратора.
     */
    public void createRequestDeleteOrganization(Long organizationId) {
        var organization = getOrganizationByIdOrThrow(organizationId);

        organizationRepository.save(organization);
    }

    public Page<OrganizationFeedback> getOrganizationRequestFeedbacks(
            Long id,
            PageableRequest<OrganizationFeedback> request
    ) {
        var organizationRequest = getOrganizationRequestByIdOrThrow(id);

        request.addFilter(new Filter(
                "request.id",
                FilterCondition.EQUALS,
                id
        ));

        var specification = request.getSpecification();

        return organizationFeedbackRepository.findAll(specification, request.getPageRequest());
    }

    public void newFeedbackOrganizationRequest(
            Long id,
            NewFeedbackRequest request,
            UserProvider userProvider
    ) {

    }

    private Organization getOrganizationByIdOrThrow(Long id) {
        return organizationRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Error when getting organization",
                        String.format("Organization with ID: %s not found", id)
                ));
    }

    private OrganizationRequest getOrganizationRequestByIdOrThrow(Long id) {
        return organizationRequestRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Error when getting organization request",
                        String.format("Organization request with ID: %s not found", id)
                ));
    }
}
