package proiect.demo.Services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import proiect.demo.Repostiories.UserRepository;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;

    /**
     * Autentifică utilizatorul și generează un token JWT în cazul în care autentificarea este reușită.
     *
     * @param request Cererea de autentificare care conține adresa de e-mail și parola utilizatorului.
     * @return Răspunsul de autentificare care conține token-ul JWT generat.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()
                )
        );
        System.out.println(1);
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken)
                .build();
    }
    /**
     * Verifică validitatea unui token JWT.
     *
     * @param authHeader Antetul de autorizare care conține token-ul JWT.
     * @return `true` dacă token-ul este valid, altfel `false`.
     */
    public boolean authorise(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);

        return jwtService.authorizeToken(token);
    }
}