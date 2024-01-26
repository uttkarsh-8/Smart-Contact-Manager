package com.smart.smartContactManager.controller;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        User user = new User();
        user.setName("uttu");
        user.setEmail("uttu.sri@outlook.com");
        userRepository.save(user);
        return "Working";
    }
}
