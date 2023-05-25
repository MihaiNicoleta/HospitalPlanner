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

    @PostMapping("/createtoken")
    public ResponseEntity<AuthenticationResponse> createToken(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

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