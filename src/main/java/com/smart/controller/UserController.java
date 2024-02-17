package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private ContactRepository cr;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	@ModelAttribute
	public void AddCommonData(Model m, Principal p) {
		String uname = p.getName();
		User u = ur.getUserByUserName(uname);
		m.addAttribute("user", u);
	}

	// home dashboard
	@RequestMapping("/index")
	public String dashboard(Model m, Principal p) {
		m.addAttribute("title", "index");
		return "normal/user_dashboard";
	}

	// open add form handler

	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "Add contact");
		m.addAttribute("contact", new Contact());
		return "normal/add_contact_form";

	}

	// process contact form
	@PostMapping("/process-contact")
	public String process_Contact(@ModelAttribute Contact contact,Model m, @RequestParam("profileimage") MultipartFile file,
			Principal p) {
		
		try {

			String name = p.getName();
			User u = ur.getUserByUserName(name);

			// processing file.......
			if (file.isEmpty()) {
				System.out.println("file is empty");
				contact.setImageUrl("myDefault.svg.png");

			} else {
				// upload the file to folder....
				contact.setImageUrl(file.getOriginalFilename());
				File f = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(f.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("file uploaded safely");
			}

			u.getContacts().add(contact);
			contact.setUser(u);

			this.ur.save(u);

			System.out.println("user ===:" + u);

			System.out.println("contact ===:" + contact.getName());
			// message..............
			
			m.addAttribute("message", new Message("successfully added contact","alert-success"));


		} catch (Exception e) {
			System.out.println("error :-");
			e.printStackTrace();

			// message.........
			m.addAttribute("message", new Message("failed to added contact","alert-danger"));


		}
		return "normal/add_contact_form";
	}
	
	
	
	//show contact handler
	//perpage = 5;
	//current page =
	
	@GetMapping("/show_Contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal p)
	{
		m.addAttribute("title","user contacts");
		
		User u = ur.getUserByUserName(p.getName());
		
		Pageable pageable = PageRequest.of(page,5);
		
		Page<Contact> contacts = cr.findContactsbyUser(u.getId(),pageable);
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_Contacts";
	}
	
	
	//show particular contact
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid")  Integer cid,Model m,Principal p)
	{
		Optional<Contact> OPcontact=	cr.findById(cid);
		Contact contact =OPcontact.get();
		
		//current user
		User user = ur.getUserByUserName(p.getName());
		if(user.getId()== contact.getUser().getId())
		{
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getName());
		}
		System.out.println("showing contact :"+contact.getName());
		
		return "normal/contact_Detail";
	}
	
	
	//delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model m,Principal p)
	{
		
		//chec.................
		User user = ur.getUserByUserName(p.getName());
		Optional<Contact> con =cr.findById(cid);
		Contact contact = con.get();
		System.out.println("conatct is :"+contact.getcId());
		if(user.getId()== contact.getUser().getId())
		{
			
			try {
				File f = new ClassPathResource("static/img").getFile();
//				Path path = Paths.get(f.getAbsolutePath() + File.separator + contact.getImageUrl());
//				File file =path.toFile();
//				file.delete();
				if(!contact.getImageUrl().equals("myDefault.svg.png")) {
					Paths.get(f.getAbsolutePath() + File.separator + contact.getImageUrl()).toFile().delete();
					System.out.println("deleting contact PROFILE with id :"+contact.getcId());
				}
				

			} catch (IOException e) {
				System.out.println("__________FILE NOT DELETED_______________");
				e.printStackTrace();
			}
			System.out.println("deleting contact with id :"+contact.getcId());

			contact.setUser(null);
			cr.deleteById(contact.getcId());
		}
		else {
			System.out.println("unauthorized user tried to deleting id"+cid+"+++++++++++++++++++++");
		}
		
		
//		m.addAttribute("message", new Message("successfully deleted contact","alert-success"));

		return "redirect:/user/show_Contacts/0";
	}
	
	
	//update Controller
	
	@PostMapping("/updateForm/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Model m)
	{
		m.addAttribute("title","update form :"+cid);
		Contact c = cr.findById(cid).get();
		m.addAttribute("contact",c);
		return "normal/updateForm";				
				
	}
	
	//update process-update handlwe
	
	@PostMapping("/process-update")
	public String process_update(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,Model m,Principal p) 
	{
		try {
			if(!file.isEmpty())
			{
				//filework
				File f = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(f.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				if(!contact.getImageUrl().equals("myDefault.svg.png")) {
					Paths.get(f.getAbsolutePath() + File.separator + contact.getImageUrl()).toFile().delete();
					
				}
				
				
				contact.setImageUrl(file.getOriginalFilename());
			}
			else {
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		User user = ur.getUserByUserName(p.getName());
		
		contact.setUser(user);
		cr.save(contact);
		System.out.println("user :"+user.getId());
		System.out.println("printing updated contact_________-");
		System.out.println("contact :"+contact);
		
		return "redirect:/user/"+contact.getcId()+"/contact"; 
	}
	
	
	//user profile handler
	
	@GetMapping("/profile")
	public String yourProfile(Model m)
	{
		m.addAttribute("title","user profile");
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String settings()
	{
		return "normal/settings";
	}

	//change paaword handler
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("opass") String opass,@RequestParam("npass") String npass,Principal p,Model m)
	{
		System.out.println("opass :"+opass);
		System.out.println("npass : "+npass);
		
		User u = ur.getUserByUserName(p.getName());
		if(pe.matches(opass, u.getPassword()))
		{
			System.out.println("pass matched");
		u.setPassword(pe.encode(npass));
		ur.save(u);
		m.addAttribute("msg",new Message(" Your password successfully updated", "alert-success"));
		return "normal/password-update-success";
		}
		m.addAttribute("msg",new Message(" Your password not updated", "alert-danger"));

		return "normal/password-update-success";
	}
	
	
	
	
	
}



