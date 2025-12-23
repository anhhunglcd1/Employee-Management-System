package model;

public class Salary {
    private int id;
    private int employeeId;
    private int salaryMonth;
    private int salaryYear;
    private double baseSalary;
    private double salaryCoefficient;
    private double allowance;
    private double overtimePay;
    private double bonus;
    private double otherIncome;
    private double lateDeduction;
    private double absentDeduction;
    private double insuranceDeduction;
    private double taxDeduction;
    private double otherDeduction;
    private double grossSalary;
    private double totalDeduction;
    private double netSalary;
    private int workingDays;
    private int standardDays;
    private double overtimeHours;
    private String status; // PENDING, APPROVED, PAID
    private Integer approvedBy;
    private String notes;
    
    public Salary() {
    }
    
    // Constructor
    public Salary(int id, int employeeId, int salaryMonth, int salaryYear, double baseSalary,
                 double salaryCoefficient, double allowance, double overtimePay, double bonus, double otherIncome,
                 double lateDeduction, double absentDeduction, double insuranceDeduction,
                 double taxDeduction, double otherDeduction, double grossSalary,
                 double totalDeduction, double netSalary, int workingDays, int standardDays,
                 double overtimeHours, String status, Integer approvedBy, String notes) {
        this.id = id;
        this.employeeId = employeeId;
        this.salaryMonth = salaryMonth;
        this.salaryYear = salaryYear;
        this.baseSalary = baseSalary;
        this.salaryCoefficient = salaryCoefficient;
        this.allowance = allowance;
        this.overtimePay = overtimePay;
        this.bonus = bonus;
        this.otherIncome = otherIncome;
        this.lateDeduction = lateDeduction;
        this. absentDeduction = absentDeduction;
        this.insuranceDeduction = insuranceDeduction;
        this.taxDeduction = taxDeduction;
        this. otherDeduction = otherDeduction;
        this.grossSalary = grossSalary;
        this.totalDeduction = totalDeduction;
        this. netSalary = netSalary;
        this.workingDays = workingDays;
        this.standardDays = standardDays;
        this. overtimeHours = overtimeHours;
        this.status = status;
        this.approvedBy = approvedBy;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public int getSalaryMonth() { return salaryMonth; }
    public void setSalaryMonth(int salaryMonth) { this.salaryMonth = salaryMonth; }
    
    public int getSalaryYear() { return salaryYear; }
    public void setSalaryYear(int salaryYear) { this.salaryYear = salaryYear; }
    
    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    
    public double getSalaryCoefficient() { return salaryCoefficient; }
    public void setSalaryCoefficient(double salaryCoefficient) { this.salaryCoefficient = salaryCoefficient; }
    
    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }
    
    public double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(double overtimePay) { this.overtimePay = overtimePay; }
    
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    
    public double getOtherIncome() { return otherIncome; }
    public void setOtherIncome(double otherIncome) { this.otherIncome = otherIncome; }
    
    public double getLateDeduction() { return lateDeduction; }
    public void setLateDeduction(double lateDeduction) { this.lateDeduction = lateDeduction; }
    
    public double getAbsentDeduction() { return absentDeduction; }
    public void setAbsentDeduction(double absentDeduction) { this.absentDeduction = absentDeduction; }
    
    public double getInsuranceDeduction() { return insuranceDeduction; }
    public void setInsuranceDeduction(double insuranceDeduction) { this.insuranceDeduction = insuranceDeduction; }
    
    public double getTaxDeduction() { return taxDeduction; }
    public void setTaxDeduction(double taxDeduction) { this.taxDeduction = taxDeduction; }
    
    public double getOtherDeduction() { return otherDeduction; }
    public void setOtherDeduction(double otherDeduction) { this.otherDeduction = otherDeduction; }
    
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    
    public double getTotalDeduction() { return totalDeduction; }
    public void setTotalDeduction(double totalDeduction) { this.totalDeduction = totalDeduction; }
    
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    
    public int getWorkingDays() { return workingDays; }
    public void setWorkingDays(int workingDays) { this.workingDays = workingDays; }
    
    public int getStandardDays() { return standardDays; }
    public void setStandardDays(int standardDays) { this.standardDays = standardDays; }
    
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}