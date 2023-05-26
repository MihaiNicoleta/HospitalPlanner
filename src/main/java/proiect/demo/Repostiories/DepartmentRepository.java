package proiect.demo.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;
import proiect.demo.Domain.Department;
import proiect.demo.Domain.User;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findById(Integer id);
}
