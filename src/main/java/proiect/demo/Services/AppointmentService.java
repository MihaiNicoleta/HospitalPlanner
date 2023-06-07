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

    public List<Appointment> findByDoctorId(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
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

    /**
     * Verifică disponibilitatea unui interval de timp pentru un anumit doctor într-o anumită dată.
     *
     * @param doctorId       Id-ul doctorului.
     * @param startDateTime  Ora de început a intervalului de timp.
     * @param endDateTime    Ora de sfârșit a intervalului de timp.
     * @param date           Data.
     * @param session        Sesiunea curentă.
     * @throws RuntimeException        Dacă intervalul de timp se suprapune cu o altă programare existentă.
     * @throws ResourceNotFoundException Dacă nu există un doctor cu id-ul specificat.
     */
    public void checkSlotAvailability(int doctorId, LocalTime startDateTime, LocalTime endDateTime, Date date, HttpSession session) {
        LocalTime startDateTimeLocal = startDateTime;
        LocalTime endDateTimeLocal = endDateTime;
        List<Appointment> overlappingAppointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        for (Appointment appointment : overlappingAppointments) {
            LocalTime appointmentStart = appointment.getStartTime();
            LocalTime appointmentEnd = appointment.getEndTime();

            if (startDateTimeLocal.isBefore(appointmentEnd) && endDateTimeLocal.isAfter(appointmentStart)) {
                session.setAttribute("eroare", true);
                throw new RuntimeException("Intervalul dvs. se suprapune cu o altă programare existentă.");
            }
        }
    }
    /**
     * Obțineți programările disponibile pentru un anumit departament.
     *
     * @param departmentId Id-ul departamentului.
     * @return Lista de programări disponibile.
     */
    public List<Appointment> getFreeAppointments(int departmentId) {
        // Obțineți departamentul pe baza id-ului
        Optional<Department> department = departmentRepository.findById(departmentId);

        List<Doctor> doctors = doctorRepository.findByDepartmentId(departmentId);
        List<Appointment> freeAppointments = new ArrayList<>();

        for (Doctor doctor : doctors) {
            // programarile existente ale doctorului
            List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndDate(doctor, new Date());

            // intervalele libere
            List<TimeSlot> freeTimeSlots = getFreeTimeSlots(existingAppointments);
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
    /**
     * Obțineți intervalele orare libere pentru programările existente.
     *
     * @param existingAppointments Lista de programări existente.
     * @return Lista de intervale orare libere.
     */
    private List<TimeSlot> getFreeTimeSlots(List<Appointment> existingAppointments) {
        List<TimeSlot> freeTimeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0); // Ora de început (8:00 AM)
        LocalTime endTime = LocalTime.of(15, 0); // Ora de sfârșit (3:00 PM)

        //toate intervalele orare intre ora de inceput si ora de sfarsit
        while (startTime.isBefore(endTime)) {
            LocalTime nextTime = startTime.plusMinutes(120);
            TimeSlot timeSlot = new TimeSlot(startTime, nextTime);

            boolean isFree = true;

            //  intervalul orar este deja ocupat?
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

    /**
     * Verifică dacă două intervale orare se suprapun.
     *
     * @param start1 Ora de început a primului interval orar.
     * @param end1   Ora de sfârșit a primului interval orar.
     * @param start2 Ora de început a celui de-al doilea interval orar.
     * @param end2   Ora de sfârșit a celui de-al doilea interval orar.
     * @return `true` dacă intervalele se suprapun, `false` în caz contrar.
     */
    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }


    /**
     * Salvează o programare și verifică disponibilitatea intervalului de timp.
     *
     * @param appointment Programarea care trebuie salvată.
     * @param session     Sesiunea curentă.
     * @throws RuntimeException        Dacă intervalul de timp se suprapune cu o altă programare existentă.
     * @throws ResourceNotFoundException Dacă nu există programarea pentru doctorul specificat.
     */
    @Transactional
    public void save(Appointment appointment, HttpSession session) {
        int doctorId = appointment.getDoctor().getId();
        LocalTime startDateTime = appointment.getStartTime();
        LocalTime endDateTime = appointment.getEndTime();
        Date date = appointment.getDate();
        checkSlotAvailability(doctorId, startDateTime, endDateTime, date, session);
        appointment.setStatus("confirmed");
        appointmentRepository.save(appointment);
    }

    /**
     * Obțineți o programare după id.
     *
     * @param id Id-ul programării.
     * @return Programarea cu id-ul specificat.
     * @throws ResourceNotFoundException Dacă nu există programarea cu id-ul specificat.
     */
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
