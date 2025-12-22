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
    private String status; // ACTIVE, EXPIRED, TERMINATED
    private String notes;
    
    public Contract() {}
    
    public Contract(int id, int employeeId, String contractType, String contractNumber, 
                   LocalDate startDate, LocalDate endDate, double salary, String status, 
                   String notes) {
        this.id = id;
        this.employeeId = employeeId;
        this. contractType = contractType;
        this.contractNumber = contractNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this. salary = salary;
        this. status = status;
        this. notes = notes;
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
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}