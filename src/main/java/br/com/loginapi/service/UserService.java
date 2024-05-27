package br.com.loginapi.service;

import br.com.loginapi.model.User;
import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;
import br.com.loginapi.model.dto.converter.UserConverter;
import br.com.loginapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> listarUsuario() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();

        for (User user : users) {
            userResponseDTOS.add(UserConverter.converterEntidadeParaDTO(user));
        }

        return userResponseDTOS;
    }

    public void cadastrarUsuario(UserRequestDTO userRequestDTO) {
        String name = userRequestDTO.getName();
        String email = userRequestDTO.getEmail();

        User user = UserConverter.converterDTOParaEntidade(userRequestDTO);

        userRepository.save(user);
    }

}
