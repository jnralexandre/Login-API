package br.com.loginapi.model.dto;

import lombok.Data;

@Data
public class EmailUpdateRequestDTO {

    private String currentEmail;
    private String password;
    private String newEmail;

}
