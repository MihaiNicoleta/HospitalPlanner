package proiect.demo.Domain;

import lombok.*;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "schedules")
public class Schedule {

    @EmbeddedId
    private ScheduleId id;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @ManyToOne
    @MapsId("doctorId")
    @JoinColumn(name = "doctor_id",  insertable = false, updatable = false)
    private Doctor doctor;

    public Schedule(ScheduleId id, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
