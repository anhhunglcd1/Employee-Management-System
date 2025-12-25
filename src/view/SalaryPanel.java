package view;

import dao.SalaryDAO;
import dao. EmployeeDAO;
import model.Salary;
import model. Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util. List;
import java.util.Locale;

public class SalaryPanel extends JPanel {
    private SalaryDAO salaryDAO;
    private EmployeeDAO employeeDAO;
    
    private JTable salaryTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<Employee> cboEmployee;
    private JSpinner spnMonth, spnYear, spnFilterMonth, spnFilterYear;
    private JTextField txtBaseSalary, txtSalaryCoefficient, txtAllowance, txtOvertimePay, txtBonus, txtOtherIncome;
    private JTextField txtLateDeduction, txtAbsentDeduction, txtInsurance, txtTax, txtOtherDeduction;
    private JTextField txtGrossSalary, txtTotalDeduction, txtNetSalary;
    private JTextField txtWorkingDays, txtStandardDays, txtOvertimeHours;
    private JComboBox<String> cboStatus;
    private JTextArea txtNotes;
    
    private JButton btnCalculate, btnSave, btnApprove, btnDelete, btnClear, btnFilter;
    
    private NumberFormat currencyFormat;
    
    public SalaryPanel() {
        salaryDAO = new SalaryDAO();
        employeeDAO = new EmployeeDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        setupLayout();
        loadData();
        setupEventListeners();
    }
    
