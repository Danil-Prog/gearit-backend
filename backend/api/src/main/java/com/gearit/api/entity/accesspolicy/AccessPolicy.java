package com.gearit.api.entity.accesspolicy;

import org.gearit.common.constants.TableNames;
import com.gearit.api.entity.endpoint.Endpoint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = TableNames.ACCESS_POLICIES)
public class AccessPolicy implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = TableNames.ACCESS_POLICIES_ENDPOINTS,
            joinColumns = @JoinColumn(name = "access_policy_id"),
            inverseJoinColumns = @JoinColumn(name = "endpoint_id")
    )
    private Set<Endpoint> endpoints;

    @Override
    public String getAuthority() {
        return name;
    }
}
