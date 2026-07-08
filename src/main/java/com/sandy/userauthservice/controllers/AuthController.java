package com.sandy.userauthservice.controllers;

import com.sandy.userauthservice.dtos.LoginRequestDTO;
import com.sandy.userauthservice.dtos.SignUpRequestDTO;
import com.sandy.userauthservice.dtos.UserDTO;
import com.sandy.userauthservice.model.User;
import com.sandy.userauthservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            User user = authService.signUp(
                    signUpRequestDTO.getName(),
                    signUpRequestDTO.getEmail(),
                    signUpRequestDTO.getPassword()
            );
            UserDTO userDTO = user.toUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            User user = authService.login(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );

            UserDTO userDTO = user.toUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
