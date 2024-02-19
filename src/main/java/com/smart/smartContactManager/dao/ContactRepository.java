package com.smart.smartContactManager.dao;

import com.smart.smartContactManager.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    // to get contacts of a particular user
    @Query("from Contact as c where c.user.id =:userid")
    public List<Contact> findContactsByUserUserid(@Param("userid") int userid);

    // mainly for pagination

}
