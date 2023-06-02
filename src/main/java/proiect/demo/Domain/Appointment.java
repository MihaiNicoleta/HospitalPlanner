package proiect.demo.Domain;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "start_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(nullable = false)
    private String status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
