package dao;

import model. Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM Departments ORDER BY code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs. next()) {
                departments.add(extractDepartmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return departments;
    }
    
    public Department getDepartmentById(int id) {
        String sql = "SELECT * FROM Departments WHERE id = ? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDepartmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO Departments (code, name, description, status) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, department.getCode());
            pstmt.setString(2, department.getName());
            pstmt.setString(3, department.getDescription());
            pstmt.setString(4, department.getStatus());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDepartment(Department department) {
        String sql = "UPDATE Departments SET code=?, name=?, description=?, status=? WHERE id=? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, department.getCode());
            pstmt.setString(2, department.getName());
            pstmt.setString(3, department.getDescription());
            pstmt.setString(4, department.getStatus());
            pstmt.setInt(5, department. getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDepartment(int id) {
        String sql = "DELETE FROM Departments WHERE id = ?";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Department extractDepartmentFromResultSet(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setId(rs.getInt("id"));
        dept.setCode(rs.getString("code"));
        dept.setName(rs. getString("name"));
        dept.setDescription(rs.getString("description"));
        
        int managerId = rs.getInt("manager_id");
        if (!rs.wasNull()) {
            dept.setManagerId(managerId);
        }
        
        dept.setStatus(rs.getString("status"));
        
        return dept;
    }
}