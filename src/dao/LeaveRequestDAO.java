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
     * Duyệt đơn nghỉ phép và cập nhật trạng thái nhân viên
     */
    public boolean approveLeaveRequest(int requestId, int approverId, String status, String note) {
        Connection conn = null;
        PreparedStatement pstmtLeave = null;
        PreparedStatement pstmtEmployee = null;
        PreparedStatement pstmtGetEmp = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // 1. Cập nhật đơn nghỉ phép
            String sqlLeave = "UPDATE LeaveRequests SET status = ?, approver_id = ?, approver_note = ?, " +
                             "approve_date = GETDATE() WHERE id = ?";
            pstmtLeave = conn.prepareStatement(sqlLeave);
            
            pstmtLeave.setString(1, status);
            if (approverId > 0) {
                pstmtLeave.setInt(2, approverId);
            } else {
                pstmtLeave.setNull(2, Types.INTEGER);
            }
            pstmtLeave.setString(3, note);
            pstmtLeave.setInt(4, requestId);
            
            int rowsUpdated = pstmtLeave.executeUpdate();
            
            if (rowsUpdated > 0) {
                // 2. Lấy employee_id từ đơn nghỉ phép
                String sqlGetEmp = "SELECT employee_id, start_date, end_date FROM LeaveRequests WHERE id = ?";
                pstmtGetEmp = conn.prepareStatement(sqlGetEmp);
                pstmtGetEmp.setInt(1, requestId);
                rs = pstmtGetEmp.executeQuery();
                
                if (rs.next()) {
                    int employeeId = rs.getInt("employee_id");
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    Date today = Date.valueOf(java.time.LocalDate.now());
                    
                    // 3. Cập nhật trạng thái nhân viên nếu đơn được duyệt và đang trong thời gian nghỉ
                    String newEmployeeStatus = null;
                    if (status.equals("APPROVED") && 
                        startDate != null && endDate != null &&
                        !today.before(startDate) && !today.after(endDate)) {
                        newEmployeeStatus = "Đang nghỉ phép";
                    } else if (status.equals("REJECTED") || 
                              (status.equals("APPROVED") && endDate != null && today.after(endDate))) {
                        newEmployeeStatus = "Đang làm việc";
                    }
                    
                    if (newEmployeeStatus != null) {
                        String sqlEmployee = "UPDATE Employees SET status = ? WHERE id = ?";
                        pstmtEmployee = conn.prepareStatement(sqlEmployee);
                        pstmtEmployee.setString(1, newEmployeeStatus);
                        pstmtEmployee.setInt(2, employeeId);
                        pstmtEmployee.executeUpdate();
                    }
                }
            }
            
            conn.commit(); // Commit transaction
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Đóng resources
            try {
                if (rs != null) rs.close();
                if (pstmtGetEmp != null) pstmtGetEmp.close();
                if (pstmtEmployee != null) pstmtEmployee.close();
                if (pstmtLeave != null) pstmtLeave.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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