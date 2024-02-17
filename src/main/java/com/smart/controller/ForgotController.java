package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService es;
	@Autowired
	private UserRepository ur;
	@Autowired
	private BCryptPasswordEncoder pe;
	
	
	
	
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
	
		return "forgot_email_form";
	}
	
	
	@PostMapping("/sendOtp")
	public String openEmailForm(@RequestParam("email") String email,Model m)
	{
		User u = ur.getUserByUserName(email);
		
		if(u == null)
		{
			m.addAttribute("msg",new Message("register","alert-danger"));
			System.out.println("not sent invalid user");
			return "forgot_email_form";
		}
		
		
		
		System.out.println("Emal :"+email);
		int otp =1000+ new Random().nextInt(9000);
		System.out.println("opt :"+otp);
	
		
		String passwordResetEmail = """
		        <html>
		            <head>
		                <style>
		                    body {
		                        background: #f4f4f4; /* Light background color */
		                    }

		                    .email-container {
		                        max-width: 600px;
		                        margin: 50px auto;
		                        padding: 20px;
		                        background: #ffffff;
		                        border-radius: 10px;
		                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		                    }

		                    h2 {
		                        color: #333333; /* Dark text color */
		                    }

		                    p {
		                        color: #666666; /* Medium dark text color */
		                    }

		                    .otp-container {
		                        margin-top: 20px;
		                        padding: 15px;
		                        background: #3498db; /* Blue background color for OTP */
		                        border-radius: 5px;
		                        text-align: center;
		                        font-size: 24px;
		                        color: #ffffff; /* White text color */
		                    }

		                    /* Add more styles as needed */
		                </style>
		            </head>
		            <body>
		                <div class="email-container">
		                    <h2>Password Reset Request</h2>

		                    <p>
		                        Dear User,
		                    </p>

		                    <p>
		                        We received a request to reset your password. To proceed, use the following One-Time Password (OTP):
		                    </p>

		                    <div class="otp-container">
		                        Generated OTP : %s
		                    </div>

		                    <p>
		                        If you did not request a password reset, please ignore this email.
		                    </p>

		                    <p>
		                        Regards,
		                        <br>
		                        SmartContactManager Team
		                    </p>
		                </div>
		            </body>
		        </html>
		    """.formatted(otp);

	
		
		
		boolean b =this.es.sendMail(email,  "OTP for SmartContactManager", passwordResetEmail);
		if(b)
		{
			System.out.println("otp sent to user email valid for 30 seconds only");
			m.addAttribute("otp",String.valueOf(otp));
			m.addAttribute("email",email);

			return "verify_otp";
		}
		
		m.addAttribute("msg",new Message("someting went wrong please try again!!","alert-danger"));
		System.out.println("not sent something went wrong in our side!!");

		return "forgot_email_form";
	}
	
	@PostMapping("/verify_otp")
	public String Verifyin_otp(@RequestParam("otp") String otp,@RequestParam("enteredOtp") String enteredOtp,@RequestParam("email") String email,Model m)
	{
		System.out.println("otp: "+otp);
		System.out.println("entered otp :"+enteredOtp);
		System.out.println("email :"+email);
		System.out.println("equal :"+otp.equals(enteredOtp));
		if(otp.equals(enteredOtp))
		{
			m.addAttribute("email",email);
			return "change_password";

		}
		m.addAttribute("msg",new Message("wrong otp entered","alert-danger"));
		m.addAttribute("otp",otp);
		m.addAttribute("email",email);
System.out.println(" wrong otp entered");
		return "verify_otp";
	}
	
	@PostMapping("/Change_password")
	public String changepass(@RequestParam("password") String pass,@RequestParam("email") String email,Model m)
	{
//		User u = ur.getUserByUserName(email);
		
		System.out.println("______________");
		System.out.println(email);
		System.out.println(pass);
		System.out.println(ur.getUserByUserName(email));
		System.out.println("saving new password..");
		User u = ur.getUserByUserName(email);
		u.setPassword(pe.encode(pass));
		ur.save(u);
		
		System.out.println("______________");

		m.addAttribute("title","login");
		m.addAttribute("msg",new Message("Password Reset Successful","type"));
		return "login";
	}

}
