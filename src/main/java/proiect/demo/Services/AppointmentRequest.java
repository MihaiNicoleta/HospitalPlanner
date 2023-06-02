package proiect.demo.Services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class AppointmentRequest {
    private int doctorId;
    private int patientId;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Date date;

    public int getDoctorId() {
        return doctorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }
}
