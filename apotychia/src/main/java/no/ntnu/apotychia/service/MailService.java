package no.ntnu.apotychia.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.HashSet;
import java.util.Set;

import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    GroupRepository groupRepository;

	
	private static String USER_NAME = "apotychia.ntnu";
	private static String PASSWORD = "1337apotychia";
	public  boolean push(Set<User> adress, String content) {
		String from = USER_NAME;
		String pass = PASSWORD;
		HashSet<User> to = (HashSet<User>)adress;
		String subject = "You have been invited to an Event";
		String body = content;
		
		if(sendFromGmail(from, pass, to, subject, body)) return true;
		else return false;
		
	}
	
	
	private boolean sendFromGmail(String from, String pass, HashSet<User> to, String subject, String body) {
		Properties properties = System.getProperties();
		String host = "smtp.gmail.com";
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", pass);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		
		try {
			
			message.setFrom(new InternetAddress(from));
			List<InternetAddress> toAdress = new ArrayList<InternetAddress>();
			
       	    for (User user : to) {
                toAdress.add(new InternetAddress(user.getEmail()));
            }
            
			
			for (InternetAddress internetAddress : toAdress) {
                message.addRecipient(Message.RecipientType.TO, internetAddress);
            }
			message.setSubject(subject);
			message.setText(body, "UTF-8", "html");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
            if (message.getAllRecipients().length > 0)
			    transport.sendMessage(message, message.getAllRecipients());
			transport.close();


		}
		
		catch (AddressException ae) {
			ae.printStackTrace();
			return false;
		}
		catch (MessagingException me) {
			me.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	
}
