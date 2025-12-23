package view;

import dao.EmployeeDAO;
import dao.DepartmentDAO;
import dao. PositionDAO;
import model.Employee;
import model.Department;
import model.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java. awt.*;
import java.time.LocalDate;
import java. time.ZoneId;
import java.util.Date;
import java.util.List;

public class EmployeePanel extends JPanel {
    // DAO
    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;
    private PositionDAO positionDAO;
    
    // Table
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    // Form Components
    private JTextField txtEmployeeCode, txtFullName, txtIdCard, txtPhone, txtEmail, txtAddress, txtSearch;
    private JComboBox<String> cboGender, cboStatus;
    private JComboBox<Department> cboDepartment;
    private JComboBox<Position> cboPosition;
    private JSpinner spnDateOfBirth, spnJoinDate;
    
    // Buttons
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh, btnSearch;
    
    // Selected Employee ID
    private int selectedEmployeeId = -1;
    
    public EmployeePanel() {
        // Khởi tạo DAO
        employeeDAO = new EmployeeDAO();
        departmentDAO = new DepartmentDAO();
        positionDAO = new PositionDAO();
        
        // Setup Panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        setupLayout();
        loadData();
        setupEventListeners();
    }
    
    private void initComponents() {
        // === TABLE ===
        String[] columns = {"ID", "Mã NV", "Họ Tên", "Ngày Sinh", "Giới Tính", 
                           "CCCD", "SĐT", "Email", "Phòng Ban", "Chức Vụ", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel. SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font. BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(52, 152, 219));
//        employeeTable.getTableHeader().setForeground(Color.WHITE);
        
        // Hide ID column
        employeeTable.getColumnModel().getColumn(0).setMinWidth(0);
        employeeTable.getColumnModel().getColumn(0).setMaxWidth(0);
        employeeTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Row sorter
        sorter = new TableRowSorter<>(tableModel);
        employeeTable. setRowSorter(sorter);
        
        // === FORM FIELDS ===
        txtEmployeeCode = new JTextField(20);
        txtFullName = new JTextField(20);
        txtIdCard = new JTextField(20);
        txtPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtAddress = new JTextField(20);
        txtSearch = new JTextField(30);
        
        cboGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cboStatus = new JComboBox<>(new String[]{"Đang làm việc", "Đang nghỉ phép", "Đã nghỉ việc"});
        
        cboDepartment = new JComboBox<>();
        cboPosition = new JComboBox<>();
        
        // Date Spinners
        SpinnerDateModel dobModel = new SpinnerDateModel();
        spnDateOfBirth = new JSpinner(dobModel);
        JSpinner.DateEditor dobEditor = new JSpinner.DateEditor(spnDateOfBirth, "dd/MM/yyyy");
        spnDateOfBirth.setEditor(dobEditor);
        
        SpinnerDateModel joinModel = new SpinnerDateModel();
        spnJoinDate = new JSpinner(joinModel);
        JSpinner.DateEditor joinEditor = new JSpinner. DateEditor(spnJoinDate, "dd/MM/yyyy");
        spnJoinDate. setEditor(joinEditor);
        
        // === BUTTONS ===
        btnAdd = createButton("➕ Thêm", new Color(46, 204, 113));
        btnUpdate = createButton("✏️ Cập Nhật", new Color(52, 152, 219));
        btnDelete = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClear = createButton("🔄 Làm Mới", new Color(149, 165, 166));
        btnRefresh = createButton("↻ Tải Lại", new Color(241, 196, 15));
        btnSearch = createButton("🔍 Tìm Kiếm", new Color(155, 89, 182));
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
    
    private void setupLayout() {
        // === TOP PANEL:  SEARCH ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        // === FORM PANEL ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Nhân Viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmployeeCode, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtFullName, gbc);
        
        // Row 1
        gbc. gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ngày Sinh: "), gbc);
        gbc.gridx = 1;
        formPanel.add(spnDateOfBirth, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboGender, gbc);
        
        // Row 2
        gbc. gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdCard, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtPhone, gbc);
        
        // Row 3
        gbc. gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel. add(txtEmail, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtAddress, gbc);
        
        // Row 4
        gbc. gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Phòng Ban: "), gbc);
        gbc.gridx = 1;
        formPanel.add(cboDepartment, gbc);
        
        gbc.gridx = 2;
        formPanel. add(new JLabel("Chức Vụ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboPosition, gbc);
        
        // Row 5
        gbc. gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Ngày Vào: "), gbc);
        gbc.gridx = 1;
        formPanel.add(spnJoinDate, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboStatus, gbc);
        
        // === BUTTON PANEL ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout. CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);
        
        // === TABLE PANEL ===
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Nhân Viên"));
        
        // === MAIN LAYOUT ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout. NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout. NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        // Button events
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadEmployeeData());
        btnSearch.addActionListener(e -> searchEmployees());
        
        // Table selection event
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (! e.getValueIsAdjusting()) {
                loadSelectedEmployee();
            }
        });
        
