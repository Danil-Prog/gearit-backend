package com.gearit.api.entity.org;

import com.gearit.api.entity.user.UserProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.gearit.common.constants.TableNames;

import java.time.Instant;
import java.util.Set;

@Data
@FieldNameConstants
@Entity
@Table(name = TableNames.ORGANIZATION_REQUESTS)
public class OrganizationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullname;

    @Column(name = "abbreviated_name")
    private String abbreviatedName;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "register_address_id", referencedColumnName = "id")
    private OrganizationAddress registerAddress;

    @OneToOne
    @JoinColumn(name = "current_address_id", referencedColumnName = "id")
    private OrganizationAddress currentAddress;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "date_creation_organization")
    private Instant createdAtOrganization;

    @Column(name = "date_creation_request")
    private Instant createdAtOrganizationRequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserProvider createdBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrganizationRequestStatus status = OrganizationRequestStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = TableNames.ORGANIZATION_REQUESTS_FEEDBACKS,
            joinColumns = @JoinColumn(name = "organization_request_id"),
            inverseJoinColumns = @JoinColumn(name = "feedback_id")
    )
    private Set<OrganizationFeedback> feedbacks;
}
