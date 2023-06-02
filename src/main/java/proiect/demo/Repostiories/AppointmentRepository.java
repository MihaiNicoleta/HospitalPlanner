package proiect.demo.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import proiect.demo.Domain.Appointment;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query(value = "SELECT * FROM appointments a WHERE a.doctor_id = :doctorId AND a.status = 'confirmed' and\n" +
            " ((a.start_time >= :startDateTime and a.start_time < :endDateTime) or \n" +
            "  (a.end_time > :startDateTime and a.end_time <= :endDateTime) or \n" +
            "  (a.start_time < :startDateTime and a.end_time > :endDateTime))",
            nativeQuery = true)
    List<Appointment> findByDoctorIdAndDateTime(int doctorId, Time startDateTime, Time endDateTime);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date")
    List<Appointment> findByDoctorIdAndDate(int doctorId, Date date);

}
