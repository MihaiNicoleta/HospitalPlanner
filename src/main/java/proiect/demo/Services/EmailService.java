package proiect.demo.Services;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    /**
     * Construiește un serviciu de trimitere a e-mailurilor cu ajutorul unui JavaMailSender.
     *
     * @param javaMailSender JavaMailSender utilizat pentru a trimite e-mailurile.
     */
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Trimite un e-mail către destinatar.
     *
     * @param to      Adresa de e-mail a destinatarului.
     * @param subject Subiectul e-mailului.
     * @param body    Corpul e-mailului.
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("proiectip81@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        this.javaMailSender.send(message);
    }
}