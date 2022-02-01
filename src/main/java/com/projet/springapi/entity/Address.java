package com.projet.springapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer number;
    private String road;
    private String city;
    private String postCode;
    private Double latitude;
    private Double longitude;

    public Address(Integer number, String road, String city, String postCode, Double latitude, Double longitude) {
        this.number = number;
        this.road = road;
        this.city = city;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
