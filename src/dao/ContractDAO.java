package dao;

import model. Contract;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    
    public List<Contract> getAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM Contracts ORDER BY start_date DESC";
        
        try (Connection conn = DatabaseConnection. getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                contracts.add(extractContractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    public List<Contract> getContractsByEmployee(int employeeId) {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM Contracts WHERE employee_id = ? ORDER BY start_date DESC";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt. executeQuery();
            
            while (rs.next()) {
                contracts.add(extractContractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    public Contract getContractById(int id) {
        String sql = "SELECT * FROM Contracts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractContractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addContract(Contract contract) {
        // Kiểm tra xem nhân viên đã có hợp đồng còn hạn chưa
        System.out.println("Checking active contract for employee: " + contract.getEmployeeId());
        if (hasActiveContract(contract.getEmployeeId())) {
            System.out.println("Employee already has active contract. Cannot add new one.");
            return false;
        }
        System.out.println("No active contract found. Proceeding with insert...");
        
        String sql = "INSERT INTO Contracts (employee_id, contract_type, contract_number, " +
                    "start_date, end_date, salary, allowance, salary_coefficient, position, work_location, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert contract
            PreparedStatement pstmt = conn.prepareStatement(sql);
            setContractParameters(pstmt, contract);
            pstmt.executeUpdate();
            pstmt.close();
            
            // Update BaseSalary table
            updateBaseSalaryFromContract(conn, contract);
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean updateContract(Contract contract) {
        String sql = "UPDATE Contracts SET contract_type=?, contract_number=?, start_date=?, " +
                    "end_date=?, salary=?, allowance=?, salary_coefficient=?, position=?, work_location=?, status=?, notes=? WHERE id=?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update contract
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, contract.getContractType());
            pstmt.setString(2, contract.getContractNumber());
            pstmt.setDate(3, Date.valueOf(contract.getStartDate()));
            pstmt.setDate(4, contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null);
            pstmt.setDouble(5, contract.getSalary());
            pstmt.setDouble(6, contract.getAllowance());
            pstmt.setDouble(7, contract.getSalaryCoefficient());
            pstmt.setString(8, contract.getPosition());
            pstmt.setString(9, contract.getWorkLocation());
            pstmt.setString(10, contract.getStatus());
            pstmt.setString(11, contract.getNotes());
            pstmt.setInt(12, contract.getId());
            pstmt.executeUpdate();
            pstmt.close();
            
            // Update BaseSalary table
            updateBaseSalaryFromContract(conn, contract);
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean deleteContract(int id) {
        String sql = "DELETE FROM Contracts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy hợp đồng sắp hết hạn (30 ngày)
     */
    public List<Contract> getExpiringContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM vw_ExpiringContracts";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Contract contract = new Contract();
                contract.setId(rs.getInt("contract_id"));
                contract.setContractNumber(rs.getString("contract_number"));
                contract.setContractType(rs.getString("contract_type"));
                
                Date startDate = rs.getDate("start_date");
                if (startDate != null) {
                    contract.setStartDate(startDate.toLocalDate());
                }
                
                Date endDate = rs.getDate("end_date");
                if (endDate != null) {
                    contract.setEndDate(endDate. toLocalDate());
                }
                
                contracts.add(contract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    private Contract extractContractFromResultSet(ResultSet rs) throws SQLException {
        Contract contract = new Contract();
        contract.setId(rs. getInt("id"));
        contract.setEmployeeId(rs.getInt("employee_id"));
        contract.setContractType(rs.getString("contract_type"));
        contract.setContractNumber(rs. getString("contract_number"));
        
        Date startDate = rs. getDate("start_date");
        if (startDate != null) {
            contract.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            contract.setEndDate(endDate.toLocalDate());
        }
        
        contract.setSalary(rs.getDouble("salary"));
        contract.setAllowance(rs.getDouble("allowance"));
        contract.setSalaryCoefficient(rs.getDouble("salary_coefficient"));
        contract.setPosition(rs.getString("position"));
        contract.setWorkLocation(rs.getString("work_location"));
        contract.setStatus(rs.getString("status"));
        
        Date terminationDate = rs.getDate("termination_date");
        if (terminationDate != null) {
            contract.setTerminationDate(terminationDate.toLocalDate());
        }
        
        contract.setTerminationReason(rs.getString("termination_reason"));
        contract.setNotes(rs. getString("notes"));
        contract.setFileUrl(rs.getString("file_url"));
        
        return contract;
    }
    
    private void setContractParameters(PreparedStatement pstmt, Contract contract) throws SQLException {
        pstmt.setInt(1, contract.getEmployeeId());
        pstmt.setString(2, contract.getContractType());
        pstmt.setString(3, contract.getContractNumber());
        pstmt.setDate(4, Date.valueOf(contract.getStartDate()));
        pstmt.setDate(5, contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null);
        pstmt.setDouble(6, contract.getSalary());
        pstmt.setDouble(7, contract.getAllowance());
        pstmt.setDouble(8, contract.getSalaryCoefficient());
        pstmt.setString(9, contract.getPosition());
        pstmt.setString(10, contract.getWorkLocation());
        pstmt.setString(11, contract.getStatus());
        pstmt.setString(12, contract.getNotes());
    }
    
    /**
     * Kiểm tra nhân viên có hợp đồng còn hạn không
     * Trả về true nếu có hợp đồng ACTIVE và chưa hết hạn
     */
    public boolean hasActiveContract(int employeeId) {
        String sql = "SELECT id, contract_number, end_date FROM Contracts " +
                    "WHERE employee_id = ? " +
                    "AND status = 'ACTIVE' " +
                    "AND (end_date IS NULL OR end_date >= CAST(GETDATE() AS DATE))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Found active contract ID: " + rs.getInt("id") + 
                                 ", Number: " + rs.getString("contract_number") + 
                                 ", End date: " + rs.getDate("end_date"));
                return true;
            }
            System.out.println("No active contract found for employee ID: " + employeeId);
        } catch (SQLException e) {
            System.err.println("Error checking active contract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cập nhật BaseSalary từ thông tin hợp đồng
     */
    private void updateBaseSalaryFromContract(Connection conn, Contract contract) throws SQLException {
        // Check if BaseSalary record exists
        String checkSql = "SELECT id FROM BaseSalary WHERE employee_id = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, contract.getEmployeeId());
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            // Update existing record
            String updateSql = "UPDATE BaseSalary SET base_salary = ?, allowance = ?, salary_coefficient = ?, effective_date = ? WHERE employee_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDouble(1, contract.getSalary());
            updateStmt.setDouble(2, contract.getAllowance());
            updateStmt.setDouble(3, contract.getSalaryCoefficient());
            updateStmt.setDate(4, Date.valueOf(contract.getStartDate()));
            updateStmt.setInt(5, contract.getEmployeeId());
            updateStmt.executeUpdate();
            updateStmt.close();
        } else {
            // Insert new record
            String insertSql = "INSERT INTO BaseSalary (employee_id, base_salary, allowance, salary_coefficient, effective_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, contract.getEmployeeId());
            insertStmt.setDouble(2, contract.getSalary());
            insertStmt.setDouble(3, contract.getAllowance());
            insertStmt.setDouble(4, contract.getSalaryCoefficient());
            insertStmt.setDate(5, Date.valueOf(contract.getStartDate()));
            insertStmt.executeUpdate();
            insertStmt.close();
        }
        
        rs.close();
        checkStmt.close();
    }
}