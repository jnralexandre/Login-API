package br.com.loginapi.controller;

import br.com.loginapi.model.User;
import br.com.loginapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/buscar-usuarios")
    public ResponseEntity<List<User>> buscarUsuario() {

        return ResponseEntity.ok(this.userService.buscarUsuario());
    }


}
