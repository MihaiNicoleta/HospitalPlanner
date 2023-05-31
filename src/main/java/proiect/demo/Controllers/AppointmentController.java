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

    @PostMapping("/appointments")
    public String createAppointment(@ModelAttribute("appointment") AppointmentDto dto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    HttpSession session) {

        /* Verificați validitatea datelor primite în apelul la metoda.
           Amarcați erorile pe obiectul BindingResult, în cazul în care sunt găsite. */

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "appointment";
        }

        /* Extract un id utilizatorului din HttpSession */
        int userId = (int) session.getAttribute("userId");
        User user = userService.getPatientById(userId);

        // Extract the doctor
        Doctor doctor = doctorService.getDoctorById(dto.getDoctorId());

        /* Verificați dacă intervalul orar specificat este disponibil pentru medic */
        try {
            appointmentService.checkSlotAvailability(doctor.getId(), dto.getStartDateTime(), dto.getEndDateTime());
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "appointment";
        }

        /* Creați o nouă programare */
        Appointment appointment = new Appointment();
        appointment.setPatient(user);
        appointment.setDoctor(doctor);
        appointment.setDate(dto.getStartDateTime());
        appointment.setStartTime(dto.getStartDateTime());
        appointment.setEndTime(dto.getEndDateTime());
        appointment.setStatus("confirmed");

        /* Salvați noua programare în baza de date */
        appointmentService.save(appointment);

        redirectAttributes.addFlashAttribute("successMessage", "Programare creată cu succes");

        return "redirect:/users/dashboard";
    }
}
