package proiect.demo.Domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "cabinet_number")
    private Integer cabinetNumber;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    @OneToMany(mappedBy = "doctor")
    private List<AvailableSlot> availableSlots;
/*
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
    //un sched are zi si ore
    @OneToMany(mappedBy = "doctor")
    private List<Schedule> schedules;
    */
    public Doctor(String name, Department department, String email, String password, int cabinetNumber) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.password = password;
        this.cabinetNumber = cabinetNumber;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cabinetNumber=" + cabinetNumber +
                '}';
    }
}
