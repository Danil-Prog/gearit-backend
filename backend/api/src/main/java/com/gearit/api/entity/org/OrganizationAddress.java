package com.gearit.api.entity.org;

import com.gearit.common.constants.TableNames;
import com.gearit.common.dto.OrganizationAddressDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.ORGANIZATION_ADDRESSES)
public class OrganizationAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index")
    private Long index;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building")
    private String building;

    @Column(name = "placement")
    private String placement;

    public OrganizationAddress(
            Long index,
            String region,
            String street,
            String city,
            String building,
            String placement) {
        this.index = index;
        this.region = region;
        this.street = street;
        this.city = city;
        this.building = building;
        this.placement = placement;
    }

    public OrganizationAddress() {

    }

    public static OrganizationAddress fromDto(OrganizationAddressDto dto) {
        return new OrganizationAddress(
                dto.index(),
                dto.region(),
                dto.street(),
                dto.city(),
                dto.building(),
                dto.placement()
        );
    }
}
