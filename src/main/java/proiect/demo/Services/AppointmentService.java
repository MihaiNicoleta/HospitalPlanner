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
import proiect.demo.Domain.Doctor;
import proiect.demo.Domain.User;
import proiect.demo.Repostiories.AppointmentRepository;
import proiect.demo.Repostiories.AvailableSlotRepository;
import proiect.demo.Repostiories.DoctorRepository;
import proiect.demo.Repostiories.UserRepository;
import proiect.demo.configs.ResourceNotFoundException;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;

    public Doctor getDoctorById(int doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + doctorId + " not found"));
    }

    public User getUserById(int pacientId) {
        return userRepository.findById(pacientId)
                .orElseThrow(() -> new ResourceNotFoundException("Pacient with id=" + pacientId + " not found"));
    }

    public void checkSlotAvailability(int doctorId, LocalTime startDateTime, LocalTime endDateTime, Date date, HttpSession session) {
        // Convertește Timestamp la LocalDateTime
        LocalTime startDateTimeLocal = startDateTime;
        LocalTime endDateTimeLocal = endDateTime;
        System.out.println("AICI 6");
        // Verifică suprapunerile pentru programările dintr-o anumită dată
        List<Appointment> overlappingAppointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        for (Appointment appointment : overlappingAppointments) {
            LocalTime appointmentStart = appointment.getStartTime();
            LocalTime appointmentEnd = appointment.getEndTime();

            // Verifică suprapunerea intervalului orar
            if (startDateTimeLocal.isBefore(appointmentEnd) && endDateTimeLocal.isAfter(appointmentStart)) {
                session.setAttribute("eroare",true);
                System.out.println("AICI 7");
                throw new RuntimeException("Intervalul dvs. se suprapune cu o altă programare existentă.");
            }
        }
    }


    @Transactional
    public void save(Appointment appointment, HttpSession session) {
        int doctorId = appointment.getDoctor().getId();
        LocalTime startDateTime = appointment.getStartTime();
        LocalTime endDateTime = appointment.getEndTime();
        Date date = appointment.getDate();
        System.out.println("AICI 3");
        checkSlotAvailability(doctorId, startDateTime, endDateTime, date,session);
        System.out.println("AICI 4");
        appointment.setStatus("confirmed");
        appointmentRepository.save(appointment);
        System.out.println("AICI 5");
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
