package com.eventapp.service;

import com.eventapp.dto.UserDTO;
import com.eventapp.enums.UserRole;
import com.eventapp.exception.CustomException;
import com.eventapp.exception.ResourceNotFoundException;
import com.eventapp.model.User;
import com.eventapp.repository.UserRepository;
import com.eventapp.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTool jwtTool;

    public Optional<User> getUser(int username){
        return userRepository.findById(Long.valueOf(username));
    }

    public void register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(UserRole.valueOf(userDTO.getRole()));
        userRepository.save(user);
    }

    public String login(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials");
        }
        return jwtTool.createToken(user.getUsername());
    }
}
