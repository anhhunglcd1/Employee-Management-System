package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private int id;
    private int employeeId;
    private LocalDate attendanceDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String status; // PRESENT, LATE, EARLY_LEAVE, ABSENT, LEAVE, BUSINESS_TRIP
    private double workingHours;
    private double overtimeHours;
    private int lateMinutes;
    private int earlyLeaveMinutes;
    private String notes;
    
    public Attendance() {
    }
    
    public Attendance(int id, int employeeId, LocalDate attendanceDate, LocalTime checkInTime,
                     LocalTime checkOutTime, String status, double workingHours, double overtimeHours,
                     int lateMinutes, int earlyLeaveMinutes, String notes) {
        this.id = id;
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.workingHours = workingHours;
        this.overtimeHours = overtimeHours;
        this.lateMinutes = lateMinutes;
        this.earlyLeaveMinutes = earlyLeaveMinutes;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    
    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }
    
    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getWorkingHours() { return workingHours; }
    public void setWorkingHours(double workingHours) { this.workingHours = workingHours; }
    
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
    
    public int getLateMinutes() { return lateMinutes; }
    public void setLateMinutes(int lateMinutes) { this.lateMinutes = lateMinutes; }
    
    public int getEarlyLeaveMinutes() { return earlyLeaveMinutes; }
    public void setEarlyLeaveMinutes(int earlyLeaveMinutes) { this.earlyLeaveMinutes = earlyLeaveMinutes; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
}