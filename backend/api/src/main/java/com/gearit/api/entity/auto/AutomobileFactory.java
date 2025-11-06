package com.gearit.api.entity.auto;

import com.gearit.common.constants.TableNames;
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
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = TableNames.AUTOMOBILE_FACTORIES)
public class AutomobileFactory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = TableNames.AUTOMOBILE_FACTORIES_MODELS,
            joinColumns = @JoinColumn(name = "automobile_factory_id"),
            inverseJoinColumns = @JoinColumn(name = "automobile_model_id")
    )
    private Set<AutomobileModel> models;
}
