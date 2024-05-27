package br.com.loginapi.model.dto.converter;

import br.com.loginapi.model.User;
import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;

public class UserConverter {

    public static User convertDTOToEntity(UserRequestDTO userRequestDTO) {
        User userEntity = new User();

        userEntity.setName(userRequestDTO.getName());
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setPassword(userRequestDTO.getPassword());

        return userEntity;
    }

    public static UserResponseDTO convertEntityToDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());

        return userResponseDTO;
    }

}
