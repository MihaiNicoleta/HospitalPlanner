package proiect.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.Appointment;
import proiect.demo.Domain.Department;
import proiect.demo.Domain.Doctor;
import proiect.demo.Domain.Schedule;
import proiect.demo.Repostiories.AppointmentRepository;
import proiect.demo.Repostiories.DoctorRepository;

import java.sql.Time;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class HospitalService {}
/*
@Service
public class HospitalService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Doctor> getAvailableDoctorsInDepartment(Date date, Time startTime, Time endTime, Department department) {
        List<Doctor> allDoctors = doctorRepository.findByDepartment(department);

        List<Doctor> availableDoctors = new ArrayList<>();
        for (Doctor doctor : allDoctors) {
            if (isDoctorAvailable(doctor, date, startTime, endTime)) {
                availableDoctors.add(doctor);
            }
        }
        return availableDoctors;
    }

    private boolean isDoctorAvailable(Doctor doctor, Date date, Time startTime, Time endTime) {
        List<Schedule> schedules = scheduleRepository.findByDoctor(doctor);
        List<Appointment> appointments = appointmentRepository.findByDoctorAndDate(doctor, date);

        for (Schedule schedule : schedules) {
            if (schedule.getDayOfWeek().equalsIgnoreCase(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().toString())) {
                if ((startTime.after(Time.valueOf(schedule.getStartTime())) || startTime.equals(Time.valueOf(schedule.getStartTime()))) &&
                        (endTime.before(Time.valueOf(schedule.getEndTime())) || endTime.equals(Time.valueOf(schedule.getEndTime())))) {
                    for (Appointment appointment : appointments) {
                        if ((startTime.before(appointment.getEndTime())) && (endTime.after(appointment.getStartTime()))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}*/