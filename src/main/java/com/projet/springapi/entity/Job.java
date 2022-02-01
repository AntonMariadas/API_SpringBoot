package com.projet.springapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String identifier;
    private String name;
    private String field;
    private String profile;
    private String siret;
    private String website;
    private Double stars;
    private String address;
    private Boolean alternance;
    private String contactMode;
    private String size;
    private Double latitude;
    private Double longitude;
    private String addedDate;
    private String applicationDate;
    private String relaunchDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Job(String name, String identifier, String field, String profile, String siret, String website, Double stars, String address, Boolean alternance, String contactMode, String size, Double latitude, Double longitude, String addedDate, User user) {
        this.name = name;
        this.identifier = identifier;
        this.field = field;
        this.profile = profile;
        this.siret = siret;
        this.website = website;
        this.stars = stars;
        this.address = address;
        this.alternance = alternance;
        this.contactMode = contactMode;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addedDate = addedDate;
        this.user = user;
    }
}
