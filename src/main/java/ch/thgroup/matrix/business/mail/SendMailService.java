package ch.thgroup.matrix.business.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class SendMailService {
    private final MailGeneratorService mailGeneratorService;
    private final JavaMailSender emailSender;
    private final String senderAddress;
    private final String ENCODING = "utf-8";

	SendMailService(MailGeneratorService mailGeneratorService,
					@Value("${common.mail.smtp.host}") final String smtpHost,
					@Value("${common.mail.smtp.port:25}") final int smtpPort,
					@Value("${common.mail.smtp.authentication:false}") final boolean smtpAuthentication,
					@Value("${common.mail.smtp.username:null}") final String smtpUsername,
					@Value("${common.mail.smtp.password:null}") final String smtpPassword,
					@Value("${common.mail.smtp.tls:false}") final boolean smtpTls,
					@Value("${common.mail.sender}") final String senderAddress) {
		this.mailGeneratorService = mailGeneratorService;

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);

		if (smtpAuthentication) {
			mailSender.setUsername(smtpUsername);
			mailSender.setPassword(smtpPassword);
		}

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", smtpAuthentication);
		props.put("mail.smtp.starttls.enable", smtpTls);

		this.emailSender = mailSender;
		this.senderAddress = senderAddress;
	}

	@Async
	public void sendMail(MailType type, Map<String, String> placeholders, String receiver, Language language) {
		log.info("Sending e-mail "
				+ "\n TemplateName: " + type.getTemplateName()
				+ "\n Receiver: " + receiver);

		final SimpleMailMessage message = mailGeneratorService.generatePartial(type, language, placeholders);
		sendSimpleMailMessage(receiver, message);
	}

    public void sendSimpleMailMessage(String receiver, SimpleMailMessage message) {
        try {
			log.info("Sending simple e-mail "
					+ "\n Subject: " + message.getSubject()
					+ "\n Receiver: " + receiver);

			final MimeMessage mimeMessage = emailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODING);

			helper.setSubject(message.getSubject());
			helper.setText(message.getText(), true); // html = true
			helper.setFrom(senderAddress);
			helper.setTo(receiver);

            emailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            log.error("Could not send mail", e);
        }
    }

    public void sendMailSynchronous(MailType type, Map<String,String> placeholders, String receiver, Language language)
            throws MessagingException {
        final SimpleMailMessage message = mailGeneratorService.generatePartial(type, language, placeholders);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODING);
        helper.setSubject(message.getSubject());
        helper.setText(message.getText(), true); // html = true
        helper.setFrom(senderAddress);
        helper.setTo(receiver);
        emailSender.send(mimeMessage);
    }
}
