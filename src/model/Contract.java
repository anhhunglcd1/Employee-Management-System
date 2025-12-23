package model;

import java.time.LocalDate;

public class Contract {
    private int id;
    private int employeeId;
    private String contractType; // PROBATION, FIXED_TERM, INDEFINITE
    private String contractNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private double salary;
    private double allowance;
    private double salaryCoefficient;
    private String position;
    private String workLocation;
    private String status; // ACTIVE, EXPIRED, TERMINATED
    private LocalDate terminationDate;
    private String terminationReason;
    private String notes;
    private String fileUrl;
    
    public Contract() {
    }
    
    public Contract(int id, int employeeId, String contractType, String contractNumber,
                   LocalDate startDate, LocalDate endDate, double salary, double allowance, double salaryCoefficient, String position,
                   String workLocation, String status, LocalDate terminationDate,
                   String terminationReason, String notes, String fileUrl) {
        this.id = id;
        this.employeeId = employeeId;
        this.contractType = contractType;
        this.contractNumber = contractNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
        this.allowance = allowance;
        this.salaryCoefficient = salaryCoefficient;
        this.position = position;
        this.workLocation = workLocation;
        this.status = status;
        this.terminationDate = terminationDate;
        this.terminationReason = terminationReason;
        this.notes = notes;
        this.fileUrl = fileUrl;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    
    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }
    
    public double getSalaryCoefficient() { return salaryCoefficient; }
    public void setSalaryCoefficient(double salaryCoefficient) { this.salaryCoefficient = salaryCoefficient; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getWorkLocation() { return workLocation; }
    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getTerminationDate() { return terminationDate; }
    public void setTerminationDate(LocalDate terminationDate) { this.terminationDate = terminationDate; }
    
    public String getTerminationReason() { return terminationReason; }
    public void setTerminationReason(String terminationReason) { this.terminationReason = terminationReason; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}