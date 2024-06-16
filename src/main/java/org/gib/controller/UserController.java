package org.gib.controller;

import lombok.extern.slf4j.Slf4j;
import org.gib.dto.ResponseDto;
import org.gib.dto.UserDTO;
import org.gib.model.UserEntity;
import org.gib.security.TokenProvider;
import org.gib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        PasswordValidator.validatePassword(userDto.getPassword());
        try {
            UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

            UserEntity registeredUser = userService.create(userEntity);
            UserDTO responseUserDto = UserDTO.builder()
                .email(registeredUser.getEmail())
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
            return ResponseEntity.ok(responseUserDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                .error(e.getMessage())
                .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        System.out.println(">>> 1");
        UserEntity entity = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if (entity != null) {
            final String token = tokenProvider.create(entity);
            final UserDTO responseUserDto = UserDTO.builder()
                .email(entity.getEmail())
                .id(entity.getId())
                .token(token)
                .build();

            return ResponseEntity.ok(responseUserDto);
        } else {
            System.out.println(">>> error");
            ResponseDto responseDto = ResponseDto.builder()
                .error("Login failed")
                .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
