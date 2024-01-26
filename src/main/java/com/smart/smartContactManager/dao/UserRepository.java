package com.smart.smartContactManager.dao;

import com.smart.smartContactManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

}
