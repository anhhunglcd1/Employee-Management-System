package dao;

import model.Attendance;
import java.sql.*;
import java. time.LocalDate;
import java. util.ArrayList;
import java. util.List;

public class AttendanceDAO {
    
    /**
     * Lấy tất cả chấm công
     */
    public List<Attendance> getAllAttendance() {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance ORDER BY attendance_date DESC";
        
        try (Connection conn = DatabaseConnection. getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return attendances;
    }
    
    /**
     * Lấy chấm công theo nhân viên
     */
    public List<Attendance> getAttendanceByEmployee(int employeeId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE employee_id = ? ORDER BY attendance_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return attendances;
    }
    
    /**
     * Lấy chấm công theo tháng
     */
    public List<Attendance> getAttendanceByMonth(int month, int year) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE MONTH(attendance_date) = ? AND YEAR(attendance_date) = ?  " +
                    "ORDER BY attendance_date DESC";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return attendances;
    }
    
    /**
     * Lấy chấm công theo nhân viên và tháng
     */
    public List<Attendance> getAttendanceByEmployeeAndMonth(int employeeId, int month, int year) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE employee_id = ? " +
                    "AND MONTH(attendance_date) = ? AND YEAR(attendance_date) = ? " +
                    "ORDER BY attendance_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            pstmt. setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return attendances;
    }
    
    /**
     * Thêm chấm công
     */
    public boolean addAttendance(Attendance attendance) {
        String sql = "INSERT INTO Attendance (employee_id, attendance_date, check_in_time, check_out_time, " +
                    "status, working_hours, overtime_hours, late_minutes, early_leave_minutes, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setAttendanceParameters(pstmt, attendance);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật chấm công
     */
    public boolean updateAttendance(Attendance attendance) {
        String sql = "UPDATE Attendance SET check_in_time=?, check_out_time=?, status=?, " +
                    "working_hours=?, overtime_hours=?, late_minutes=?, early_leave_minutes=?, notes=? " +
                    "WHERE employee_id=?  AND attendance_date=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTime(1, attendance.getCheckInTime() != null ? 
                         Time.valueOf(attendance.getCheckInTime()) : null);
            pstmt.setTime(2, attendance.getCheckOutTime() != null ? 
                         Time.valueOf(attendance. getCheckOutTime()) : null);
            pstmt.setString(3, attendance.getStatus());
            pstmt.setDouble(4, attendance.getWorkingHours());
            pstmt.setDouble(5, attendance. getOvertimeHours());
            pstmt.setInt(6, attendance.getLateMinutes());
            pstmt.setInt(7, attendance.getEarlyLeaveMinutes());
            pstmt. setString(8, attendance.getNotes());
            pstmt.setInt(9, attendance.getEmployeeId());
            pstmt. setDate(10, Date.valueOf(attendance.getAttendanceDate()));
            
            return pstmt. executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa chấm công
     */
    public boolean deleteAttendance(int employeeId, LocalDate date) {
        String sql = "DELETE FROM Attendance WHERE employee_id = ? AND attendance_date = ? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, Date. valueOf(date));
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra đã chấm công chưa
     */
    public boolean isAttendanceExists(int employeeId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM Attendance WHERE employee_id = ? AND attendance_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Attendance extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setEmployeeId(rs.getInt("employee_id"));
        
        Date date = rs.getDate("attendance_date");
        if (date != null) {
            attendance.setAttendanceDate(date.toLocalDate());
        }
        
        Time checkIn = rs.getTime("check_in_time");
        if (checkIn != null) {
            attendance.setCheckInTime(checkIn.toLocalTime());
        }
        
        Time checkOut = rs.getTime("check_out_time");
        if (checkOut != null) {
            attendance.setCheckOutTime(checkOut.toLocalTime());
        }
        
        attendance.setStatus(rs.getString("status"));
        attendance.setWorkingHours(rs.getDouble("working_hours"));
        attendance.setOvertimeHours(rs. getDouble("overtime_hours"));
        attendance.setLateMinutes(rs.getInt("late_minutes"));
        attendance.setEarlyLeaveMinutes(rs.getInt("early_leave_minutes"));
        attendance.setNotes(rs.getString("notes"));
        
        return attendance;
    }
    
    private void setAttendanceParameters(PreparedStatement pstmt, Attendance attendance) 
            throws SQLException {
        pstmt.setInt(1, attendance.getEmployeeId());
        pstmt.setDate(2, Date.valueOf(attendance. getAttendanceDate()));
        pstmt.setTime(3, attendance.getCheckInTime() != null ? 
                         Time.valueOf(attendance.getCheckInTime()) : null);
        pstmt.setTime(4, attendance.getCheckOutTime() != null ? 
                         Time.valueOf(attendance.getCheckOutTime()) : null);
        pstmt.setString(5, attendance.getStatus());
        pstmt.setDouble(6, attendance.getWorkingHours());
        pstmt.setDouble(7, attendance.getOvertimeHours());
        pstmt.setInt(8, attendance.getLateMinutes());
        pstmt.setInt(9, attendance.getEarlyLeaveMinutes());
        pstmt.setString(10, attendance.getNotes());
    }
}