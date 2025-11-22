package com.gearit.api.entity.org;

import com.gearit.api.entity.user.UserProvider;
import org.gearit.common.constants.TableNames;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.ORGANIZATIONS)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullname;

    @Column(name = "abbreviated_name")
    private String abbreviatedName;

    @Column(name = "description")
    private String description;

    @Column(name = "registered_address")
    private String registeredAddress;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "year")
    private Long year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_provider_id", referencedColumnName = "id")
    private UserProvider createdBy;
}
