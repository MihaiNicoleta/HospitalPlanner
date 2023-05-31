package proiect.demo.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proiect.demo.Domain.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findAll();
    List<Doctor> findByDepartmentId(Integer departmentId);

}