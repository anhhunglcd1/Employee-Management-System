package dao;

import model.Position;
import java.sql.*;
import java. util.ArrayList;
import java. util.List;

public class PositionDAO {
    
    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM Positions ORDER BY code";
        
        try (Connection conn = DatabaseConnection. getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                positions.add(extractPositionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return positions;
    }
    
    public Position getPositionById(int id) {
        String sql = "SELECT * FROM Positions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs. next()) {
                return extractPositionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addPosition(Position position) {
        String sql = "INSERT INTO Positions (code, name, level, description) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, position. getCode());
            pstmt.setString(2, position. getName());
            pstmt. setString(3, position.getLevel());
            pstmt.setString(4, position.getDescription());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePosition(Position position) {
        String sql = "UPDATE Positions SET code=?, name=?, level=?, description=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection. getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, position.getCode());
            pstmt.setString(2, position.getName());
            pstmt.setString(3, position.getLevel());
            pstmt.setString(4, position.getDescription());
            pstmt.setInt(5, position.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePosition(int id) {
        String sql = "DELETE FROM Positions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Position extractPositionFromResultSet(ResultSet rs) throws SQLException {
        Position position = new Position();
        position.setId(rs.getInt("id"));
        position.setCode(rs.getString("code"));
        position.setName(rs.getString("name"));
        position.setLevel(rs.getString("level"));
        position.setDescription(rs.getString("description"));
        
        return position;
    }
}