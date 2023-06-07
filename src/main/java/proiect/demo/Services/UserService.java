package proiect.demo.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Doctor;
import proiect.demo.Domain.LoginForm;
import proiect.demo.Domain.User;
import proiect.demo.Repostiories.UserRepository;
import proiect.demo.configs.ResourceNotFoundException;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;
import proiect.demo.models.RegisterRequest;
import proiect.demo.models.RegisterResponse;

import java.util.List;

import static org.checkerframework.checker.nullness.Opt.orElseThrow;


@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationService authService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Autentifică utilizatorul și returnează un token de autentificare.
     *
     * @param request Cererea de autentificare care conține email-ul și parola utilizatorului.
     * @return Răspunsul care conține token-ul de autentificare.
     */
    public AuthenticationResponse login(AuthenticationRequest request) {

        UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        System.out.println(request.getEmail() + " " +
                request.getPassword());

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginForm loginForm) {
        String email = loginForm.getUsername();
        String password = loginForm.getPassword();

        UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        authenticationManager.authenticate(upaToken);

        var user = userRepository.findByEmail(email).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    /**
     * Înregistrează un nou utilizator și returnează un token de autentificare.
     *
     * @param request Cererea de înregistrare care conține informațiile utilizatorului.
     * @return Răspunsul care conține token-ul de autentificare.
     */
    public RegisterResponse register(RegisterRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        var savedUser = userRepository.save(user);
            /*
            var jwtToken = jwtService.generateToken(savedUser);
            */

        var jwtToken = login(AuthenticationRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build()
        ).getToken();

        return RegisterResponse.builder()
                .token(jwtToken)
                .build();
    }
    /**
     * Returnează o listă cu toți pacienții.
     *
     * @return Lista cu toți pacienții.
     */
    public List<User> getAllPatients() {
        return userRepository.findAll();
    }
    /**
     * Returnează un pacient după ID.
     *
     * @param id ID-ul pacientului.
     * @return Pacientul găsit.
     * @throws ResourceNotFoundException dacă pacientul nu există.
     */
    public User getPatientById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + id + " not found"));
    }

}