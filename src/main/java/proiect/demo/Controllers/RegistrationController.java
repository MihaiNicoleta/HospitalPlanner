package proiect.demo.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.User;


@Controller
public class RegistrationController {

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String submitRegistrationForm(@ModelAttribute User user, Model model) {
        // Process the registration form submission
        return "registrationSuccess";
    }
}
