package proiect.demo.Controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proiect.demo.Domain.*;
import proiect.demo.Services.*;
import proiect.demo.configs.ApplicationConfig;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;
import proiect.demo.models.RegisterRequest;
import proiect.demo.models.RegisterResponse;

import java.sql.Time;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final ApplicationConfig applicationConfig;
    private final AppointmentService appointmentService;

    @GetMapping("/login2")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/app")
    public String app() {
        return "app";
    }

    @GetMapping("/pacient_menu")
    public String pacient_menu() {
        return "pacient_menu";
    }

    @GetMapping("pacient_menu/find_doctor")
    public String find_doctor() {
        return "find_doctor";
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
            model.addAttribute("userId", user.getId());
            return "redirect:/users/pacient_menu";
        }
    }


    @PostMapping("/pacient_menu/appointment")
    public String saveAppointment(@ModelAttribute @Valid Appointment appointment, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println("AICI 1");
        Integer patientId = (Integer) session.getAttribute("userId");

        // Verificați dacă pacientul este autentificat
        if (patientId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nu sunteți autentificat!");
            return "redirect:/users/login2";
        }

        // Setați ID-ul pacientului în obiectul appointment
        appointment.setPatient(userService.getPatientById(patientId));
        if (result.hasErrors()) {
            System.out.println("Erori de validare: " + result.getAllErrors());
            return "appointment";
        }
        try {
            System.out.println("AIci 2");
            appointmentService.save(appointment, session);
            redirectAttributes.addFlashAttribute("successMessage", "Programarea a fost salvată cu succes!");
            return "redirect:/users/pacient_menu";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "A apărut o eroare la salvarea programării: " + e.getMessage());
            return "redirect:/users/pacient_menu/appointment";
        }
    }

    @GetMapping("/pacient_menu/appointment")
    public String showAppointmentForm(Model model) {
        List<Department> departmentList = departmentService.getAllDepartments();
        model.addAttribute("departmentList", departmentList);
        List<Doctor> doctorList = doctorService.getAllDoctors();
        model.addAttribute("doctorList", doctorList);
        return "appointment";
    }

    @GetMapping("pacient_menu/find_doctor/result")
    public String getDoctorsByDepartment(@RequestParam("department") String departmentSlug, Model model) {
        // obțineți id-ul departamentului din slug
        Integer departmentId = null;
        switch (departmentSlug) {
            case "cardiology":
                departmentId = 10;
                break;
            case "dermatology":
                departmentId = 11;
                break;
            case "endocrinology":
                departmentId = 12;
                break;
            case "gastroenterology":
                departmentId = 13;
                break;
        }

        // verificați dacă s-a reușit obținerea id-ului departamentului
        if (departmentId != null) {
            // redirecționați utilizatorul către pagina cu doctorii pentru acel departament
            return "redirect:/doctors/department/" + departmentId;
        } else {
            // afișați o eroare
            model.addAttribute("error", "Cannot find doctors for selected department!");
            return "find_doctor";
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
            return "redirect:/users/login2";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "User already exists");
            return "redirect:/users/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/register";
        }
    }
}
