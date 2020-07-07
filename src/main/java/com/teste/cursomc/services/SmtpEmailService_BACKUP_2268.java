package com.teste.cursomc.services;

<<<<<<< HEAD
import javax.mail.internet.MimeMessage;

=======
>>>>>>> 41566890a43c72b2e390d52bb10b63070d958d5e
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
<<<<<<< HEAD
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javaMailSender;
=======

public class SmtpEmailService extends AbstractEmailService{

	@Autowired
	MailSender mailSender;
>>>>>>> 41566890a43c72b2e390d52bb10b63070d958d5e
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
<<<<<<< HEAD
		LOG.info("Enviando email...");
		mailSender.send(msg);
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email html...");
		javaMailSender.send(msg);
		LOG.info("Email enviado");
	}
	
	
}
=======
		LOG.info("Enviando Email...");
		LOG.info(msg.toString());
		mailSender.send(msg);
		LOG.info("Email Enviado!");
		
	}

}
>>>>>>> 41566890a43c72b2e390d52bb10b63070d958d5e
