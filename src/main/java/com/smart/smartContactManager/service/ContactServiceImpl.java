package com.smart.smartContactManager.service;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.Contact;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public void saveContact(Contact contact, String username, MultipartFile mutlipartFile) throws IOException {

        if (mutlipartFile.isEmpty()) {
            System.out.println("File is empty"); //if file is empty
        } else {
            // Generate a unique file name by appending a UUID before the file extension
            String originalFileName = mutlipartFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Set the unique file name to the contact
            contact.setImage(uniqueFileName);

            // Specify the directory path to save the image
            File file = new ClassPathResource("static/img").getFile();

            // Get the path including the unique file name
            Path path = Paths.get(file.getAbsolutePath() + File.separator + uniqueFileName);

            // Copy the file to the specified path with the unique file name
            Files.copy(mutlipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        User user = userRepository.getUserByUserName(username); //get user by username
        contact.setUser(user); //set user to contact
        user.getContacts().add(contact); //add contact to user
        userRepository.save(user); //save user
    }
}
