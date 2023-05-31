package proiect.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.Doctor;
import proiect.demo.Services.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @GetMapping("/afisareDoctori")
    public String showDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "afisare_doctori";
    }

}
