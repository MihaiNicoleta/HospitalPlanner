package proiect.demo.Controllers;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.*;
import proiect.demo.Services.AppointmentService;
import proiect.demo.Services.DepartmentService;
import proiect.demo.Services.DoctorService;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.*;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DepartmentService departmentService;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @GetMapping("/login")
    public String doctor_login() {
        return "doctor_login";
    }
    @GetMapping("/appointments")
    public String allAppointments(Model model, HttpSession session) {
        int doctorId = (int) session.getAttribute("doctorId");
        List<Appointment> appointments = appointmentService.findByDoctorId(doctorId);
        model.addAttribute("appointments", appointments);
        return "appointments";
    }


    @GetMapping("/patients_contact")
    public String doctorPatientsContact(Model model, HttpSession session) {
        int doctorId = (int) session.getAttribute("doctorId");
        List<Appointment> appointments = appointmentService.findByDoctorId(doctorId);

        List<PatientContactInfo> patients = appointments.stream()
                .map(appointment -> new PatientContactInfo(
                        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PatientContactInfo::getName))),
                        ArrayList::new));

        System.out.println(patients);

        model.addAttribute("patients", patients);
        return "patients_contact";
    }



    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        Doctor doctor = doctorService.getDoctorByEmail(email);
        if (doctor == null || !doctor.getPassword().equals(password)) {
            model.addAttribute("error", true);
            return "doctor_login";
        } else {
            int doctorId = doctor.getId();
            session.setAttribute("doctorId", doctorId);
            return "redirect:/doctors/doctor_menu";
        }
    }


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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    @GetMapping("/doctor_menu")
    public String pacient_menu() {
        return "doctor_menu";
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

    @GetMapping("/afisareDoctori")
    public String showDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "afisare_doctori";
    }

}