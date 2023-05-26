package proiect.demo.Domain;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "availableslots")
public class AvailableSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Date day;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    public AvailableSlot(Doctor doctor, Date day, Timestamp startTime, Timestamp endTime) {
        this.doctor = doctor;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
