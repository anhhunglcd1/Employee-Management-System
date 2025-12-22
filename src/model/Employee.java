package model;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String employeeCode;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String idCard;
    private int departmentId;
    private int positionId;
    private LocalDate joinDate;
    private String status; // "WORKING", "ON_LEAVE", "RESIGNED"
    private String phone;
    private String email;
    private String address;
    
    // Constructors
    public Employee() {}
    
    public Employee(int id, String employeeCode, String fullName, LocalDate dateOfBirth, 
                   String gender, String idCard, int departmentId, int positionId, 
                   LocalDate joinDate, String status, String phone, String email, String address) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.idCard = idCard;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.joinDate = joinDate;
        this.status = status;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    
    public int getPositionId() { return positionId; }
    public void setPositionId(int positionId) { this.positionId = positionId; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}