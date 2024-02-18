package com.smart.smartContactManager.service;

import com.smart.smartContactManager.entities.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContactService {
    void saveContact(Contact contact, String username, MultipartFile mutlipartFile) throws IOException;
}
