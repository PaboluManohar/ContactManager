package com.smart.service;

import java.io.File;
import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport; 
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class EmailService {
	public boolean sendMail(String to, String subject, String text) {
		boolean flag = false;

		String from = "pabolumanohar1@gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.gmail.com");

		// session
		Session session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("mailtesting7670", "ftak ymkv dzkn judk");
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(subject);

			MimeMultipart mmp = new MimeMultipart();
			MimeBodyPart p1 = new MimeBodyPart();
			p1.setContent(text, "text/html; charset=utf-8");
			mmp.addBodyPart(p1);
			msg.setContent(mmp);

			Transport.send(msg);
			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}
}

























//public boolean sendMail(String to, String from, String subject, String text, File f) {
//	boolean flag = false;
//
//	Properties props = new Properties();
//	props.put("mail.smtp.auth", true);
//	props.put("mail.smtp.starttls.enable", true);
//	props.put("mail.smtp.port", "587");
//	props.put("mail.smtp.host", "smtp.gmail.com");
//
//	// session
//	Session session = Session.getInstance(props, new Authenticator() {
//
//		@Override
//		protected PasswordAuthentication getPasswordAuthentication() {
//
//			return new PasswordAuthentication("pabolumanohar1", "qame vzyg erhe geuk");
//		}
//	});
//
//	try {
//		Message msg = new MimeMessage(session);
//		msg.setFrom(new InternetAddress(from));
//		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//		msg.setSubject(subject);
//
//		MimeBodyPart p1 = new MimeBodyPart();
//		p1.setContent(text, "text/html; charset=utf-8");
//		MimeBodyPart p2 = new MimeBodyPart();
//		p2.attachFile(f);
//		MimeMultipart mmp = new MimeMultipart();
//		mmp.addBodyPart(p1);
//		mmp.addBodyPart(p2);
//
//		msg.setContent(mmp);
//		
//
//		Transport.send(msg);
//		flag = true;
//
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//
//	return flag;
//}
