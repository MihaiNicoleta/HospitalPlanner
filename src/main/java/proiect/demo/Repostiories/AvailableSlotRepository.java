package proiect.demo.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import proiect.demo.Domain.Appointment;
import proiect.demo.Domain.AvailableSlot;


@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Integer> {

}
