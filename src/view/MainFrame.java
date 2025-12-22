package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private EmployeePanel employeePanel;
    private DepartmentPanel departmentPanel;
    
    public MainFrame() {
        initComponents();
        setTitle("HỆ THỐNG QUẢN LÝ NHÂN SỰ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame. MAXIMIZED_BOTH); // Full screen
    }
    
    private void initComponents() {
        // Tạo TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font. BOLD, 14));
        
        // Tạo các Panel
        employeePanel = new EmployeePanel();
        departmentPanel = new DepartmentPanel();
        
        // Thêm các tab
        tabbedPane.addTab("👥 QUẢN LÝ NHÂN VIÊN", employeePanel);
        tabbedPane.addTab("🏢 QUẢN LÝ PHÒNG BAN & CHỨC VỤ", departmentPanel);
        
        // Thêm vào JFrame
        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ V1.0", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
//        JLabel subtitleLabel = new JLabel("HR Management System v1.0", SwingConstants. CENTER);
//        subtitleLabel. setFont(new Font("Arial", Font.PLAIN, 14));
//        subtitleLabel.setForeground(new Color(189, 195, 199));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(new Color(52, 73, 94));
        titleContainer.add(titleLabel);
//        titleContainer.add(subtitleLabel);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setPreferredSize(new Dimension(0, 30));
        
        JLabel footerLabel = new JLabel("© 2025 HR Management System | Developed by Your Team", SwingConstants.CENTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font. PLAIN, 12));
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Chạy ứng dụng
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}