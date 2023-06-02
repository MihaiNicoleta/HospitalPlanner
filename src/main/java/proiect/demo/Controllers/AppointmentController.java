package proiect.demo.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proiect.demo.Domain.Appointment;
import proiect.demo.Domain.AppointmentDto;
import proiect.demo.Domain.Doctor;
import proiect.demo.Domain.User;
import proiect.demo.Services.AppointmentService;
import proiect.demo.Services.DoctorService;
import proiect.demo.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired

    private DoctorService doctorService;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment) {
        Appointment newAppointment = appointmentService.addAppointment(appointment);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id, @RequestBody Appointment appointment) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
