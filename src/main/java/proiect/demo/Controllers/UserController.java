package proiect.demo.Controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import proiect.demo.Domain.User;
import proiect.demo.Domain.UserDto;
import proiect.demo.Services.JwtService;
import proiect.demo.Services.UserService;
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


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/index")
    public String home() {
        return "index"; // returnați numele fișierului HTML pentru pagina de pornire
    }

    @PostMapping(value = "/login2")
    public String login(@ModelAttribute AuthenticationRequest request, Model model, HttpSession session) {
        try {
            AuthenticationResponse authenticationResponse = userService.login(request);
            String token = authenticationResponse.getToken();
            Integer userId = null;
            try {
                userId = jwtService.extractUserId(token);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            session.setAttribute("userId", userId);
            System.out.println("AICI " + userId);
            return "redirect:/index"; // Redirect to home page
        } catch (Exception e) {
                        model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/users/login";
        }
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid User userDto, BindingResult result, Model model) {
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
            RegisterResponse registerResponse = userService.register(registerRequest);
            return "redirect:/users/login?registerSuccess=true";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "User already exists");
            return "register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }


}
