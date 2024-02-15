package com.smart.smartContactManager.config;

import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching User from database
       User user =  userRepository.getUserByUserName(username);

       if (user == null){
           throw new UsernameNotFoundException("User not found");
       }

       CustomUserDetails customUserDetails = new CustomUserDetails(user);

       return customUserDetails;

    }
}
