package proiect.demo.Domain;



import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.Doc;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    private int id;
    private String name;

    private String email;

    private String password;

    private int cabinetNumber;

    public DoctorDTO(Doctor doctor) {
        id = doctor.getId();
        name = doctor.getName();
        email = doctor.getEmail();
        password = doctor.getPassword();
        cabinetNumber = doctor.getCabinetNumber();

    }
}
