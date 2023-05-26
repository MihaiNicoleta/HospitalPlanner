package proiect.demo.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Department;
import proiect.demo.Domain.Doctor;
import proiect.demo.Repostiories.DepartmentRepository;
import proiect.demo.Repostiories.DoctorRepository;
import proiect.demo.configs.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        System.out.println("\n\n2\n\n");
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Integer id) {
        return doctorRepository.findById(id);
    }


}
