package proiect.demo.Domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private int doctorId;

    private Timestamp startDateTime;

    private Timestamp endDateTime;


}
