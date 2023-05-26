package proiect.demo.Domain;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 32)
    private String password;

    // getters and setters

}
