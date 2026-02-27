package dao;

import java.sql.*;

public class DatabaseConnection {
    // Thông tin kết nối
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "htqlnv";
    private static final String USERNAME = "sa"; // Hoặc "sa"
    private static final String PASSWORD = "mkdc@2025"; // Thay đổi theo mật khẩu của bạn
    
    // Connection String
    private static final String URL = String.format(
        "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
        SERVER, PORT, DATABASE
    );
    
    private static Connection connection = null;
    
    /**
     * Lấy kết nối database
     * @return 
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✓ Kết nối database thành công!");
            }
        } catch (ClassNotFoundException e) {
            System. err.println("✗ Không tìm thấy SQL Server JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối database!");
            System.err.println("URL: " + URL);
            System.err.println("Username: " + USERNAME);
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection. isClosed()) {
                connection.close();
                System.out.println("✓ Đã đóng kết nối database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test kết nối
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out. println("\n=== THÔNG TIN KẾT NỐI ===");
                System.out. println("Database: " + metaData.getDatabaseProductName());
                System. out.println("Version: " + metaData.getDatabaseProductVersion());
                System.out.println("Driver: " + metaData.getDriverName());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("User: " + metaData. getUserName());
                System.out.println("========================\n");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Main để test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Đang test kết nối database.. .\n");
        
        if (testConnection()) {
            System.out.println("✓ Test kết nối THÀNH CÔNG!");
        } else {
            System.out.println("✗ Test kết nối THẤT BẠI!");
            System.out.println("\nKiểm tra lại:");
            System.out.println("1. SQL Server đã chạy chưa?");
            System. out.println("2. Database 'HRMS_Database' đã tạo chưa?");
            System. out.println("3. Username và Password đúng chưa?");
            System. out.println("4. JDBC Driver đã thêm vào project chưa?");
        }
        
        closeConnection();
    }
}