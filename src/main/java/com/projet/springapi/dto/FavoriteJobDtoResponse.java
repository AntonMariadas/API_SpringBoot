package com.projet.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteJobDtoResponse {

    private Long id;
    private String name;
    private String identifier;
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
}
