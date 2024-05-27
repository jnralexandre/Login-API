package br.com.loginapi.service;

import br.com.loginapi.model.User;
import br.com.loginapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> buscarUsuario() {

        return userRepository.findAll();
    }

}