        // Enter key for search
        txtSearch.addActionListener(e -> searchEmployees());
    }
    
    private void loadData() {
        loadDepartments();
        loadPositions();
        loadEmployeeData();
    }
    
    private void loadDepartments() {
        cboDepartment.removeAllItems();
        List<Department> departments = departmentDAO.getAllDepartments();
        for (Department dept : departments) {
            cboDepartment.addItem(dept);
        }
    }
    
    private void loadPositions() {
        cboPosition.removeAllItems();
        List<Position> positions = positionDAO.getAllPositions();
        for (Position pos : positions) {
            cboPosition.addItem(pos);
        }
    }
    
    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeDAO. getAllEmployees();
        
        for (Employee emp : employees) {
            Department dept = departmentDAO.getDepartmentById(emp.getDepartmentId());
            Position pos = positionDAO. getPositionById(emp.getPositionId());
            
            Object[] row = {
                emp. getId(),
                emp.getEmployeeCode(),
                emp.getFullName(),
                emp.getDateOfBirth(),
                emp.getGender(),
                emp.getIdCard(),
                emp.getPhone(),
                emp. getEmail(),
                dept != null ? dept.getName() : "",
                pos != null ? pos. getName() : "",
                emp.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchEmployees() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword. isEmpty()) {
            loadEmployeeData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Employee> employees = employeeDAO.searchEmployees(keyword);
        
        for (Employee emp : employees) {
            Department dept = departmentDAO.getDepartmentById(emp.getDepartmentId());
            Position pos = positionDAO.getPositionById(emp.getPositionId());
            
            Object[] row = {
                emp.getId(),
                emp.getEmployeeCode(),
                emp.getFullName(),
                emp.getDateOfBirth(),
                emp.getGender(),
                emp.getIdCard(),
                emp.getPhone(),
                emp.getEmail(),
                dept != null ? dept. getName() : "",
                pos != null ? pos.getName() : "",
                emp.getStatus()
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Tìm thấy " + employees.size() + " kết quả!", 
            "Kết Quả Tìm Kiếm", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addEmployee() {
        if (! validateForm()) return;
        
        Employee employee = getEmployeeFromForm();
        
        // Kiểm tra mã nhân viên trùng
        if (employeeDAO.isEmployeeCodeExists(employee.getEmployeeCode(), 0)) {
            JOptionPane. showMessageDialog(this, 
                "Mã nhân viên đã tồn tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (employeeDAO.addEmployee(employee)) {
            JOptionPane.showMessageDialog(this, 
                "Thêm nhân viên thành công!", 
                "Thành Công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadEmployeeData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Thêm nhân viên thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn nhân viên cần cập nhật!", 
                "Cảnh Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) return;
        
        Employee employee = getEmployeeFromForm();
        employee.setId(selectedEmployeeId);
        
        // Kiểm tra mã nhân viên trùng (trừ chính nó)
        if (employeeDAO.isEmployeeCodeExists(employee.getEmployeeCode(), selectedEmployeeId)) {
            JOptionPane.showMessageDialog(this, 
                "Mã nhân viên đã tồn tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (employeeDAO.updateEmployee(employee)) {
            JOptionPane. showMessageDialog(this, 
                "Cập nhật nhân viên thành công!", 
                "Thành Công", 
                JOptionPane. INFORMATION_MESSAGE);
            loadEmployeeData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật nhân viên thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn nhân viên cần xóa!", 
                "Cảnh Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa nhân viên này?\n" +
            "Hành động này không thể hoàn tác!", 
            "Xác Nhận Xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (employeeDAO.deleteEmployee(selectedEmployeeId)) {
                JOptionPane.showMessageDialog(this, 
                    "Xóa nhân viên thành công!", 
                    "Thành Công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadEmployeeData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa nhân viên thất bại!\nCó thể nhân viên đang có dữ liệu liên quan.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            selectedEmployeeId = -1;
            return;
        }
        
        selectedEmployeeId = (int) employeeTable.getValueAt(selectedRow, 0);
        Employee employee = employeeDAO.getEmployeeById(selectedEmployeeId);
        
        if (employee != null) {
            txtEmployeeCode.setText(employee.getEmployeeCode());
            txtFullName.setText(employee.getFullName());
            txtIdCard.setText(employee.getIdCard());
            txtPhone.setText(employee.getPhone());
            txtEmail.setText(employee.getEmail());
            txtAddress.setText(employee.getAddress());
            cboGender.setSelectedItem(employee.getGender());
            cboStatus.setSelectedItem(employee.getStatus());
            
            if (employee.getDateOfBirth() != null) {
                spnDateOfBirth.setValue(Date.from(employee.getDateOfBirth()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            
            if (employee.getJoinDate() != null) {
                spnJoinDate.setValue(Date.from(employee.getJoinDate()
                    . atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            
            // Set department
            for (int i = 0; i < cboDepartment.getItemCount(); i++) {
                if (cboDepartment.getItemAt(i).getId() == employee.getDepartmentId()) {
                    cboDepartment.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set position
            for (int i = 0; i < cboPosition.getItemCount(); i++) {
                if (cboPosition.getItemAt(i).getId() == employee.getPositionId()) {
                    cboPosition.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private Employee getEmployeeFromForm() {
        Employee employee = new Employee();
        
        employee.setEmployeeCode(txtEmployeeCode.getText().trim());
        employee.setFullName(txtFullName.getText().trim());
        employee.setIdCard(txtIdCard.getText().trim());
        employee.setPhone(txtPhone.getText().trim());
        employee.setEmail(txtEmail.getText().trim());
        employee.setAddress(txtAddress.getText().trim());
        employee.setGender(cboGender. getSelectedItem().toString());
        employee.setStatus(cboStatus.getSelectedItem().toString());
        
        // Department & Position
        Department dept = (Department) cboDepartment.getSelectedItem();
        if (dept != null) {
            employee.setDepartmentId(dept.getId());
        }
        
        Position pos = (Position) cboPosition.getSelectedItem();
        if (pos != null) {
            employee.setPositionId(pos.getId());
        }
        
        // Dates
        Date dob = (Date) spnDateOfBirth.getValue();
        employee.setDateOfBirth(dob. toInstant().atZone(ZoneId. systemDefault()).toLocalDate());
        
        Date joinDate = (Date) spnJoinDate.getValue();
        employee.setJoinDate(joinDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        return employee;
    }
    
    private boolean validateForm() {
        if (txtEmployeeCode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane. ERROR_MESSAGE);
            txtEmployeeCode.requestFocus();
            return false;
        }
        
        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtFullName.requestFocus();
            return false;
        }
        
        if (cboDepartment.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng ban!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (cboPosition.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        selectedEmployeeId = -1;
        txtEmployeeCode.setText("");
        txtFullName.setText("");
        txtIdCard.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        cboGender.setSelectedIndex(0);
        cboStatus. setSelectedIndex(0);
        spnDateOfBirth.setValue(new Date());
        spnJoinDate.setValue(new Date());
        
        if (cboDepartment.getItemCount() > 0) {
            cboDepartment.setSelectedIndex(0);
        }
        
        if (cboPosition.getItemCount() > 0) {
            cboPosition. setSelectedIndex(0);
        }
        
        employeeTable.clearSelection();
    }
}