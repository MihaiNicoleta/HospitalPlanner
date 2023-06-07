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
    /**
     * Returnează doctorul cu ID-ul specificat.
     *
     * @param id ID-ul doctorului.
     * @return Doctorul cu ID-ul specificat.
     * @throws ResourceNotFoundException dacă doctorul nu poate fi găsit.
     */
    public Doctor getDoctorById(int id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + id + " not found"));
    }
    /**
     * Returnează doctorul cu adresa de e-mail specificată.
     *
     * @param email Adresa de e-mail a doctorului.
     * @return Doctorul cu adresa de e-mail specificată.
     */
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    /**
     * Returnează o listă cu toți doctorii.
     *
     * @return Lista cu toți doctorii.
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Returnează o listă cu doctorii dintr-un anumit departament.
     *
     * @param departmentId ID-ul departamentului.
     * @return Lista cu doctorii din departamentul specificat.
     */
    public List<Doctor> getDoctorsByDepartment(Integer departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }
}