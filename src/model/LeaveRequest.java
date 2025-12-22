package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequest {
    private int id;
    private int employeeId;
    private String leaveType; // ANNUAL, SICK, UNPAID, PERSONAL
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDays;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private int approverId;
    private LocalDateTime requestDate;
    private LocalDateTime approveDate;
    private String approverNote;
    
    public LeaveRequest() {}
    
    public LeaveRequest(int id, int employeeId, String leaveType, LocalDate startDate, 
                       LocalDate endDate, int totalDays, String reason, String status, 
                       int approverId, LocalDateTime requestDate, LocalDateTime approveDate, 
                       String approverNote) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this. endDate = endDate;
        this.totalDays = totalDays;
        this.reason = reason;
        this.status = status;
        this.approverId = approverId;
        this.requestDate = requestDate;
        this.approveDate = approveDate;
        this. approverNote = approverNote;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getApproverId() { return approverId; }
    public void setApproverId(int approverId) { this.approverId = approverId; }
    
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    
    public LocalDateTime getApproveDate() { return approveDate; }
    public void setApproveDate(LocalDateTime approveDate) { this.approveDate = approveDate; }
    
    public String getApproverNote() { return approverNote; }
    public void setApproverNote(String approverNote) { this.approverNote = approverNote; }
}