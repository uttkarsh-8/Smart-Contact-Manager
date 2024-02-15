package com.smart.smartContactManager.service;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.dto.UserRegistrationDto;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUser(UserRegistrationDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setImageUrl("default.png");
        return userRepository.save(user);
    }


}