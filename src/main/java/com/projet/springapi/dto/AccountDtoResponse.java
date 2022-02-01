package com.projet.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profile;
    private String role;
    private Integer number;
    private String road;
    private String city;
    private String postCode;
    private Double latitude;
    private Double longitude;
}
