package proiect.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import proiect.demo.Services.EmailService;
import proiect.demo.models.EmailRequest;

@RestController
public class EmailController {
    private final EmailService emailService;
    /**
     * Constructor pentru EmailController.
     *
     * @param emailService Serviciul de trimitere a emailurilor.
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    /**
     * Endpoint pentru trimiterea unui email.
     *
     * @param request Obiectul EmailRequest care conține detalii despre email (destinatar, subiect, conținut).
     * @return Răspunsul HTTP care indică rezultatul trimiterii emailului.
     *         - Dacă emailul a fost trimis cu succes, se returnează un răspuns cu starea 200 OK și un mesaj de succes.
     *         - Dacă a apărut o eroare în timpul trimiterii emailului, se returnează un răspuns cu starea 500 Internal Server Error și un mesaj de eroare.
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
}