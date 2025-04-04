package com.linkDatabase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "geo_units_2")
public class GeoUnits2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="geo_unit_id")
    @NotNull
    private Integer geoUnitId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getGeoUnitId() {
        return geoUnitId;
    }
    public void setGeoUnitId(Integer geoUnitId) {
        this.geoUnitId = geoUnitId;
    }
}