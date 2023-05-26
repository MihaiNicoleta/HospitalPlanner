package proiect.demo.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleId implements Serializable {

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "day_of_the_week")
    private String dayOfWeek;

}
