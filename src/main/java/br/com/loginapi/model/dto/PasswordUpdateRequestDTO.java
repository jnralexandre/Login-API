package br.com.loginapi.model.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequestDTO {

    private String email;
    private String currentPassword;
    private String newPassword;

}