    private void initComponents() {
        // Table
        String[] columns = {"NV", "Tháng", "Năm", "Lương CB", "Hệ Số", "Phụ Cấp", "Tăng Ca", 
                           "Thưởng", "Khấu Trừ", "Thực Lãnh", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        salaryTable = new JTable(tableModel);
        salaryTable.setRowHeight(25);
        
        // Form
        cboEmployee = new JComboBox<>();
        spnMonth = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spnYear = new JSpinner(new SpinnerNumberModel(2025, 2020, 2100, 1));
        
        // Filter spinners
        spnFilterMonth = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spnFilterYear = new JSpinner(new SpinnerNumberModel(2025, 2020, 2100, 1));
        
        txtBaseSalary = new JTextField(15);
        txtSalaryCoefficient = new JTextField(15);
        txtSalaryCoefficient.setEditable(false);
        txtSalaryCoefficient.setBackground(Color.LIGHT_GRAY);
        txtAllowance = new JTextField(15);
        txtOvertimePay = new JTextField(15);
        txtBonus = new JTextField(15);
        txtOtherIncome = new JTextField(15);
        
        txtLateDeduction = new JTextField(15);
        txtAbsentDeduction = new JTextField(15);
        txtInsurance = new JTextField(15);
        txtTax = new JTextField(15);
        txtOtherDeduction = new JTextField(15);
        
        txtGrossSalary = new JTextField(15);
        txtGrossSalary.setEditable(false);
        txtGrossSalary.setBackground(Color.LIGHT_GRAY);
        
        txtTotalDeduction = new JTextField(15);
        txtTotalDeduction.setEditable(false);
        txtTotalDeduction.setBackground(Color. LIGHT_GRAY);
        
        txtNetSalary = new JTextField(15);
        txtNetSalary.setEditable(false);
        txtNetSalary.setBackground(new Color(144, 238, 144));
        txtNetSalary.setFont(new Font("Arial", Font. BOLD, 14));
        
        txtWorkingDays = new JTextField(15);
        txtStandardDays = new JTextField(15);
        txtOvertimeHours = new JTextField(15);
        
        cboStatus = new JComboBox<>(new String[]{"PENDING", "APPROVED", "PAID"});
        txtNotes = new JTextArea(3, 20);
        
        // Buttons
        btnCalculate = createButton("Tính Lương (SP)", new Color(241, 196, 15));
        btnSave = createButton("Lưu", new Color(46, 204, 113));
        btnApprove = createButton("Duyệt", new Color(52, 152, 219));
        btnDelete = createButton("Xóa", new Color(231, 76, 60));
        btnClear = createButton("Làm Mới", new Color(149, 165, 166));
        btnFilter = createButton("Lọc", new Color(155, 89, 182));
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font. BOLD, 11));
        return button;
    }
    
    private void setupLayout() {
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Tháng:"));
        filterPanel.add(spnFilterMonth);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(spnFilterYear);
        filterPanel.add(btnFilter);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory. createTitledBorder("Bảng Lương"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Employee & Period
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboEmployee, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tháng: "), gbc);
        gbc.gridx = 3;
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodPanel.add(spnMonth);
        periodPanel. add(new JLabel("/"));
        periodPanel.add(spnYear);
        formPanel.add(periodPanel, gbc);
        
        row++;
        
        // Income Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        JLabel lblIncome = new JLabel("═══ THU NHẬP ═══");
        lblIncome. setFont(new Font("Arial", Font. BOLD, 12));
        lblIncome.setForeground(new Color(46, 204, 113));
        formPanel.add(lblIncome, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Lương CB:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtBaseSalary, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Hệ Số Lương:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtSalaryCoefficient, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phụ Cấp:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAllowance, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tăng Ca:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtOvertimePay, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Thưởng:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtBonus, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Thu Khác:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtOtherIncome, gbc);
        
        row++;
        
        // Deduction Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        JLabel lblDeduction = new JLabel("═══ KHẤU TRỪ ═══");
        lblDeduction.setFont(new Font("Arial", Font.BOLD, 12));
        lblDeduction.setForeground(new Color(231, 76, 60));
        formPanel.add(lblDeduction, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel. add(new JLabel("Trừ Trễ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLateDeduction, gbc);
        
        gbc. gridx = 2;
        formPanel.add(new JLabel("Trừ Vắng:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtAbsentDeduction, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc. gridy = row;
        formPanel.add(new JLabel("Bảo Hiểm:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtInsurance, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Thuế:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtTax, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Trừ Khác:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtOtherDeduction, gbc);
        
        row++;
        
        // Summary Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        JLabel lblSummary = new JLabel("═══ TỔNG HỢP ═══");
        lblSummary.setFont(new Font("Arial", Font. BOLD, 12));
        lblSummary.setForeground(new Color(52, 152, 219));
        formPanel.add(lblSummary, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tổng Thu: "), gbc);
        gbc.gridx = 1;
        formPanel.add(txtGrossSalary, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tổng Trừ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtTotalDeduction, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblNet = new JLabel("THỰC LÃNH:");
        lblNet.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(lblNet, gbc);
        gbc.gridx = 1;
        formPanel.add(txtNetSalary, gbc);
        
        row++;
        
        // Working Info
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Ngày Công: "), gbc);
        gbc.gridx = 1;
        formPanel.add(txtWorkingDays, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày Chuẩn:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtStandardDays, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Giờ TC:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtOvertimeHours, gbc);
        
        gbc. gridx = 2;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboStatus, gbc);
        
        row++;
        
        gbc.gridx = 0; gbc. gridy = row;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(txtNotes), gbc);
        
        row++;
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnCalculate);
        btnPanel.add(btnSave);
        btnPanel.add(btnApprove);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel, gbc);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(salaryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Bảng Lương"));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout. NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, scrollPane);
        splitPane.setDividerLocation(450);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        btnCalculate.addActionListener(e -> calculateSalaryBySP());
        btnSave.addActionListener(e -> saveSalary());
        btnApprove.addActionListener(e -> approveSalary());
        btnDelete.addActionListener(e -> deleteSalary());
        btnClear.addActionListener(e -> clearForm());
        btnFilter.addActionListener(e -> filterByPeriod());
        
        salaryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedSalary();
            }
        });
    }
    
    private void loadData() {
        loadEmployees();
        loadSalaryData();
    }
    
    private void loadEmployees() {
        cboEmployee.removeAllItems();
        List<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee emp : employees) {
            cboEmployee.addItem(emp);
        }
    }
    
    private void loadSalaryData() {
        tableModel.setRowCount(0);
        List<Salary> salaries = salaryDAO.getAllSalaries();
        
        for (Salary salary : salaries) {
            Employee emp = employeeDAO.getEmployeeById(salary.getEmployeeId());
            
            Object[] row = {
                emp != null ? emp.getFullName() : "",
                salary. getSalaryMonth(),
                salary. getSalaryYear(),
                currencyFormat. format(salary.getBaseSalary()),
                String.format("%.2f", salary.getSalaryCoefficient()),
                currencyFormat.format(salary.getAllowance()),
                currencyFormat. format(salary.getOvertimePay()),
                currencyFormat.format(salary.getBonus()),
                currencyFormat.format(salary.getTotalDeduction()),
                currencyFormat.format(salary.getNetSalary()),
                salary.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterByPeriod() {
        int month = (int) spnFilterMonth.getValue();
        int year = (int) spnFilterYear.getValue();
        
        tableModel.setRowCount(0);
        List<Salary> salaries = salaryDAO.getSalariesByPeriod(month, year);
        
        for (Salary salary : salaries) {
            Employee emp = employeeDAO.getEmployeeById(salary.getEmployeeId());
            
            Object[] row = {
                emp != null ? emp.getFullName() : "",
                salary.getSalaryMonth(),
                salary.getSalaryYear(),
                currencyFormat.format(salary.getBaseSalary()),                String.format("%.2f", salary.getSalaryCoefficient()),                currencyFormat.format(salary.getAllowance()),
                currencyFormat.format(salary.getOvertimePay()),
                currencyFormat. format(salary.getBonus()),
                currencyFormat.format(salary.getTotalDeduction()),
                currencyFormat.format(salary.getNetSalary()),
                salary.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void calculateSalaryBySP() {
        if (cboEmployee.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return;
        }
        
        Employee emp = (Employee) cboEmployee.getSelectedItem();
        int month = (int) spnMonth.getValue();
        int year = (int) spnYear.getValue();
        
        if (salaryDAO.calculateMonthlySalary(emp. getId(), month, year)) {
            JOptionPane.showMessageDialog(this, "Tính lương thành công!");
            
            // Load salary data
            Salary salary = salaryDAO.getSalaryByEmployeeAndPeriod(emp.getId(), month, year);
            if (salary != null) {
                displaySalary(salary);
            }
            
            loadSalaryData();
        } else {
            JOptionPane. showMessageDialog(this, "Tính lương thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displaySalary(Salary salary) {
        txtBaseSalary.setText(String.valueOf(salary.getBaseSalary()));
        txtSalaryCoefficient.setText(String.format("%.2f", salary.getSalaryCoefficient()));
        txtAllowance.setText(String.valueOf(salary.getAllowance()));
        txtOvertimePay.setText(String.valueOf(salary.getOvertimePay()));
        txtBonus.setText(String. valueOf(salary.getBonus()));
        txtOtherIncome.setText(String.valueOf(salary.getOtherIncome()));
        
        txtLateDeduction.setText(String. valueOf(salary.getLateDeduction()));
        txtAbsentDeduction.setText(String. valueOf(salary.getAbsentDeduction()));
        txtInsurance. setText(String.valueOf(salary. getInsuranceDeduction()));
        txtTax.setText(String.valueOf(salary.getTaxDeduction()));
        txtOtherDeduction.setText(String.valueOf(salary.getOtherDeduction()));
        
        txtGrossSalary.setText(currencyFormat.format(salary.getGrossSalary()));
        txtTotalDeduction.setText(currencyFormat.format(salary.getTotalDeduction()));
        txtNetSalary.setText(currencyFormat.format(salary.getNetSalary()));
        
        txtWorkingDays.setText(String.valueOf(salary.getWorkingDays()));
        txtStandardDays.setText(String.valueOf(salary.getStandardDays()));
        txtOvertimeHours.setText(String.valueOf(salary.getOvertimeHours()));
        
        cboStatus.setSelectedItem(salary.getStatus());
        txtNotes.setText(salary.getNotes() != null ? salary.getNotes() : "");
    }
    
    private void saveSalary() {
        JOptionPane.showMessageDialog(this, "Chức năng đang phát triển!");
    }
    
    private void approveSalary() {
        if (cboEmployee.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bảng lương!");
            return;
        }
        
        Employee emp = (Employee) cboEmployee.getSelectedItem();
        int month = (int) spnMonth.getValue();
        int year = (int) spnYear.getValue();
        
        if (salaryDAO.approveSalary(emp.getId(), month, year, 0)) { // Approver ID = 0 (NULL)
            JOptionPane.showMessageDialog(this, "Duyệt lương thành công!");
            loadSalaryData();
            cboStatus.setSelectedItem("APPROVED");
        } else {
            JOptionPane.showMessageDialog(this, "Duyệt lương thất bại!");
        }
    }
    
    private void deleteSalary() {
        JOptionPane.showMessageDialog(this, "Chức năng đang phát triển!");
    }
    
    private void loadSelectedSalary() {
        // Implementation here
    }
    
    private void clearForm() {
        if (cboEmployee.getItemCount() > 0) cboEmployee.setSelectedIndex(0);
        spnMonth.setValue(1);
        spnYear.setValue(2025);
        spnFilterMonth.setValue(1);
        spnFilterYear.setValue(2025);
        txtBaseSalary.setText("");
        txtSalaryCoefficient.setText("");
        txtAllowance.setText("");
        txtOvertimePay.setText("");
        txtBonus.setText("");
        txtOtherIncome. setText("");
        txtLateDeduction.setText("");
        txtAbsentDeduction.setText("");
        txtInsurance.setText("");
        txtTax.setText("");
        txtOtherDeduction.setText("");
        txtGrossSalary.setText("");
        txtTotalDeduction.setText("");
        txtNetSalary.setText("");
        txtWorkingDays. setText("");
        txtStandardDays.setText("22");
        txtOvertimeHours.setText("");
        txtNotes.setText("");
        cboStatus.setSelectedIndex(0);
    }
}