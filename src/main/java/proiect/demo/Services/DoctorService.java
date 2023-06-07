package proiect.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Doctor;
import proiect.demo.Repostiories.DoctorRepository;
import proiect.demo.configs.ResourceNotFoundException;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor getDoctorById(int id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + id + " not found"));
    }

    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }


    public List<Doctor> getDoctorsByDepartment(Integer departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }
}