package com.linkDatabase;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "cities")
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    @NotNull
    private String name;

    // Câmpul countyId va rămâne, dar poți seta insertable/updatable=false
    // dacă dorești să nu îl modifici direct din entitate
    @Column(name="county_id")
    @NotNull
    private Integer countyId;

    // Maparea relației cu entitatea Counties
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Counties county;

    // Getteri și setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCountyId() { return countyId; }
    public void setCountyId(Integer countyId) { this.countyId = countyId; }

    public Counties getCounty() { return county; }
    public void setCounty(Counties county) { this.county = county; }
}
