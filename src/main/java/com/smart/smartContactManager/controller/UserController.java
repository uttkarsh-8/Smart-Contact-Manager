    package com.smart.smartContactManager.controller;


    import com.smart.smartContactManager.dao.ContactRepository;
    import com.smart.smartContactManager.dao.UserRepository;
    import com.smart.smartContactManager.entities.Contact;
    import com.smart.smartContactManager.entities.User;
    import com.smart.smartContactManager.service.ContactService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import javax.validation.Valid;
    import java.io.File;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.security.Principal;
    import java.util.List;

    @Controller
    @RequestMapping("/user")
    public class UserController {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ContactService contactService;

        @Autowired
        private ContactRepository contactRepository;

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
        public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result, @RequestParam ("profileImage") MultipartFile multipartFile, Principal principal, RedirectAttributes redirectAttributes, Model model){
            try{
                if(result.hasErrors()){
                    System.out.println("Error "+result);
                    System.out.println("ERROR IN BINDING RESULT");
                    return "normal/add_contact_form";

                }
                String name = principal.getName();

                contactService.saveContact(contact, name, multipartFile);
                System.out.println("ADDED TO DATA BASE");

                redirectAttributes.addFlashAttribute("success", "Contact added successfully");
                    return "redirect:/user/add-contact";
            }catch (Exception e){
                System.out.println("Error occurred!!");
                e.printStackTrace();

                redirectAttributes.addFlashAttribute("error", "Failed to add contact");
                return "redirect:/user/add-contact";
            }
        }

        //show contacts
        @GetMapping("/show-contacts")
        public String showContacts(Model model, Principal principal){
                model.addAttribute("title", "All Contacts");

                String userName = principal.getName();
                User user = userRepository.getUserByUserName(userName);

                List<Contact> contacts = contactRepository.findContactsByUserUserid(user.getId());

                model.addAttribute("contacts", contacts);

            return "normal/show_contacts";
        }
    }
