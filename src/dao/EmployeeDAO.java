package dao;

import model.Employee;
import java.sql.*;
import java.time.LocalDate;
import java. util.ArrayList;
import java. util.List;

public class EmployeeDAO {
    
    /**
     * Lấy tất cả nhân viên
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Lấy nhân viên theo ID
     */
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM Employees WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Thêm nhân viên mới
     */
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (employee_code, full_name, date_of_birth, gender, " +
                    "id_card, phone, email, address, department_id, position_id, join_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setEmployeeParameters(pstmt, employee);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin nhân viên
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET employee_code=?, full_name=?, date_of_birth=?, " +
                    "gender=?, id_card=?, phone=?, email=?, address=?, department_id=?, " +
                    "position_id=?, join_date=?, status=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setEmployeeParameters(pstmt, employee);
            pstmt.setInt(13, employee.getId());
            
            int result = pstmt. executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa nhân viên
     */
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM Employees WHERE id = ?";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm nhân viên theo từ khóa
     */
    public List<Employee> searchEmployees(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE " +
                    "employee_code LIKE ? OR full_name LIKE ? OR phone LIKE ? OR email LIKE ? " +
                    "ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt. setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Lấy nhân viên theo phòng ban
     */
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE department_id = ?  ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn. prepareStatement(sql)) {
            
            pstmt.setInt(1, departmentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Lấy nhân viên theo trạng thái
     */
    public List<Employee> getEmployeesByStatus(String status) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE status = ?  ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn. prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt. executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Kiểm tra mã nhân viên đã tồn tại chưa
     */
    public boolean isEmployeeCodeExists(String employeeCode, int excludeId) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE employee_code = ? AND id != ?";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeCode);
            pstmt.setInt(2, excludeId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Helper method:  Extract Employee from ResultSet
     */
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setEmployeeCode(rs.getString("employee_code"));
        employee.setFullName(rs.getString("full_name"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            employee.setDateOfBirth(dob.toLocalDate());
        }
        
        employee.setGender(rs.getString("gender"));
        employee.setIdCard(rs.getString("id_card"));
        employee.setPhone(rs.getString("phone"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs. getString("address"));
        employee.setDepartmentId(rs.getInt("department_id"));
        employee.setPositionId(rs.getInt("position_id"));
        
        Date joinDate = rs.getDate("join_date");
        if (joinDate != null) {
            employee.setJoinDate(joinDate.toLocalDate());
        }
        
        Date resignDate = rs.getDate("resign_date");
        if (resignDate != null) {
            employee.setJoinDate(resignDate.toLocalDate());
        }
        
        employee.setStatus(rs.getString("status"));
        
        return employee;
    }
    
    /**
     * Helper method: Set Employee parameters to PreparedStatement
     */
    private void setEmployeeParameters(PreparedStatement pstmt, Employee employee) 
            throws SQLException {
        pstmt.setString(1, employee.getEmployeeCode());
        pstmt.setString(2, employee.getFullName());
        pstmt.setDate(3, employee.getDateOfBirth() != null ? 
                         Date.valueOf(employee.getDateOfBirth()) : null);
        pstmt.setString(4, employee.getGender());
        pstmt.setString(5, employee.getIdCard());
        pstmt.setString(6, employee.getPhone());
        pstmt.setString(7, employee.getEmail());
        pstmt.setString(8, employee.getAddress());
        pstmt.setInt(9, employee.getDepartmentId());
        pstmt.setInt(10, employee.getPositionId());
        pstmt.setDate(11, employee.getJoinDate() != null ? 
                         Date.valueOf(employee.getJoinDate()) : null);
        pstmt.setString(12, employee. getStatus());
    }
}