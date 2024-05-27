package br.com.loginapi.model.dto;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;

}
