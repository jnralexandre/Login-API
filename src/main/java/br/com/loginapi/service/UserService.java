package br.com.loginapi.service;

import br.com.loginapi.model.User;
import br.com.loginapi.model.dto.EmailUpdateRequestDTO;
import br.com.loginapi.model.dto.PasswordUpdateRequestDTO;
import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;
import br.com.loginapi.model.dto.converter.UserConverter;
import br.com.loginapi.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String MESSAGE_TO_EMPTY_NAME = "O campo nome não pode ser vazio.";
    private static final String MESSAGE_TO_EMPTY_EMAIL = "O campo e-mail não pode ser vazio.";
    private static final String MESSAGE_TO_EMPTY_PASSWORD = "O campo senha não pode ser vazio.";
    private static final String MESSAGE_TO_INVALID_EMAIL = "O e-mail fornecido não é válido.";
    private static final String MESSAGE_TO_EMAIL_INCORRECT = "O e-mail informado está incorreto.";
    private static final String MESSAGE_FOR_INCORRECT_PASSWORD = "A senha informada está incorreta.";
    private static final String MESSAGE_FOR_INVALID_NAME = "O novo nome deve ser diferente do atual.";
    private static final String MESSAGE_ALL_FIELDS_REQUIRED = "Preencha todos os campos.";
    private static final String MESSAGE_FOR_INVALID_EMAIL = "O novo e-mail deve ser diferente do atual.";
    private static final String MESSAGE_FOR_INVALID_PASSWORD = "A nova senha deve ser diferente da atual.";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserResponseDTO> listUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();

        for (User user : users) {
            userResponseDTOS.add(UserConverter.convertEntityToDTO(user));
        }

        return userResponseDTOS;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    public void registerUsers(UserRequestDTO userRequestDTO) {
        if (StringUtils.isBlank(userRequestDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMPTY_NAME);
        }

        if (StringUtils.isBlank(userRequestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMPTY_EMAIL);
        }

        if (StringUtils.isBlank(userRequestDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMPTY_PASSWORD);
        }

        if (!isValidEmail(userRequestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_INVALID_EMAIL);
        }

        User user = UserConverter.convertDTOToEntity(userRequestDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public UserResponseDTO deleteUsers(UserRequestDTO userRequestDTO) {
        if (StringUtils.isBlank(userRequestDTO.getEmail()) || StringUtils.isBlank(userRequestDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_ALL_FIELDS_REQUIRED);
        }

        String email = userRequestDTO.getEmail();
        String password = userRequestDTO.getPassword();

        User userToDelete = userRepository.findByEmail(email);

        if (userToDelete == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMAIL_INCORRECT);
        }

        if (!bCryptPasswordEncoder.matches(password, userToDelete.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INCORRECT_PASSWORD);
        }

        userRepository.delete(userToDelete);

        return UserConverter.convertEntityToDTO(userToDelete);
    }

    public boolean updateName(UserRequestDTO userRequestDTO) {
        if (StringUtils.isBlank(userRequestDTO.getEmail()) || StringUtils.isBlank(userRequestDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_ALL_FIELDS_REQUIRED);
        }

        User existingUser = userRepository.findByEmail(userRequestDTO.getEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMAIL_INCORRECT);
        }

        if (!bCryptPasswordEncoder.matches(userRequestDTO.getPassword(), existingUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INCORRECT_PASSWORD);
        }

        if (StringUtils.isBlank(userRequestDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMPTY_NAME);
        }

        if (existingUser.getName().equals(userRequestDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INVALID_NAME);
        }

        existingUser.setName(userRequestDTO.getName());
        userRepository.save(existingUser);

        return true;
    }

    public boolean updateEmail(EmailUpdateRequestDTO emailUpdateRequestDTO) {
        if (StringUtils.isBlank(emailUpdateRequestDTO.getCurrentEmail())
                || StringUtils.isBlank(emailUpdateRequestDTO.getPassword())
                || StringUtils.isBlank(emailUpdateRequestDTO.getNewEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_ALL_FIELDS_REQUIRED);
        }

        if (!isValidEmail(emailUpdateRequestDTO.getNewEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_INVALID_EMAIL);
        }

        User existingUser = userRepository.findByEmail(emailUpdateRequestDTO.getCurrentEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMAIL_INCORRECT);
        }

        if (!bCryptPasswordEncoder.matches(emailUpdateRequestDTO.getPassword(), existingUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INCORRECT_PASSWORD);
        }

        if (existingUser.getEmail().equals(emailUpdateRequestDTO.getNewEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INVALID_EMAIL);
        }

        existingUser.setEmail(emailUpdateRequestDTO.getNewEmail());
        userRepository.save(existingUser);

        return true;
    }


    public boolean updatePassword(PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        if (StringUtils.isBlank(passwordUpdateRequestDTO.getEmail())
                || StringUtils.isBlank(passwordUpdateRequestDTO.getCurrentPassword())
                || StringUtils.isBlank(passwordUpdateRequestDTO.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_ALL_FIELDS_REQUIRED);
        }

        User existingUser = userRepository.findByEmail(passwordUpdateRequestDTO.getEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_TO_EMAIL_INCORRECT);
        }

        if (!bCryptPasswordEncoder.matches(passwordUpdateRequestDTO.getCurrentPassword(), existingUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INCORRECT_PASSWORD);
        }

        if (passwordUpdateRequestDTO.getCurrentPassword().equals(passwordUpdateRequestDTO.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_FOR_INVALID_PASSWORD);
        }

        existingUser.setPassword(bCryptPasswordEncoder.encode(passwordUpdateRequestDTO.getNewPassword()));
        userRepository.save(existingUser);

        return true;
    }

}
