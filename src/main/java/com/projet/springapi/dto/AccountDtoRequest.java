package com.projet.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoRequest {

    @NotEmpty @Size(min = 2, message = "first name should have at least 2 characters")
    private String firstName;
    @NotEmpty @Size(min = 2, message = "last name should have at least 2 characters")
    private String lastName;
    @NotEmpty @Email
    private String email;
    @NotEmpty @Size(min = 8, message = "password should have at least 8 characters")
    private String password;
    @NotEmpty @Size(min = 2, message = "profile should have at least 2 characters")
    private String profile;
    @NotNull
    private Integer number;
    @NotEmpty @Size(min = 2, message = "road should have at least 2 characters")
    private String road;
    @NotEmpty @Size(min = 2, message = "city should have at least 2 characters")
    private String city;
    @NotEmpty @Size(min = 5, max = 5, message = "post code should be 5 digits")
    private String postCode;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
