package dao;

import model.LeaveRequest;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {
    
    /**
     * Lấy tất cả đơn nghỉ phép
     */
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequests ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection. getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Lấy đơn nghỉ phép theo nhân viên
     */
    public List<LeaveRequest> getLeaveRequestsByEmployee(int employeeId) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequests WHERE employee_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Lấy đơn theo trạng thái
     */
    public List<LeaveRequest> getLeaveRequestsByStatus(String status) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequests WHERE status = ? ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Thêm đơn nghỉ phép
     */
    public boolean addLeaveRequest(LeaveRequest request) {
        String sql = "INSERT INTO LeaveRequests (employee_id, leave_type, start_date, end_date, " +
                    "total_days, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, request.getEmployeeId());
            pstmt.setString(2, request.getLeaveType());
            pstmt.setDate(3, Date.valueOf(request.getStartDate()));
            pstmt.setDate(4, Date.valueOf(request.getEndDate()));
            pstmt.setDouble(5, request.getTotalDays());
            pstmt. setString(6, request.getReason());
            pstmt. setString(7, request.getStatus());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật đơn nghỉ phép
     */
    public boolean updateLeaveRequest(LeaveRequest request) {
        String sql = "UPDATE LeaveRequests SET leave_type=?, start_date=?, end_date=?, " +
                    "total_days=?, reason=?, status=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, request.getLeaveType());
            pstmt.setDate(2, Date.valueOf(request.getStartDate()));
            pstmt.setDate(3, Date.valueOf(request.getEndDate()));
            pstmt. setDouble(4, request.getTotalDays());
            pstmt.setString(5, request.getReason());
            pstmt.setString(6, request.getStatus());
            pstmt.setInt(7, request.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Duyệt đơn nghỉ phép (gọi Stored Procedure)
     */
    public boolean approveLeaveRequest(int requestId, int approverId, String status, String note) {
        String sql = "{CALL sp_ApproveLeaveRequest(?, ?, ?, ? )}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, requestId);
            cstmt.setInt(2, approverId);
            cstmt. setString(3, status);
            cstmt.setString(4, note);
            
            cstmt.execute();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa đơn nghỉ phép
     */
    public boolean deleteLeaveRequest(int id) {
        String sql = "DELETE FROM LeaveRequests WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private LeaveRequest extractLeaveRequestFromResultSet(ResultSet rs) throws SQLException {
        LeaveRequest request = new LeaveRequest();
        request.setId(rs.getInt("id"));
        request.setEmployeeId(rs.getInt("employee_id"));
        request.setLeaveType(rs.getString("leave_type"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            request.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            request.setEndDate(endDate.toLocalDate());
        }
        
        request.setTotalDays(rs.getDouble("total_days"));
        request.setReason(rs.getString("reason"));
        request.setStatus(rs.getString("status"));
        
        int approverId = rs.getInt("approver_id");
        if (!rs.wasNull()) {
            request.setApproverId(approverId);
        }
        
        request. setApproverNote(rs.getString("approver_note"));
        
        Timestamp requestDate = rs.getTimestamp("request_date");
        if (requestDate != null) {
            request.setRequestDate(requestDate. toLocalDateTime());
        }
        
        Timestamp approveDate = rs.getTimestamp("approve_date");
        if (approveDate != null) {
            request.setApproveDate(approveDate.toLocalDateTime());
        }
        
        return request;
    }
}