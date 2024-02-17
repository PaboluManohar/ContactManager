package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

	//pagenation method
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactsbyUser(@Param("userId") int userId,Pageable p);
	//currentpage info and contact per page info
	
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
}
