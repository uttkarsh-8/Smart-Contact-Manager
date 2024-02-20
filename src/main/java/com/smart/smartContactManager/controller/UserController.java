    package com.smart.smartContactManager.controller;


    import com.smart.smartContactManager.dao.ContactRepository;
    import com.smart.smartContactManager.dao.UserRepository;
    import com.smart.smartContactManager.entities.Contact;
    import com.smart.smartContactManager.entities.User;
    import com.smart.smartContactManager.service.ContactService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
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
    import java.util.Optional;

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

        //show contact handler
        // per page = 5[n]
        // current page = 0 [page]
        @GetMapping("/show-contacts/{page}")
        public String showContacts(@PathVariable("page") int page,Model model, Principal principal){
                model.addAttribute("title", "All Contacts");

                String userName = principal.getName();
                User user = userRepository.getUserByUserName(userName);

                //pagination logic
                //current page = page
                //contact per page = 5
            PageRequest pageRequest = PageRequest.of(page, 5);

            Page<Contact> contacts = contactRepository.findContactsByUserUserid(user.getId(), pageRequest);

                model.addAttribute("contacts", contacts); //contacts
                model.addAttribute("currentPage", page); //current page
                model.addAttribute("totalPages", contacts.getTotalPages()); //total pages

            return "normal/show_contacts";
        }

        //showing particular contact details
        @GetMapping("/contact/{cId}")
        public String showContactDetails(@PathVariable("cId") int cId, Model model, Principal principal){
            System.out.println("CID "+cId);
            model.addAttribute("title", "Contact Details");

            Optional<Contact> contactOptional = contactRepository.findById(cId);
            Contact contact = contactOptional.get();

            String userName =  principal.getName();
            User user = userRepository.getUserByUserName(userName);
            //check for user id and contact user id are same
            try{
                if (user.getId() == contact.getUser().getId()){
                    model.addAttribute("contact", contact);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return "normal/contact_detail";
        }

        //delete contact handler
        @GetMapping("/delete/{cId}")
        public String deleteContact(@PathVariable("cId")int cId, Model model, RedirectAttributes redirectAttributes){

            Optional<Contact> contactOptional = contactRepository.findById(cId);
            Contact contact = contactOptional.get();

            contact.setUser(null);

            contactRepository.delete(contact);

            System.out.println("CONTACT DELETED");
            redirectAttributes.addFlashAttribute("success", "Contact deleted successfully");

            return "redirect:/user/show-contacts/0";
        }

        // update form handler

        @PostMapping("/update-contact/{cId}")
        public String updateForm(Model model, @PathVariable("cId") int cId){
            model.addAttribute("title", "Update Contact");
            try{
                Optional<Contact> contactOptional = contactRepository.findById(cId);

                Contact contact = contactOptional.get();

                model.addAttribute("contact", contact);
//                model.addAttribute("success", "Contact updated successfully");

                return "normal/update_form";
            }catch (Exception e){
                model.addAttribute("error", "Cannot update the contact");
                e.printStackTrace();

                return "normal/update_form";
            }

        }

        //update contact handler

        @PostMapping("/process-update")
        public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile  multipartFile, Model model, Principal principal) {

            try {

                Contact oldContact = contactRepository.findById(contact.getCId()).get();

                if (!multipartFile.isEmpty()){

                // update new photo
                    File saveFile = new ClassPathResource("stat/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+ multipartFile.getOriginalFilename());
                    Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    contact.setImage(multipartFile.getOriginalFilename());


                }else {
                    contact.setImage(oldContact.getImage());
                }


                String userName = principal.getName();
                User user = userRepository.getUserByUserName(userName);

                contact.setUser(user);
                Contact updatedContact = contactRepository.save(contact);

                contact.setUser(user);

            } catch (Exception e) {
                e.printStackTrace();


                return "normal/update_form";
            }

            System.out.println("CONTACT name " + contact.getName());
            System.out.println("CONTACT ID " + contact.getCId());
            System.out.println("CONTACT email " + contact.getEmail());
            System.out.println("CONTACT phone " + contact.getPhone());
            System.out.println("CONTACT cId" + contact.getCId());
            return "redirect:/user/contact/"+contact.getCId();
        }
    }
