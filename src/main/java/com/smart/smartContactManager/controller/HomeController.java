package com.smart.smartContactManager.controller;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // handling for registering user

    @PostMapping("/do_register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result1, Model model) {
        try {
            if (result1.hasErrors()) {
                System.out.println("ERROR" + result1);
                model.addAttribute("user", user); // to keep the data in the form
                return "signup";
            }

            System.out.println(user);
            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImageUrl("default.png");
            User result = userRepository.save(user);
            model.addAttribute("user", new User()); // to clear the form
            model.addAttribute("success", "User has been registered successfully"); // to show user is successfully registered
                return "signup";


        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            model.addAttribute("duplicateEmail", "Email already exists");
            return "signup";
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred");
            return "signup";
        }
    }
}

