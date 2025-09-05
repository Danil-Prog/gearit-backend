package com.gearit.api.entity.endpoint;

import com.gearit.api.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.ENDPOINTS)
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method")
    private String method;

    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @Column(name = "resource")
    private String resource;
}
