package com.sandy.userauthservice.controllers;

import com.sandy.userauthservice.dtos.LoginRequestDTO;
import com.sandy.userauthservice.dtos.SignUpRequestDTO;
import com.sandy.userauthservice.dtos.UserDTO;
import com.sandy.userauthservice.dtos.UserToken;
import com.sandy.userauthservice.exception.UnauthorizedException;
import com.sandy.userauthservice.model.User;
import com.sandy.userauthservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
            UserToken userToken= authService.login(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );

            UserDTO userDTO = userToken.getUser().toUserDTO();
            String token = userToken.getToken();
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, token);

            HttpHeaders httpHeaders = new HttpHeaders(headers);

            return new ResponseEntity<>(userDTO, httpHeaders, HttpStatus.OK);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/validate-token")
    public void validateToken(@RequestBody String token) {
            Boolean res = authService.validateToken(token);
            if(!res) {
                throw new UnauthorizedException("unauthorized");
            }
    }

}
