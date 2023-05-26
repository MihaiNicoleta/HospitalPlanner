package proiect.demo.Services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Department;
import proiect.demo.Repostiories.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Integer id) {
        return departmentRepository.findById(id);
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Integer id, Department department) {
        Department existingDepartment = departmentRepository.findById(id).orElse(null);
        if (existingDepartment != null) {
            existingDepartment.setName(department.getName());
            return departmentRepository.save(existingDepartment);
        }
        return null;
    }

    public void deleteDepartmentById(Integer id) {
        departmentRepository.deleteById(id);
    }
}