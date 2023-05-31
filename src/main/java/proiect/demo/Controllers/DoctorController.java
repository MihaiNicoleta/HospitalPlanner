package proiect.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.Department;
import proiect.demo.Domain.Doctor;
import proiect.demo.Services.DepartmentService;
import proiect.demo.Services.DoctorService;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DepartmentService departmentService;

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

    @GetMapping("/department/{departmentId}")
    public String getDoctorsByDepartment(@PathVariable Integer departmentId, Model model) {
        List<Doctor> doctorList = doctorService.getDoctorsByDepartment(departmentId);
        System.out.println("Number of doctors: " + doctorList.size());
        model.addAttribute("doctorList", doctorList);
        Department department = departmentService.getDepartmentById(departmentId).get();
        System.out.println(department.getName());
        if (department != null) {
            model.addAttribute("departmentName", department.getName());
        } else {
            // afișați un mesaj de eroare dacă departamentul este invalid
            model.addAttribute("departmentName", "Invalid department");
        }

        return "doctors_op";
    }
    @GetMapping("/app")
    public String app() {
        return "app";
    }


}
