package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository ur;
	@Autowired
	private BCryptPasswordEncoder bcpwd;

	@RequestMapping("/")
	public String test(Model m)
	{
		m.addAttribute("title","home-smart contact manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
		m.addAttribute("title","about-smart contact manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m)
	{
		m.addAttribute("user",new User());
		m.addAttribute("title","Register-smart contact manager");
		return "signup";
	}
	
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registeruser(@Valid @ModelAttribute("user") User u,BindingResult br,@RequestParam(value = "agreement",defaultValue = "false") boolean aggrement ,Model m,HttpSession session,HttpServletRequest request)
	{
		try {
			if(!aggrement)
			{
				System.out.println("you have not agreed the terma and conditions");
				throw new Exception("you have not agreed the terms and conditions");
			}
			
			if(br.hasErrors())
			{
				System.out.println(" Error :"+br.toString());
				m.addAttribute("user",u);
				return "signup";
			}
			
			
			
			
			u.setRole("ROLE_USER");
			u.setEnabled(true);
			u.setImageUrl("default.png");
			u.setPassword(bcpwd.encode(u.getPassword()));
//			System.out.println("____________________________");
//			System.out.println("pwd :  "+u.getPassword());
//			System.out.println("____________________________");
//			
			System.out.println("Agreement :"+aggrement);
			System.out.println("USER :"+u); 
			
			User result =this.ur.save(u);
			m.addAttribute("user",new User());
			System.out.println("res: "+result);
//			m.addAttribute("session", request.getSession());
//			session.setAttribute("message",new Message("Successfully Registered !!","alert-success"));
			 m.addAttribute("message", new Message("Successfully Registered!! ", "alert-success"));

			return "signup";
			
		} catch (Exception e) {
			System.out.println(e);
			m.addAttribute("user",u);
//			session.setAttribute("message",new Message("something went wrong !!"+e.getMessage(),"alert-danger"));
			 m.addAttribute("message", new Message("something went wrong !!\n may be you are using same mail/credentials to signup","alert-danger"));
			 if(!aggrement)
			 {
				 m.addAttribute("message", new Message("you have not agreed the terms and conditions","alert-danger"));


			 }
			return "signup";
		}
		
	}

	@RequestMapping("/signin")
	public String CustomLogin(Model m)
	{
		m.addAttribute("title","Login Page");
		return "login";
	}
}
