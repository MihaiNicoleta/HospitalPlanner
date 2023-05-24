package proiect.demo.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.User;
import proiect.demo.Services.UserService;
import proiect.demo.models.AuthenticationRequest;
import proiect.demo.models.AuthenticationResponse;
import proiect.demo.models.RegisterRequest;
import proiect.demo.models.RegisterResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    )
    {
        try {
            AuthenticationResponse authenticationResponse = userService.login(request);
            return ResponseEntity.ok(authenticationResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    )
    {
        try {
            RegisterResponse registerResponse = userService.register(request);
            return ResponseEntity.ok(registerResponse);
        }
        catch (DataIntegrityViolationException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exists");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllPatients() {
        List<User> patients = userService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getPatientById(@PathVariable int id) {
        User patient = userService.getPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
}