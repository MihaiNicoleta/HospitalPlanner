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
import proiect.demo.Domain.*;
import proiect.demo.Repostiories.*;
import proiect.demo.configs.ResourceNotFoundException;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired

    private DepartmentRepository departmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    private AvailableSlotRepository availableSlotsRepository;

    @Autowired
    private AppointmentRepository appointmentsRepository;
    private AppointmentService appointmentService;
    private UserService userService;
    @Autowired
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
    public List<Appointment> getFreeAppointments(int departmentId) {
        // Obțineți departamentul pe baza id-ului
        Optional<Department> department = departmentRepository.findById(departmentId);
        System.out.println("DA 1 "+ department);
        // Obțineți lista de doctori din departament
        List<Doctor> doctors = doctorRepository.findByDepartmentId(departmentId);
        System.out.println("DA 2" + doctors);
        List<Appointment> freeAppointments = new ArrayList<>();

        for (Doctor doctor : doctors) {
            // programarile existente ale doctorului
            List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndDate(doctor, new Date());

            // intervalele libere
            List<TimeSlot> freeTimeSlots = getFreeTimeSlots(existingAppointments);
            System.out.println("DA 3" + freeTimeSlots);
            // pt fiecare programare libera facem un appoiment
            for (TimeSlot timeSlot : freeTimeSlots) {
                Appointment appointment = new Appointment();
                appointment.setDoctor(doctor);
                appointment.setStartTime(timeSlot.getStartTime());
                appointment.setEndTime(timeSlot.getEndTime());
                appointment.setDate(new Date());
                appointment.setStatus("Free");

                freeAppointments.add(appointment);
            }
        }

        return freeAppointments;
    }

    private List<TimeSlot> getFreeTimeSlots(List<Appointment> existingAppointments) {
        List<TimeSlot> freeTimeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0); // Ora de început (8:00 AM)
        LocalTime endTime = LocalTime.of(15, 0); // Ora de sfârșit (3:00 PM)

        // Generați toate intervalele orare între ora de început și ora de sfârșit
        while (startTime.isBefore(endTime)) {
            LocalTime nextTime = startTime.plusMinutes(120); // Durata programării este de 30 de minute
            TimeSlot timeSlot = new TimeSlot(startTime, nextTime);

            boolean isFree = true;

            // Verificați dacă intervalul orar este deja ocupat
            for (Appointment appointment : existingAppointments) {
                if (isOverlapping(appointment.getStartTime(), appointment.getEndTime(), startTime, nextTime)) {
                    isFree = false;
                    break;
                }
            }

            if (isFree) {
                freeTimeSlots.add(timeSlot);
            }

            startTime = nextTime;
        }

        return freeTimeSlots;
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
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
