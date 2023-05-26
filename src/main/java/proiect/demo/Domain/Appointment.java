package proiect.demo.Domain;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;


    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String status;

    public Appointment(Doctor doctor, User patient, Timestamp startTime, Timestamp endTime, Date date, String status) {
        this.doctor = doctor;
        this.patient = patient;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.status = status;
    }
}
