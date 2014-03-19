package no.ntnu.apotychia.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.HashSet;
import java.util.Set;

import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;

public class MailService {

	
	private static String USER_NAME = "apotychia.ntnu";
	private static String PASSWORD = "1337apotychia";
	public  boolean push(Set<Participant> adress, String content) {
		String from = USER_NAME;
		String pass = PASSWORD;
		HashSet<Participant> to = (HashSet<Participant>)adress;
		String subject = "You have been invited to an Event";
		String body = content;
		
		if(sendFromGmail(from, pass, to, subject, body)) return true;
		else return false;
		
	}
	
	
	private boolean sendFromGmail(String from, String pass, HashSet<Participant> to, String subject, String body) {
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
			InternetAddress[] toAdress = new InternetAddress[to.size()];
			int count = 0;
			for(Participant p : to) {
				toAdress[count] = new InternetAddress(((User)p).getEmail());
				count++;
			}
			
			
			for(int i = 0; i < toAdress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAdress[i]);
			}
			
			message.setSubject(subject);
			message.setText(body, "UTF-8", "html");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
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
