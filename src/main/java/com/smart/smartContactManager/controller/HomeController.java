package com.smart.smartContactManager.controller;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.User;
import com.smart.smartContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String register(@Valid @ModelAttribute("user")User user, Model model, HttpSession session, BindingResult result1){
        try{
            if (result1.hasErrors()){
                System.out.println("Error: "+ result1.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImageUrl("default.png");
            User result = userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered!!", "alert-success"));
            return "signup";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong!!" + e.getMessage(), "alert-danger"));
            return "signup";

        }

    }
}
