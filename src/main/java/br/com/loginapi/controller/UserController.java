package br.com.loginapi.controller;

import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;
import br.com.loginapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/buscar-usuario")
    public ResponseEntity<List<UserResponseDTO>> listarUsuario() {

        return ResponseEntity.ok(this.userService.listarUsuario());
    }

    @PostMapping("/cadastrar-usuario")
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody UserRequestDTO userRequestDTO) {
        userService.cadastrarUsuario(userRequestDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
