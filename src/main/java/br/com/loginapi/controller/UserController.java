package br.com.loginapi.controller;

import br.com.loginapi.model.dto.UserRequestDTO;
import br.com.loginapi.model.dto.UserResponseDTO;
import br.com.loginapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {

        return ResponseEntity.ok(this.userService.listUsers());
    }

    @PostMapping("/register-users")
    public ResponseEntity<Void> registerUsers(@RequestBody UserRequestDTO userRequestDTO) {
        userService.registerUsers(userRequestDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-users")
    public ResponseEntity<UserResponseDTO> deleteUsers(@RequestBody UserRequestDTO userRequestDTO) {

        return ResponseEntity.ok(this.userService.deleteUsers(userRequestDTO));
    }

}
