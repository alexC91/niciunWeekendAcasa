package com.linkDatabase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "locations")
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "lat")
    @NotNull
    private Double lat;

    @Column(name = "lon")
    @NotNull
    private Double lon;

    @Column(name = "city_id")
    @NotNull
    private Integer cityId;

    @Column(name = "county_id")
    @NotNull
    private Integer countyId;

    @Column(name = "geo_area_id")
    @NotNull
    private Integer geoAreaId;

    @Column(name = "geo_unit_id")
    @NotNull
    private Integer geoUnitId;

    @Column(name = "geo_unit2_id")
    @NotNull
    private Integer geoUnit2Id;

    @Column(name = "origin_id")
    @NotNull
    private Integer originId;

    @Column(name = "time_period")
    private String timePeriod;

    @Column(name = "type_id")
    @NotNull
    private Integer typeId;

    @Column(name = "type2")
    private String type2;

    @Column(name = "status_id")
    @NotNull
    private Integer status_id;

    @Column(name = "hist_region_id")
    @NotNull
    private Integer histRegionId;

    @Column(name = "ethno_region_id")
    @NotNull
    private Integer ethnoRegionId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }
    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getCityId() {
        return cityId;
    }
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountyId() {
        return countyId;
    }
    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getGeoAreaId() {
        return geoAreaId;
    }
    public void setGeoAreaId(Integer geoAreaId) {
        this.geoAreaId = geoAreaId;
    }

    public Integer getGeoUnitId() {
        return geoUnitId;
    }
    public void setGeoUnitId(Integer geoUnitId) {
        this.geoUnitId = geoUnitId;
    }

    public Integer getGeoUnit2Id() {
        return geoUnit2Id;
    }
    public void setGeoUnit2Id(Integer geoUnit2Id) {
        this.geoUnit2Id = geoUnit2Id;
    }

    public Integer getOriginId() {
        return originId;
    }
    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getTimePeriod() {
        return timePeriod;
    }
    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Integer getTypeId() {
        return typeId;
    }
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getType2() {
        return type2;
    }
    public void setType2(String type2) {
        this.type2 = type2;
    }

    public Integer getStatus_id() {
        return status_id;
    }
    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getHistRegionId() {
        return histRegionId;
    }
    public void setHistRegionId(Integer histRegionId) {
        this.histRegionId = histRegionId;
    }

    public Integer getEthnoRegionId() {
        return ethnoRegionId;
    }
    public void setEthnoRegionId(Integer ethnoRegionId) {
        this.ethnoRegionId = ethnoRegionId;
    }
}