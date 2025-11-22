package com.gearit.api.entity.org;

import com.gearit.api.entity.user.UserProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.gearit.common.constants.TableNames;

import java.time.Instant;

@Entity
@Table(name = TableNames.FEEDBACKS)
public class OrganizationFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_provider_id", referencedColumnName = "id")
    private UserProvider userProvider;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "text", nullable = false)
    private String text;

    @OneToOne
    @JoinTable(name = TableNames.ORGANIZATION_REQUESTS_FEEDBACKS,
            joinColumns = @JoinColumn(name = "feedback_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_request_id"))
    private OrganizationRequest request;
}
