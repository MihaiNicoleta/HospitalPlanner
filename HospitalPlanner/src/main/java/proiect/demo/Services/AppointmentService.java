package proiect.demo.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Appointment;
import proiect.demo.Repostiories.AppointmentRepository;
import proiect.demo.configs.ResourceNotFoundException;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
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
