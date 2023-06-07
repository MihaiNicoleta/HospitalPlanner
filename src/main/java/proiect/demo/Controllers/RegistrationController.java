package proiect.demo.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.User;

/**
 * Această clasă este un controller responsabil de gestionarea înregistrării utilizatorilor.
 */
@Controller

public class RegistrationController {
    /**
     * Returnează vizualizarea pentru formularul de înregistrare.
     *
     * @param model Obiectul Model.
     * @return Numele vizualizării pentru formularul de înregistrare.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    /**
     * Procesează formularul de înregistrare și returnează vizualizarea pentru succesul înregistrării.
     *
     * @param user  Obiectul User cu datele utilizatorului.
     * @param model Obiectul Model.
     * @return Numele vizualizării pentru succesul înregistrării.
     */
    @PostMapping("/register")
    public String submitRegistrationForm(@ModelAttribute User user, Model model) {
        return "registrationSuccess";
    }
}
