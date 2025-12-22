package model;

public class Salary {
    private int id;
    private int employeeId;
    private int month;
    private int year;
    private double baseSalary;
    private double allowance;
    private double overtimePay;
    private double bonus;
    private double deduction;
    private double totalSalary;
    private int workingDays;
    private String status; // PENDING, APPROVED, PAID
    
    public Salary() {}
    
    public Salary(int id, int employeeId, int month, int year, double baseSalary, 
                 double allowance, double overtimePay, double bonus, double deduction, 
                 double totalSalary, int workingDays, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.allowance = allowance;
        this.overtimePay = overtimePay;
        this.bonus = bonus;
        this.deduction = deduction;
        this.totalSalary = totalSalary;
        this.workingDays = workingDays;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    
    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }
    
    public double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(double overtimePay) { this.overtimePay = overtimePay; }
    
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    
    public double getDeduction() { return deduction; }
    public void setDeduction(double deduction) { this.deduction = deduction; }
    
    public double getTotalSalary() { return totalSalary; }
    public void setTotalSalary(double totalSalary) { this.totalSalary = totalSalary; }
    
    public int getWorkingDays() { return workingDays; }
    public void setWorkingDays(int workingDays) { this.workingDays = workingDays; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}