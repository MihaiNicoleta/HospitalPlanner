package proiect.demo.Services;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proiect.demo.Domain.Appointment;
import proiect.demo.Domain.AvailableSlot;
import proiect.demo.Domain.User;
import proiect.demo.Repostiories.AppointmentRepository;
import proiect.demo.Repostiories.AvailableSlotRepository;
import proiect.demo.configs.ResourceNotFoundException;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    private AvailableSlotRepository availableSlotsRepository;

    @Autowired
    private AppointmentRepository appointmentsRepository;
    private AppointmentService appointmentService;
    private UserService userService;


    public void checkSlotAvailability(int doctorId, Timestamp startDateTime, Timestamp endDateTime) {
        List<Appointment> overlappingAppointments = appointmentRepository.findByDoctorIdAndDateTime(doctorId, startDateTime, endDateTime);

        if (!overlappingAppointments.isEmpty()) {
            throw new RuntimeException("Intervalul dvs. se suprapune cu o altă programare existențială.");
        }
    }


    @Transactional
    public void save(Appointment appointment) {

        appointmentRepository.save(appointment);
    }


    public Appointment getAppointmentById(int id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id=" + id + " not found"));
    }

    public Appointment addAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(int id, Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id=" + id + " not found"));
        existingAppointment.setDoctor(appointment.getDoctor());
        existingAppointment.setPatient(appointment.getPatient());
        existingAppointment.setStartTime(appointment.getStartTime());
        existingAppointment.setEndTime(appointment.getEndTime());
        existingAppointment.setDate(appointment.getDate());
        existingAppointment.setStatus(appointment.getStatus());
        return appointmentRepository.save(existingAppointment);
    }

    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

}
