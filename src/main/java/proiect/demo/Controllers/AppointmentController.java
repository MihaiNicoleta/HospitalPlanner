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

    /**
     * Endpoint pentru obținerea tuturor programărilor.
     *
     * @return Răspunsul HTTP care conține o listă cu toate programările și starea 200 OK în caz de succes.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Endpoint pentru obținerea unei programări după ID.
     *
     * @param id ID-ul programării.
     * @return Răspunsul HTTP care conține programarea și starea 200 OK în caz de succes.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    /**
     * Endpoint pentru adăugarea unei noi programări.
     *
     * @param appointment Obiectul de programare care trebuie adăugat.
     * @return Răspunsul HTTP care conține programarea adăugată și starea 201 CREATED în caz de succes.
     */
    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment) {
        Appointment newAppointment = appointmentService.addAppointment(appointment);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    /**
     * Endpoint pentru actualizarea unei programări existente.
     *
     * @param id          ID-ul programării care trebuie actualizată.
     * @param appointment Obiectul de programare cu noile date.
     * @return Răspunsul HTTP care conține programarea actualizată și starea 200 OK în caz de succes.
     */
    @PostMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id, @RequestBody Appointment appointment) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    /**
     * Endpoint pentru ștergerea unei programări.
     *
     * @param id ID-ul programării care trebuie ștearsă.
     * @return Răspunsul HTTP cu un corp gol și starea 204 NO CONTENT în caz de succes.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
