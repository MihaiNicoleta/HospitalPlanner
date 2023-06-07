package proiect.demo.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Services.AuthenticationService;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    /**
     * Endpoint pentru crearea unui token de autentificare.
     *
     * @param request Obiectul de cerere care conține informațiile de autentificare.
     * @return Răspunsul HTTP care conține un obiect AuthenticationResponse ce conține tokenul generat,
     *         și starea 200 OK în caz de succes.
     */
    @PostMapping("/createtoken")
    public ResponseEntity<AuthenticationResponse> createToken(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    /**
     * Endpoint pentru autorizarea unui token de autentificare.
     *
     * @param authHeader Header-ul de autorizare care conține tokenul.
     * @return Răspunsul HTTP cu un corp gol și starea 200 OK dacă tokenul este valid,
     *         sau starea 401 Unauthorized și un mesaj de eroare dacă tokenul este invalid.
     */
    @PostMapping("/authorisetoken")
    public ResponseEntity<?> authoriseToken(
            @RequestHeader("Authorization") String authHeader) {
        try
        {
            if (service.authorise(authHeader)) return ResponseEntity.ok("Valid token");
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token : " + e.getLocalizedMessage());
        }
    }
}