package view;

import dao.LeaveRequestDAO;
import dao.EmployeeDAO;
import model.LeaveRequest;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java. time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class LeavePanel extends JPanel {
    private LeaveRequestDAO leaveRequestDAO;
    private EmployeeDAO employeeDAO;
    
    private JTable leaveTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<Employee> cboEmployee;
    private JComboBox<String> cboLeaveType, cboStatus, cboFilterStatus;
    private JSpinner spnStartDate, spnEndDate;
    private JTextField txtTotalDays;
    private JTextArea txtReason, txtApproverNote;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnApprove, btnReject, btnFilter;
    
    private int selectedRequestId = -1;
    
    public LeavePanel() {
        leaveRequestDAO = new LeaveRequestDAO();
        employeeDAO = new EmployeeDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        setupLayout();
        loadData();
        setupEventListeners();
    }
    
    private void initComponents() {
        // Table
        String[] columns = {"ID", "Nhân Viên", "Loại", "Từ Ngày", "Đến Ngày", 
                           "Số Ngày", "Lý Do", "Trạng Thái", "Ngày Yêu Cầu"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        leaveTable = new JTable(tableModel);
        leaveTable.setRowHeight(25);
        
        // Hide ID column
        leaveTable.getColumnModel().getColumn(0).setMinWidth(0);
        leaveTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Form
        cboEmployee = new JComboBox<>();
        cboLeaveType = new JComboBox<>(new String[]{"ANNUAL", "SICK", "UNPAID", "PERSONAL", "MATERNITY", "PATERNITY"});
        cboStatus = new JComboBox<>(new String[]{"PENDING", "APPROVED", "REJECTED", "CANCELLED"});
        cboFilterStatus = new JComboBox<>(new String[]{"TẤT CẢ", "PENDING", "APPROVED", "REJECTED"});
        
        spnStartDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(spnStartDate, "dd/MM/yyyy");
        spnStartDate.setEditor(startEditor);
        
        spnEndDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(spnEndDate, "dd/MM/yyyy");
        spnEndDate.setEditor(endEditor);
        
        txtTotalDays = new JTextField(10);
        txtTotalDays.setEditable(false);
        txtTotalDays.setBackground(Color. LIGHT_GRAY);
        
        txtReason = new JTextArea(3, 20);
        txtApproverNote = new JTextArea(3, 20);
        
        // Buttons
        btnAdd = createButton("➕ Đăng Ký", new Color(46, 204, 113));
        btnUpdate = createButton("✏️ Cập Nhật", new Color(52, 152, 219));
        btnDelete = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClear = createButton("🔄 Làm Mới", new Color(149, 165, 166));
        btnApprove = createButton("✅ Duyệt", new Color(39, 174, 96));
        btnReject = createButton("❌ Từ Chối", new Color(192, 57, 43));
        btnFilter = createButton("🔍 Lọc", new Color(155, 89, 182));
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        return button;
    }
    
    private void setupLayout() {
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Lọc Đơn"));
        filterPanel.add(new JLabel("Trạng Thái:"));
        filterPanel.add(cboFilterStatus);
        filterPanel. add(btnFilter);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Đơn Nghỉ Phép"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboEmployee, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Loại Phép:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboLeaveType, gbc);
        
        // Row 1
        gbc. gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Từ Ngày:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spnStartDate, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Đến Ngày:"), gbc);
        gbc.gridx = 3;
        formPanel.add(spnEndDate, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số Ngày:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTotalDays, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboStatus, gbc);
        
        // Row 3
        gbc. gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Lý Do:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(txtReason), gbc);
        
        // Row 4
        gbc. gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Ghi Chú Duyệt:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(txtApproverNote), gbc);
        
        // Row 5 - Buttons
        gbc.gridx = 0; gbc. gridy = 5; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel, gbc);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Đơn Nghỉ Phép"));
        
        // Main Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout. NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout. NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        btnAdd.addActionListener(e -> addLeaveRequest());
        btnUpdate. addActionListener(e -> updateLeaveRequest());
        btnDelete. addActionListener(e -> deleteLeaveRequest());
        btnClear.addActionListener(e -> clearForm());
        btnApprove.addActionListener(e -> approveLeaveRequest());
        btnReject.addActionListener(e -> rejectLeaveRequest());
        btnFilter.addActionListener(e -> filterByStatus());
        
        // Auto calculate days
        spnStartDate.addChangeListener(e -> calculateTotalDays());
        spnEndDate.addChangeListener(e -> calculateTotalDays());
        
        leaveTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedLeaveRequest();
            }
        });
    }
    
    private void loadData() {
        loadEmployees();
        loadLeaveRequests();
    }
    
    private void loadEmployees() {
        cboEmployee.removeAllItems();
        List<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee emp : employees) {
            cboEmployee.addItem(emp);
        }
    }
    
    private void loadLeaveRequests() {
        tableModel.setRowCount(0);
        List<LeaveRequest> requests = leaveRequestDAO.getAllLeaveRequests();
        
        for (LeaveRequest req : requests) {
            Employee emp = employeeDAO.getEmployeeById(req.getEmployeeId());
            
            Object[] row = {
                req.getId(),
                emp != null ? emp.getFullName() : "",
                req.getLeaveType(),
                req.getStartDate(),
                req.getEndDate(),
                req.getTotalDays(),
                req. getReason(),
                req.getStatus(),
                req.getRequestDate()
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterByStatus() {
        String status = cboFilterStatus.getSelectedItem().toString();
        
        if (status.equals("TẤT CẢ")) {
            loadLeaveRequests();
            return;
        }
        
        tableModel.setRowCount(0);
        List<LeaveRequest> requests = leaveRequestDAO.getLeaveRequestsByStatus(status);
        
        for (LeaveRequest req : requests) {
            Employee emp = employeeDAO.getEmployeeById(req.getEmployeeId());
            
            Object[] row = {
                req. getId(),
                emp != null ?  emp.getFullName() : "",
                req.getLeaveType(),
                req.getStartDate(),
                req.getEndDate(),
                req.getTotalDays(),
                req.getReason(),
                req.getStatus(),
                req.getRequestDate()
            };
            tableModel. addRow(row);
        }
    }
    
    private void calculateTotalDays() {
        try {
            Date startDate = (Date) spnStartDate.getValue();
            Date endDate = (Date) spnEndDate.getValue();
            
            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            long days = ChronoUnit. DAYS.between(start, end) + 1; // +1 to include both days
            txtTotalDays. setText(String.valueOf(days));
            
        } catch (Exception ex) {
            txtTotalDays.setText("0");
        }
    }
    
    private void addLeaveRequest() {
        if (!validateForm()) return;
        
        LeaveRequest request = getLeaveRequestFromForm();
        
        if (leaveRequestDAO.addLeaveRequest(request)) {
            JOptionPane.showMessageDialog(this, "Đăng ký nghỉ phép thành công!");
            loadLeaveRequests();
            clearForm();
        } else {
            JOptionPane. showMessageDialog(this, "Đăng ký thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateLeaveRequest() {
        if (selectedRequestId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn cần cập nhật!");
            return;
        }
        
        if (!validateForm()) return;
        
        LeaveRequest request = getLeaveRequestFromForm();
        request.setId(selectedRequestId);
        
        if (leaveRequestDAO.updateLeaveRequest(request)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadLeaveRequests();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void approveLeaveRequest() {
        if (selectedRequestId == -1) {
            JOptionPane. showMessageDialog(this, "Vui lòng chọn đơn cần duyệt!");
            return;
        }
        
        String note = txtApproverNote.getText().trim();
        
        if (leaveRequestDAO.approveLeaveRequest(selectedRequestId, 1, "APPROVED", note)) {
            JOptionPane.showMessageDialog(this, "Duyệt đơn thành công!");
            loadLeaveRequests();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Duyệt đơn thất bại!");
        }
    }
    
    private void rejectLeaveRequest() {
        if (selectedRequestId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn cần từ chối!");
            return;
        }
        
        String note = txtApproverNote. getText().trim();
        if (note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do từ chối!");
            return;
        }
        
        if (leaveRequestDAO.approveLeaveRequest(selectedRequestId, 1, "REJECTED", note)) {
            JOptionPane.showMessageDialog(this, "Từ chối đơn thành công!");
            loadLeaveRequests();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Từ chối đơn thất bại!");
        }
    }
    
    private void deleteLeaveRequest() {
        if (selectedRequestId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa? ");
        if (confirm == JOptionPane.YES_OPTION) {
            if (leaveRequestDAO.deleteLeaveRequest(selectedRequestId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadLeaveRequests();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void loadSelectedLeaveRequest() {
        int row = leaveTable.getSelectedRow();
        if (row == -1) return;
        
        selectedRequestId = (int) tableModel.getValueAt(row, 0);
        
        // Set employee
        String empName = tableModel.getValueAt(row, 1).toString();
        for (int i = 0; i < cboEmployee.getItemCount(); i++) {
            if (cboEmployee.getItemAt(i).getFullName().equals(empName)) {
                cboEmployee.setSelectedIndex(i);
                break;
            }
        }
        
        cboLeaveType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        
        LocalDate startDate = (LocalDate) tableModel.getValueAt(row, 3);
        spnStartDate.setValue(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        
        LocalDate endDate = (LocalDate) tableModel.getValueAt(row, 4);
        spnEndDate.setValue(Date.from(endDate.atStartOfDay(ZoneId. systemDefault()).toInstant()));
        
        txtTotalDays.setText(tableModel.getValueAt(row, 5).toString());
        txtReason.setText(tableModel. getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
        cboStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
    }
    
    private LeaveRequest getLeaveRequestFromForm() {
        LeaveRequest request = new LeaveRequest();
        
        Employee emp = (Employee) cboEmployee.getSelectedItem();
        request.setEmployeeId(emp. getId());
        
        request.setLeaveType(cboLeaveType.getSelectedItem().toString());
        
        Date startDate = (Date) spnStartDate.getValue();
        request.setStartDate(startDate. toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        Date endDate = (Date) spnEndDate.getValue();
        request.setEndDate(endDate. toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        request.setTotalDays(Double.parseDouble(txtTotalDays.getText()));
        request.setReason(txtReason.getText());
        request.setStatus(cboStatus.getSelectedItem().toString());
        
        return request;
    }
    
    private boolean validateForm() {
        if (cboEmployee.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return false;
        }
        if (txtReason.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do!");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        selectedRequestId = -1;
        if (cboEmployee.getItemCount() > 0) cboEmployee.setSelectedIndex(0);
        cboLeaveType.setSelectedIndex(0);
        cboStatus.setSelectedIndex(0);
        spnStartDate.setValue(new Date());
        spnEndDate.setValue(new Date());
        txtTotalDays.setText("");
        txtReason.setText("");
        txtApproverNote. setText("");
        leaveTable.clearSelection();
    }
}