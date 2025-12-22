package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private int id;
    private int employeeId;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String status; // PRESENT, LATE, EARLY_LEAVE, ABSENT, LEAVE
    private double workingHours;
    private double overtimeHours;
    private String notes;
    
    public Attendance() {}
    
    public Attendance(int id, int employeeId, LocalDate date, LocalTime checkIn, 
                     LocalTime checkOut, String status, double workingHours, 
                     double overtimeHours, String notes) {
        this.id = id;
        this. employeeId = employeeId;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this. status = status;
        this. workingHours = workingHours;
        this.overtimeHours = overtimeHours;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }
    
    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getWorkingHours() { return workingHours; }
    public void setWorkingHours(double workingHours) { this.workingHours = workingHours; }
    
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}