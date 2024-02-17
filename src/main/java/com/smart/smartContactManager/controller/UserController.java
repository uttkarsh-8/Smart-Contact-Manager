    package com.smart.smartContactManager.controller;


    import com.smart.smartContactManager.dao.UserRepository;
    import com.smart.smartContactManager.entities.Contact;
    import com.smart.smartContactManager.entities.User;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    import java.security.Principal;

    @Controller
    @RequestMapping("/user")
    public class UserController {

        @Autowired
        private UserRepository userRepository;

        @ModelAttribute //method to add common data to response
        public void addCommonData(Model model, Principal principal){
            String userName = principal.getName();
            User user = userRepository.getUserByUserName(userName);
            model.addAttribute("user", user);
        }

        @GetMapping("/index")
        public String dashboard(Model model){

            return "normal/user_dashboard";
        }

        //add form handler
        @GetMapping("/add-contact")
        public String openAddContactForm(Model model){
            model.addAttribute("title", "Add Contact");
            model.addAttribute("contact", new Contact());
            return "normal/add_contact_form";
        }
        // Processing add contact form
        @PostMapping("/process-contact")
        public String processContact(@ModelAttribute Contact contact, Principal principal){
            System.out.println("Contact info is hererererer"+contact);
            String name = principal.getName();
            User user = userRepository.getUserByUserName(name);
            contact.setUser(user);
            user.getContacts().add(contact);
            userRepository.save(user);
            System.out.println("ADDED TO DATA BASE");
            return "normal/add_contact_form";
        }
    }
