package view;

import dao.AttendanceDAO;
import dao. EmployeeDAO;
import model. Attendance;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java. time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AttendancePanel extends JPanel {
    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;
    
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<Employee> cboEmployee;
    private JSpinner spnDate, spnCheckIn, spnCheckOut;
    private JComboBox<String> cboStatus;
    private JTextField txtWorkingHours, txtOvertimeHours, txtLateMinutes, txtNotes;
    private JSpinner spnMonth, spnYear;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnFilter, btnCalculate;
    
    private int selectedEmployeeId = -1;
    private LocalDate selectedDate = null;
    
    public AttendancePanel() {
        attendanceDAO = new AttendanceDAO();
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
        String[] columns = {"Nhân Viên", "Ngày", "Giờ Vào", "Giờ Ra", "Trạng Thái", 
                           "Giờ Làm", "Tăng Ca", "Trễ (phút)", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setFont(new Font("Arial", Font. BOLD, 12));
        
        // Form components
        cboEmployee = new JComboBox<>();
        
        spnDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDate, "dd/MM/yyyy");
        spnDate.setEditor(dateEditor);
        
        SpinnerDateModel checkInModel = new SpinnerDateModel();
        spnCheckIn = new JSpinner(checkInModel);
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(spnCheckIn, "HH:mm");
        spnCheckIn.setEditor(checkInEditor);
        
        SpinnerDateModel checkOutModel = new SpinnerDateModel();
        spnCheckOut = new JSpinner(checkOutModel);
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(spnCheckOut, "HH:mm");
        spnCheckOut.setEditor(checkOutEditor);
        
        cboStatus = new JComboBox<>(new String[]{"PRESENT", "LATE", "EARLY_LEAVE", "ABSENT", "LEAVE", "BUSINESS_TRIP"});
        
        txtWorkingHours = new JTextField(10);
        txtWorkingHours.setEditable(false);
        txtOvertimeHours = new JTextField(10);
        txtLateMinutes = new JTextField(10);
        txtLateMinutes.setEditable(false);
        txtNotes = new JTextField(20);
        
        spnMonth = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
        spnYear = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        
        // Buttons
        btnAdd = createButton("➕ Thêm", new Color(46, 204, 113));
        btnUpdate = createButton("✏️ Cập Nhật", new Color(52, 152, 219));
        btnDelete = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClear = createButton("🔄 Làm Mới", new Color(149, 165, 166));
        btnFilter = createButton("🔍 Lọc Theo Tháng", new Color(155, 89, 182));
        btnCalculate = createButton("🧮 Tính Toán", new Color(241, 196, 15));
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
        filterPanel.setBorder(BorderFactory.createTitledBorder("Lọc Dữ Liệu"));
        filterPanel.add(new JLabel("Tháng:"));
        filterPanel.add(spnMonth);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(spnYear);
        filterPanel.add(btnFilter);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Chấm Công"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboEmployee, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày:"), gbc);
        gbc.gridx = 3;
        formPanel.add(spnDate, gbc);
        
        // Row 1
        gbc. gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Giờ Vào:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spnCheckIn, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ Ra:"), gbc);
        gbc.gridx = 3;
        formPanel.add(spnCheckOut, gbc);
        
        // Row 2
        gbc. gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboStatus, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ Làm:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtWorkingHours, gbc);
        
        // Row 3
        gbc. gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Tăng Ca (giờ):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtOvertimeHours, gbc);
        
        gbc. gridx = 2;
        formPanel.add(new JLabel("Trễ (phút):"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtLateMinutes, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(txtNotes, gbc);
        
        // Row 5 - Buttons
        gbc. gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnCalculate);
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel, gbc);
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory. createTitledBorder("Danh Sách Chấm Công"));
        
        // Main Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout. NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout. NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        btnAdd.addActionListener(e -> addAttendance());
        btnUpdate. addActionListener(e -> updateAttendance());
        btnDelete. addActionListener(e -> deleteAttendance());
        btnClear.addActionListener(e -> clearForm());
        btnFilter.addActionListener(e -> filterByMonth());
        btnCalculate. addActionListener(e -> calculateWorkingHours());
        
        attendanceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAttendance();
            }
        });
    }
    
    private void loadData() {
        loadEmployees();
        loadAttendanceData();
    }
    
    private void loadEmployees() {
        cboEmployee.removeAllItems();
        List<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee emp : employees) {
            cboEmployee.addItem(emp);
        }
    }
    
    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        List<Attendance> attendances = attendanceDAO.getAllAttendance();
        
        for (Attendance att : attendances) {
            Employee emp = employeeDAO.getEmployeeById(att.getEmployeeId());
            
            Object[] row = {
                emp != null ? emp.getFullName() : "",
                att.getAttendanceDate(),
                att.getCheckInTime(),
                att.getCheckOutTime(),
                att.getStatus(),
                att.getWorkingHours(),
                att.getOvertimeHours(),
                att.getLateMinutes(),
                att.getNotes()
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterByMonth() {
        int month = (int) spnMonth.getValue();
        int year = (int) spnYear.getValue();
        
        tableModel.setRowCount(0);
        List<Attendance> attendances = attendanceDAO.getAttendanceByMonth(month, year);
        
        for (Attendance att : attendances) {
            Employee emp = employeeDAO.getEmployeeById(att. getEmployeeId());
            
            Object[] row = {
                emp != null ? emp.getFullName() : "",
                att. getAttendanceDate(),
                att.getCheckInTime(),
                att.getCheckOutTime(),
                att.getStatus(),
                att.getWorkingHours(),
                att.getOvertimeHours(),
                att. getLateMinutes(),
                att.getNotes()
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Tìm thấy " + attendances.size() + " bản ghi!", 
            "Kết Quả", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void calculateWorkingHours() {
        try {
            Date checkInDate = (Date) spnCheckIn.getValue();
            Date checkOutDate = (Date) spnCheckOut.getValue();
            
            LocalTime checkIn = checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime checkOut = checkOutDate.toInstant().atZone(ZoneId. systemDefault()).toLocalTime();
            
            // Tính giờ làm việc
            long minutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
            double hours = minutes / 60.0;
            txtWorkingHours.setText(String.format("%.2f", hours));
            
            // Tính trễ (so với 8:00)
            LocalTime standardCheckIn = LocalTime.of(8, 0);
            if (checkIn.isAfter(standardCheckIn)) {
                long lateMinutes = ChronoUnit.MINUTES.between(standardCheckIn, checkIn);
                txtLateMinutes.setText(String.valueOf(lateMinutes));
            } else {
                txtLateMinutes.setText("0");
            }
            
            // Tính tăng ca (nếu > 8 giờ)
            if (hours > 8) {
                txtOvertimeHours.setText(String.format("%.2f", hours - 8));
            } else {
                txtOvertimeHours. setText("0");
            }
            
        } catch (Exception ex) {
            JOptionPane. showMessageDialog(this, "Lỗi tính toán: " + ex.getMessage());
        }
    }
    
    private void addAttendance() {
        if (!validateForm()) return;
        
        Attendance attendance = getAttendanceFromForm();
        
        if (attendanceDAO.isAttendanceExists(attendance.getEmployeeId(), attendance.getAttendanceDate())) {
            JOptionPane.showMessageDialog(this, 
                "Đã có bản ghi chấm công cho ngày này!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (attendanceDAO.addAttendance(attendance)) {
            JOptionPane.showMessageDialog(this, "Thêm chấm công thành công!");
            loadAttendanceData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chấm công thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAttendance() {
        if (selectedEmployeeId == -1 || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần cập nhật!");
            return;
        }
        
        if (!validateForm()) return;
        
        Attendance attendance = getAttendanceFromForm();
        
        if (attendanceDAO.updateAttendance(attendance)) {
            JOptionPane. showMessageDialog(this, "Cập nhật thành công!");
            loadAttendanceData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void deleteAttendance() {
        if (selectedEmployeeId == -1 || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa? ");
        if (confirm == JOptionPane.YES_OPTION) {
            if (attendanceDAO. deleteAttendance(selectedEmployeeId, selectedDate)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadAttendanceData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void loadSelectedAttendance() {
        int row = attendanceTable.getSelectedRow();
        if (row == -1) return;
        
        // Get data from table (simplified - you may need to fetch from DB)
        selectedDate = (LocalDate) tableModel.getValueAt(row, 1);
        
        // Find employee
        String empName = tableModel.getValueAt(row, 0).toString();
        for (int i = 0; i < cboEmployee.getItemCount(); i++) {
            if (cboEmployee.getItemAt(i).getFullName().equals(empName)) {
                cboEmployee.setSelectedIndex(i);
                selectedEmployeeId = cboEmployee.getItemAt(i).getId();
                break;
            }
        }
        
        // Set other fields
        spnDate.setValue(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        txtWorkingHours.setText(tableModel.getValueAt(row, 5).toString());
        txtOvertimeHours.setText(tableModel.getValueAt(row, 6).toString());
        txtLateMinutes.setText(tableModel.getValueAt(row, 7).toString());
        txtNotes.setText(tableModel.getValueAt(row, 8) != null ? tableModel.getValueAt(row, 8).toString() : "");
    }
    
    private Attendance getAttendanceFromForm() {
        Attendance attendance = new Attendance();
        
        Employee emp = (Employee) cboEmployee.getSelectedItem();
        attendance.setEmployeeId(emp. getId());
        
        Date date = (Date) spnDate.getValue();
        attendance.setAttendanceDate(date. toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        Date checkInDate = (Date) spnCheckIn.getValue();
        attendance.setCheckInTime(checkInDate. toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        
        Date checkOutDate = (Date) spnCheckOut.getValue();
        attendance.setCheckOutTime(checkOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        
        attendance.setStatus(cboStatus.getSelectedItem().toString());
        attendance.setWorkingHours(Double.parseDouble(txtWorkingHours.getText().isEmpty() ? "0" : txtWorkingHours.getText()));
        attendance.setOvertimeHours(Double.parseDouble(txtOvertimeHours.getText().isEmpty() ? "0" : txtOvertimeHours.getText()));
        attendance.setLateMinutes(Integer.parseInt(txtLateMinutes.getText().isEmpty() ? "0" : txtLateMinutes. getText()));
        attendance.setNotes(txtNotes.getText());
        
        return attendance;
    }
    
    private boolean validateForm() {
        if (cboEmployee.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        selectedEmployeeId = -1;
        selectedDate = null;
        if (cboEmployee.getItemCount() > 0) cboEmployee.setSelectedIndex(0);
        spnDate.setValue(new Date());
        spnCheckIn.setValue(new Date());
        spnCheckOut. setValue(new Date());
        cboStatus.setSelectedIndex(0);
        txtWorkingHours.setText("");
        txtOvertimeHours.setText("");
        txtLateMinutes.setText("");
        txtNotes.setText("");
        attendanceTable.clearSelection();
    }
} 