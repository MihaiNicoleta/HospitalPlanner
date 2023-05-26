package proiect.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.Department;
import proiect.demo.Domain.Doctor;
import proiect.demo.Services.DoctorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
       // System.out.println("\n1\n");
        List<Doctor> doctors = doctorService.getAllDoctors();
        //System.out.println("\n3\n");

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Doctor>> getDoctorById(@PathVariable Integer id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }




}
