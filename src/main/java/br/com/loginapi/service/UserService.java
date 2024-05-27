package br.com.loginapi.service;

import br.com.loginapi.model.User;
import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;
import br.com.loginapi.model.dto.converter.UserConverter;
import br.com.loginapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final String MESSAGE_TO_EMAIL_INCORRECT = "O e-mail informado está incorreto.";
    private static final String MESSAGE_FOR_INCORRECT_PASSWORD = "A senha informada está incorreta.";

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> listUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();

        for (User user : users) {
            userResponseDTOS.add(UserConverter.convertEntityToDTO(user));
        }

        return userResponseDTOS;
    }

    public void registerUsers(UserRequestDTO userRequestDTO) {
        String name = userRequestDTO.getName();
        String email = userRequestDTO.getEmail();

        User user = UserConverter.convertDTOToEntity(userRequestDTO);

        userRepository.save(user);
    }

    public UserResponseDTO deleteUsers(UserRequestDTO userRequestDTO) {
        String email = userRequestDTO.getEmail();
        String password = userRequestDTO.getPassword();

        User userToDelete = userRepository.findByEmail(email);

        if (userToDelete == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMAIL_INCORRECT);
        }

        if (!userToDelete.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INCORRECT_PASSWORD);
        }

        userRepository.delete(userToDelete);

        return UserConverter.convertEntityToDTO(userToDelete);
    }

}
