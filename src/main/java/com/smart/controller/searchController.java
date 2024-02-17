package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
public class searchController {
	
	@Autowired
	private ContactRepository cr;
	@Autowired
	private UserRepository ur;
	
	
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?>  search(@PathVariable("query") String query,Principal principal )
	{
	
		User user = ur.getUserByUserName(principal.getName());
		List<Contact> contacts = cr.findByNameContainingAndUser(query,user );
		return ResponseEntity.ok(contacts);
				
	}

}
