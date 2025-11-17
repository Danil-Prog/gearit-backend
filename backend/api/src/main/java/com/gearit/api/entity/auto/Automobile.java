package com.gearit.api.entity.auto;

import com.gearit.common.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@FieldNameConstants
@Table(name = TableNames.AUTOMOBILES)
public class Automobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_provider_id")
    private Long userProviderId;

    @OneToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    private AutomobileFactory factory;

    @OneToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private AutomobileModel model;

    @Column(name = "color")
    private String color;

    @Column(name = "license")
    private String license;

    @Column(name = "body_type")
    @Enumerated(EnumType.STRING)
    private AutomobileBodyType type;

    @Column(name = "year")
    private Long year;

    @Column(name = "odometer")
    private Long odometer;
}


