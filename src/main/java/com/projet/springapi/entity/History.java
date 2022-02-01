package com.projet.springapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class History {
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
    private String address;
    private String applicationDate;
    private String relaunchDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public History(String name, String identifier, String field, String profile, String siret, String website, String address, String applicationDate, String relaunchDate, User user) {
        this.name = name;
        this.identifier = identifier;
        this.field = field;
        this.profile = profile;
        this.siret = siret;
        this.website = website;
        this.address = address;
        this.applicationDate = applicationDate;
        this.relaunchDate = relaunchDate;
        this.user = user;
    }
}
