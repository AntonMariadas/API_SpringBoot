package com.projet.springapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDtoResponse {

    private Long id;
    private String name;
    private String identifier;
    private String field;
    private String profile;
    private String siret;
    private String website;
    private String address;
    private String applicationDate;
    private String relaunchDate;
}
