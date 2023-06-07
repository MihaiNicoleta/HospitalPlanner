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
import java.util.List;

/**
 * Clasa controller care se ocupă de operațiile și rutele legate de pacienti.
 */
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
    private final EmailService emailService;
    /**
     * Returnează vizualizarea formularului de autentificare.
     *
     * @return Numele vizualizării formularului de autentificare.
     */
    @GetMapping("/login2")
    public String showLoginForm() {
        return "login";
    }
    /**
     * Returnează vizualizarea meniului pentru pacient.
     *
     * @return Numele vizualizării meniului pentru pacient.
     */
    @GetMapping("/pacient_menu")
    public String pacient_menu() {
        return "pacient_menu";
    }
    /**
     * Returnează vizualizarea pentru găsirea doctorului.
     *
     * @return Numele vizualizării pentru găsirea doctorului.
     */
    @GetMapping("pacient_menu/find_doctor")
    public String find_doctor() {
        return "find_doctor";
    }

    @GetMapping("/index")
    public String home() {
        return "index"; // return the name of the HTML file for the home page
    }

    @GetMapping("/pacient_menu/appointment/freeAppointments")
    public String free_appointments() {
        return "free_appointments";
    }
    /**
     * Gestionează cererea de autentificare.
     *
     * @param request Obiectul AuthenticationRequest.
     * @param model   Obiectul Model.
     * @param session Obiectul HttpSession.
     * @return Numele vizualizării pentru autentificarea cu succes sau eroare de autentificare.
     */
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
    /**
     * Obține programările disponibile pentru un anumit departament.
     *
     * @param departmentId ID-ul departamentului.
     * @param model        Obiectul Model.
     * @return Numele vizualizării pentru programările disponibile.
     */
    @PostMapping("/pacient_menu/appointment/freeAppointments")
    public String getFreeAppointments(@RequestParam("departmentId") int departmentId, Model model) {
        List<Appointment> freeAppointments = appointmentService.getFreeAppointments(departmentId);
        model.addAttribute("freeAppointments", freeAppointments);
        return "free_appointments";
    }


    @PostMapping("/pacient_menu/appointment")
    public String saveAppointment(@ModelAttribute @Valid Appointment appointment, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        Integer patientId = (Integer) session.getAttribute("userId");
        // vad daca pacientul e logat
        if (patientId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nu sunteți autentificat!");
            return "redirect:/users/login2";
        }
        appointment.setPatient(userService.getPatientById(patientId));
        if (result.hasErrors()) {
            System.out.println("Erori de validare: " + result.getAllErrors());
            return "appointment";
        }
        try {
            appointmentService.save(appointment, session);
            redirectAttributes.addFlashAttribute("successMessage", "Programarea a fost salvată cu succes!");
            User user = userService.getPatientById(patientId);
            String mail = user.getEmail();
            String message = "Buna ziua " + user.getFirstName() + " !" + "\n" +
                    "Multumim ca ai apelat la noi!" + "\n" + "Acestea sunt detaliile programarii tale "

                    + "\n" + "Doctor: " + appointment.getDoctor().getName() + "\n" +
                    "Data: " + appointment.getDate() + "\n" + "Ora incepere: " + appointment.getStartTime()
                    + "\n" + appointment.getEndTime() + "\n" + "Cabinet: " + appointment.getDoctor().getCabinetNumber();
            emailService.sendEmail(mail, "Detalii programare medic", message);
            return "redirect:/users/pacient_menu/appointment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "A apărut o eroare la salvarea programării: " + e.getMessage());
            return "redirect:/users/pacient_menu/appointment";
        }
    }
    /**
     * Returnează vizualizarea pentru formularul de programare a unei consultații.
     *
     * @param model Obiectul Model.
     * @return Numele vizualizării pentru formularul de programare a unei consultații.
     */
    @GetMapping("/pacient_menu/appointment")
    public String showAppointmentForm(Model model) {
        List<Department> departmentList = departmentService.getAllDepartments();
        model.addAttribute("departmentList", departmentList);
        List<Doctor> doctorList = doctorService.getAllDoctors();
        model.addAttribute("doctorList", doctorList);
        return "appointment";
    }
    /**
     * Obțineți doctorii pentru un anumit departament și afișează rezultatul.
     *
     * @param departmentSlug Slug-ul departamentului.
     * @param model          Obiectul Model.
     * @return Numele vizualizării cu doctorii pentru departamentul selectat sau vizualizarea de eroare.
     */
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
        if (departmentId != null) {
            return "redirect:/doctors/department/" + departmentId;
        } else {
            model.addAttribute("error", "Cannot find doctors for selected department!");
            return "find_doctor";
        }
    }
    /**
     * Returnează vizualizarea pentru formularul de înregistrare a utilizatorului.
     *
     * @param model Obiectul Model.
     * @return Numele vizualizării pentru formularul de înregistrare a utilizatorului.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    /**
     * Procesează cererea de înregistrare a utilizatorului.
     *
     * @param userDto            Obiectul User cu datele utilizatorului.
     * @param result             Obiectul BindingResult pentru validarea datelor.
     * @param redirectAttributes Obiectul RedirectAttributes pentru a seta atributele pentru redirecție.
     * @return Numele vizualizării pentru autentificarea cu succes sau vizualizarea de înregistrare cu erori.
     */
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
