package com.db.heistgame.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.db.heistgame.model.Member;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender emailSend;

 
    public void sendEmails(String subject, String text, List<Member> members) {
    	for (Member member : members) {
			sendEmail(member.getEmail(), subject, text);
		}
    }
    
    
    public void sendEmail(String to, String subject, String text) {       
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("<team_name>@ag04.io");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSend.send(message);        
    }
}