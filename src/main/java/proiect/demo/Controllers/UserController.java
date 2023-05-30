package proiect.demo.Controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proiect.demo.Domain.User;
import proiect.demo.Domain.UserDto;
import proiect.demo.Services.JwtService;
import proiect.demo.Services.UserService;
import proiect.demo.configs.ApplicationConfig;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;
import proiect.demo.models.RegisterRequest;
import proiect.demo.models.RegisterResponse;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;

    @GetMapping("/login2")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/app")
    public String app() {
        return "app";
    }

    @GetMapping("/index")
    public String home() {
        return "index"; // return the name of the HTML file for the home page
    }

    @PostMapping(value = "/login2")
    public String login(@ModelAttribute AuthenticationRequest request, Model model, HttpSession session) {
        AuthenticationResponse authenticationResponse = userService.login(request);
        String token = authenticationResponse.getToken();
        Integer userId = null;
        try {
            userId = jwtService.extractUserId(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        session.setAttribute("userId", userId);

        User user = userService.getPatientById(userId);

        // Compare hashed passwords
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            model.addAttribute("error", true);
            System.out.println("Login failed. Error message should be shown on the page.");
            return "login";
        } else {
            model.addAttribute("email", user.getEmail());
            return "redirect:/users/app";
        }
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid User userDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            // Convert UserDto to RegisterRequest
            RegisterRequest registerRequest = RegisterRequest.builder()
                    .username(userDto.getUsername())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .phone(userDto.getPhone())
                    .address(userDto.getAddress())
                    .build();

            userService.register(registerRequest);
            redirectAttributes.addFlashAttribute("registerSuccess", true);
            return "redirect:/users/register";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "User already exists");
            return "redirect:/users/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/register";
        }
    }


}
