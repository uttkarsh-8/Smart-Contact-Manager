package com.smart.smartContactManager.controller;


import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal){
        String userName = principal.getName(); // it will return the username of the logged in user or basically the unique identifier of the user
        System.out.println("USERNAME: "+ userName);
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
        System.out.println("USER DETAILS: "+ user);
        return "normal/user_dashboard";
    }
}
