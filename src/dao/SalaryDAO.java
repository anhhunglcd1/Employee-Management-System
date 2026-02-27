package dao;

import model.Salary;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {
    
    /**
     * Lấy tất cả bảng lương
     */
    public List<Salary> getAllSalaries() {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM MonthlySalary ORDER BY salary_year DESC, salary_month DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                salaries.add(extractSalaryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salaries;
    }
    
    /**
     * Lấy lương theo nhân viên
     */
    public List<Salary> getSalariesByEmployee(int employeeId) {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM MonthlySalary WHERE employee_id = ? " +
                    "ORDER BY salary_year DESC, salary_month DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                salaries.add(extractSalaryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salaries;
    }
    
    /**
     * Lấy lương theo tháng/năm
     */
    public List<Salary> getSalariesByPeriod(int month, int year) {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM MonthlySalary WHERE salary_month = ? AND salary_year = ? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt. executeQuery();
            
            while (rs.next()) {
                salaries.add(extractSalaryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salaries;
    }
    
    /**
     * Lấy lương cụ thể của nhân viên theo tháng/năm
     */
    public Salary getSalaryByEmployeeAndPeriod(int employeeId, int month, int year) {
        String sql = "SELECT * FROM MonthlySalary WHERE employee_id = ? " +
                    "AND salary_month = ? AND salary_year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt. executeQuery();
            
            if (rs.next()) {
                return extractSalaryFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Thêm bảng lương mới
     */
    public boolean addSalary(Salary salary) {
        String sql = "INSERT INTO MonthlySalary (employee_id, salary_month, salary_year, " +
                    "base_salary, allowance, overtime_pay, bonus, other_income, " +
                    "late_deduction, absent_deduction, insurance_deduction, tax_deduction, " +
                    "other_deduction, gross_salary, total_deduction, net_salary, " +
                    "working_days, standard_days, overtime_hours, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setSalaryParameters(pstmt, salary);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật bảng lương
     */
    public boolean updateSalary(Salary salary) {
        String sql = "UPDATE MonthlySalary SET base_salary=?, allowance=?, overtime_pay=?, " +
                    "bonus=?, other_income=?, late_deduction=?, absent_deduction=?, " +
                    "insurance_deduction=?, tax_deduction=?, other_deduction=?, " +
                    "gross_salary=?, total_deduction=?, net_salary=?, working_days=?, " +
                    "standard_days=?, overtime_hours=?, status=?, notes=? " +
                    "WHERE employee_id=? AND salary_month=? AND salary_year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn. prepareStatement(sql)) {
            
            pstmt.setDouble(1, salary.getBaseSalary());
            pstmt.setDouble(2, salary.getAllowance());
            pstmt.setDouble(3, salary.getOvertimePay());
            pstmt.setDouble(4, salary.getBonus());
            pstmt.setDouble(5, salary.getOtherIncome());
            pstmt. setDouble(6, salary.getLateDeduction());
            pstmt.setDouble(7, salary.getAbsentDeduction());
            pstmt.setDouble(8, salary.getInsuranceDeduction());
            pstmt.setDouble(9, salary. getTaxDeduction());
            pstmt.setDouble(10, salary.getOtherDeduction());
            pstmt.setDouble(11, salary.getGrossSalary());
            pstmt.setDouble(12, salary.getTotalDeduction());
            pstmt.setDouble(13, salary.getNetSalary());
            pstmt. setInt(14, salary.getWorkingDays());
            pstmt.setInt(15, salary.getStandardDays());
            pstmt.setDouble(16, salary.getOvertimeHours());
            pstmt.setString(17, salary.getStatus());
            pstmt.setString(18, salary.getNotes());
            pstmt.setInt(19, salary.getEmployeeId());
            pstmt.setInt(20, salary.getSalaryMonth());
            pstmt.setInt(21, salary.getSalaryYear());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa bảng lương
     */
    public boolean deleteSalary(int employeeId, int month, int year) {
        String sql = "DELETE FROM MonthlySalary WHERE employee_id = ? " +
                    "AND salary_month = ? AND salary_year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy thông tin lương cơ bản / hệ số / phụ cấp của nhân viên.
     * Ưu tiên từ BaseSalary, fallback sang Contracts.
     */
    public Salary getBaseSalaryInfo(int employeeId) {
        Salary info = new Salary();
        info.setSalaryCoefficient(1.0); // mặc định

        // 1. Thử lấy từ BaseSalary
        String sql = "SELECT TOP 1 base_salary, salary_coefficient, allowance " +
                     "FROM BaseSalary WHERE employee_id = ? AND status = 'ACTIVE' " +
                     "ORDER BY effective_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                info.setBaseSalary(rs.getDouble("base_salary"));
                info.setSalaryCoefficient(rs.getDouble("salary_coefficient"));
                info.setAllowance(rs.getDouble("allowance"));
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Fallback: lấy từ Contracts
        sql = "SELECT TOP 1 salary, salary_coefficient, allowance " +
              "FROM Contracts WHERE employee_id = ? AND status = 'ACTIVE' " +
              "ORDER BY start_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                info.setBaseSalary(rs.getDouble("salary"));
                info.setSalaryCoefficient(rs.getDouble("salary_coefficient"));
                info.setAllowance(rs.getDouble("allowance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * Tính lương tháng (gọi Stored Procedure)
     */
    public boolean calculateMonthlySalary(int employeeId, int month, int year) {
        String sql = "{CALL sp_CalculateMonthlySalary(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt. setInt(1, employeeId);
            cstmt.setInt(2, month);
            cstmt.setInt(3, year);
            
            cstmt.execute();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Duyệt lương
     */
    public boolean approveSalary(int employeeId, int month, int year, int approverId) {
        String sql = "UPDATE MonthlySalary SET status = 'APPROVED', approved_by = ?, " +
                    "updated_date = GETDATE() WHERE employee_id = ? " +
                    "AND salary_month = ? AND salary_year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Cho phép approved_by NULL nếu approverId <= 0
            if (approverId > 0) {
                pstmt.setInt(1, approverId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setInt(2, employeeId);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Salary extractSalaryFromResultSet(ResultSet rs) throws SQLException {
        Salary salary = new Salary();
        salary.setId(rs.getInt("id"));
        salary.setEmployeeId(rs.getInt("employee_id"));
        salary.setSalaryMonth(rs.getInt("salary_month"));
        salary.setSalaryYear(rs. getInt("salary_year"));
        salary.setBaseSalary(rs.getDouble("base_salary"));
        salary.setSalaryCoefficient(rs.getDouble("salary_coefficient"));
        salary.setAllowance(rs.getDouble("allowance"));
        salary.setOvertimePay(rs.getDouble("overtime_pay"));
        salary.setBonus(rs.getDouble("bonus"));
        salary.setOtherIncome(rs.getDouble("other_income"));
        salary.setLateDeduction(rs.getDouble("late_deduction"));
        salary.setAbsentDeduction(rs.getDouble("absent_deduction"));
        salary.setInsuranceDeduction(rs. getDouble("insurance_deduction"));
        salary.setTaxDeduction(rs.getDouble("tax_deduction"));
        salary.setOtherDeduction(rs.getDouble("other_deduction"));
        salary.setGrossSalary(rs.getDouble("gross_salary"));
        salary.setTotalDeduction(rs.getDouble("total_deduction"));
        salary.setNetSalary(rs.getDouble("net_salary"));
        salary.setWorkingDays(rs. getInt("working_days"));
        salary.setStandardDays(rs.getInt("standard_days"));
        salary.setOvertimeHours(rs.getDouble("overtime_hours"));
        salary.setStatus(rs.getString("status"));
        
        int approvedBy = rs.getInt("approved_by");
        if (! rs.wasNull()) {
            salary.setApprovedBy(approvedBy);
        }
        
        salary.setNotes(rs.getString("notes"));
        
        return salary;
    }
    
    private void setSalaryParameters(PreparedStatement pstmt, Salary salary) throws SQLException {
        pstmt.setInt(1, salary.getEmployeeId());
        pstmt.setInt(2, salary.getSalaryMonth());
        pstmt.setInt(3, salary.getSalaryYear());
        pstmt.setDouble(4, salary.getBaseSalary());
        pstmt.setDouble(5, salary.getAllowance());
        pstmt.setDouble(6, salary.getOvertimePay());
        pstmt.setDouble(7, salary.getBonus());
        pstmt.setDouble(8, salary.getOtherIncome());
        pstmt.setDouble(9, salary.getLateDeduction());
        pstmt.setDouble(10, salary. getAbsentDeduction());
        pstmt.setDouble(11, salary.getInsuranceDeduction());
        pstmt.setDouble(12, salary.getTaxDeduction());
        pstmt.setDouble(13, salary.getOtherDeduction());
        pstmt.setDouble(14, salary.getGrossSalary());
        pstmt.setDouble(15, salary.getTotalDeduction());
        pstmt.setDouble(16, salary.getNetSalary());
        pstmt.setInt(17, salary.getWorkingDays());
        pstmt.setInt(18, salary.getStandardDays());
        pstmt.setDouble(19, salary.getOvertimeHours());
        pstmt.setString(20, salary. getStatus());
        pstmt.setString(21, salary. getNotes());
    }
}