package com.smart.smartContactManager.service;

import com.smart.smartContactManager.dto.UserRegistrationDto;
import com.smart.smartContactManager.entities.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto userDto);
}
