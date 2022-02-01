package com.projet.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteJobDtoRequest {

    @NotEmpty @Size(min = 2, message = "name should have at least 2 characters")
    private String name;
    @NotEmpty @Size(min = 2, message = "identifier should be at least 2 characters")
    private String identifier;
    @NotEmpty @Size(min = 2, message = "field should have at least 2 characters")
    private String field;
    @NotEmpty @Size(min = 2, message = "profile should have at least 2 characters")
    private String profile;
    @NotEmpty @Size(min = 2, message = "siret should have at least 2 characters")
    private String siret;
    private String website;
    @NotNull
    private Double stars;
    @NotEmpty @Size(min = 2, message = "address should have at least 2 characters")
    private String address;
    @NotNull
    private Boolean alternance;
    @NotEmpty @Size(min = 2, message = "contact mode should have at least 2 characters")
    private String contactMode;
    @NotEmpty @Size(min = 2, message = "size should have at least 2 characters")
    private String size;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotEmpty @Size(min = 2, message = "added date should have at least 2 characters")
    private String addedDate;
}
