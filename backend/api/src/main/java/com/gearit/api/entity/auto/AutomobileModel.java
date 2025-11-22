package com.gearit.api.entity.auto;

import org.gearit.common.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.AUTOMOBILE_MODELS)
public class AutomobileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "year_from")
    private Long yearFrom;

    @Column(name = "year_to")
    private Long yearTo;
}
