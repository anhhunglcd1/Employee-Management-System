package view;

import dao.ContractDAO;
import dao.EmployeeDAO;
import model.Contract;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java. time.ZoneId;
import java.util.Date;
import java.util.List;

public class ContractPanel extends JPanel {
    private ContractDAO contractDAO;
    private EmployeeDAO employeeDAO;
    
    private JTable contractTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<Employee> cboEmployee;
    private JComboBox<String> cboContractType, cboStatus;
    private JTextField txtContractNumber, txtSalary, txtAllowance, txtSalaryCoefficient, txtPosition, txtWorkLocation;
    private JSpinner spnStartDate, spnEndDate;
    private JTextArea txtNotes;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnExpiring;
    
    private int selectedContractId = -1;
    
    public ContractPanel() {
        contractDAO = new ContractDAO();
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
        String[] columns = {"ID", "Nhân Viên", "Loại HĐ", "Số HĐ", "Ngày BĐ", "Ngày KT", 
                           "Lương CB", "Phụ Cấp", "Hệ Số", "Chức Vụ", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        contractTable = new JTable(tableModel);
        contractTable.setRowHeight(25);
        
        // Hide ID
        contractTable.getColumnModel().getColumn(0).setMinWidth(0);
        contractTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Form
        cboEmployee = new JComboBox<>();
        cboContractType = new JComboBox<>(new String[]{"PROBATION", "FIXED_TERM", "INDEFINITE"});
        cboStatus = new JComboBox<>(new String[]{"ACTIVE", "EXPIRED", "TERMINATED"});
        
        txtContractNumber = new JTextField(20);
        txtSalary = new JTextField(20);
        txtAllowance = new JTextField(20);
        txtSalaryCoefficient = new JTextField(20);
        txtPosition = new JTextField(20);
        txtWorkLocation = new JTextField(20);
        
        spnStartDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(spnStartDate, "dd/MM/yyyy");
        spnStartDate.setEditor(startEditor);
        
        spnEndDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner. DateEditor(spnEndDate, "dd/MM/yyyy");
        spnEndDate.setEditor(endEditor);
        
        txtNotes = new JTextArea(3, 20);
        
        // Buttons
        btnAdd = createButton("➕ Thêm", new Color(46, 204, 113));
        btnUpdate = createButton("✏️ Cập Nhật", new Color(52, 152, 219));
        btnDelete = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClear = createButton("🔄 Làm Mới", new Color(149, 165, 166));
        btnExpiring = createButton("⚠️ HĐ Sắp Hết Hạn", new Color(230, 126, 34));
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
        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnExpiring);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory. createTitledBorder("Thông Tin Hợp Đồng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboEmployee, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Loại HĐ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboContractType, gbc);
        
        // Row 1
        gbc. gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Số HĐ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtContractNumber, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cboStatus, gbc);
        
        // Row 2
        gbc. gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Ngày Bắt Đầu: "), gbc);
        gbc.gridx = 1;
        formPanel.add(spnStartDate, gbc);
        
        gbc.gridx = 2;
        formPanel. add(new JLabel("Ngày Kết Thúc:"), gbc);
        gbc.gridx = 3;
        formPanel.add(spnEndDate, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel. add(new JLabel("Lương CB:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtSalary, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Phụ Cấp:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtAllowance, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Hệ Số Lương:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtSalaryCoefficient, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Chức Vụ: "), gbc);
        gbc.gridx = 3;
        formPanel.add(txtPosition, gbc);
        
        // Row 5
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Nơi Làm Việc: "), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(txtWorkLocation, gbc);
        
        // Row 6
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(txtNotes), gbc);
        
        // Row 7 - Buttons
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel, gbc);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(contractTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Hợp Đồng"));
        
        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel. add(topPanel, BorderLayout. NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout. NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        btnAdd.addActionListener(e -> addContract());
        btnUpdate.addActionListener(e -> updateContract());
        btnDelete.addActionListener(e -> deleteContract());
        btnClear.addActionListener(e -> clearForm());
        btnExpiring.addActionListener(e -> showExpiringContracts());
        
        contractTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedContract();
            }
        });
    }
    
    private void loadData() {
        loadEmployees();
        loadContracts();
    }
    
    private void loadEmployees() {
        cboEmployee.removeAllItems();
        List<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee emp : employees) {
            cboEmployee.addItem(emp);
        }
    }
    
    private void loadContracts() {
        tableModel.setRowCount(0);
        List<Contract> contracts = contractDAO.getAllContracts();
        
        for (Contract contract : contracts) {
            Employee emp = employeeDAO.getEmployeeById(contract.getEmployeeId());
            
            Object[] row = {
                contract. getId(),
                emp != null ? emp.getFullName() : "",
                contract.getContractType(),
                contract.getContractNumber(),
                contract. getStartDate(),
                contract. getEndDate(),
                contract. getSalary(),
                contract.getAllowance(),
                contract.getSalaryCoefficient(),
                contract. getPosition(),
                contract.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showExpiringContracts() {
        List<Contract> expiring = contractDAO.getExpiringContracts();
        
        if (expiring.isEmpty()) {
            JOptionPane. showMessageDialog(this, "Không có hợp đồng sắp hết hạn!");
            return;
        }
        
        StringBuilder message = new StringBuilder("Hợp đồng sắp hết hạn (30 ngày):\n\n");
        for (Contract c : expiring) {
            message.append("- ").append(c.getContractNumber())
                   .append(" (").append(c.getEndDate()).append(")\n");
        }
        
        JOptionPane. showMessageDialog(this, message. toString(), 
            "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
    }
    
    private void addContract() {
        if (! validateForm()) return;
        
        Contract contract = getContractFromForm();
        
        // Kiểm tra nhân viên đã có hợp đồng còn hạn chưa
        if (contractDAO.hasActiveContract(contract.getEmployeeId())) {
            JOptionPane.showMessageDialog(this, 
                "Nhân viên đã có hợp đồng còn hiệu lực!\nVui lòng kết thúc hợp đồng cũ trước khi thêm mới.",
                "Cảnh Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (contractDAO.addContract(contract)) {
            JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!");
            loadContracts();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm hợp đồng thất bại!");
        }
    }
    
    private void updateContract() {
        if (selectedContractId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần cập nhật!");
            return;
        }
        
        if (!validateForm()) return;
        
        Contract contract = getContractFromForm();
        contract.setId(selectedContractId);
        
        if (contractDAO.updateContract(contract)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadContracts();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void deleteContract() {
        if (selectedContractId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa? ");
        if (confirm == JOptionPane.YES_OPTION) {
            if (contractDAO.deleteContract(selectedContractId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadContracts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void loadSelectedContract() {
        int row = contractTable.getSelectedRow();
        if (row == -1) return;
        
        selectedContractId = (int) tableModel.getValueAt(row, 0);
        
        // Set fields
        String empName = tableModel.getValueAt(row, 1).toString();
        for (int i = 0; i < cboEmployee.getItemCount(); i++) {
            if (cboEmployee.getItemAt(i).getFullName().equals(empName)) {
                cboEmployee.setSelectedIndex(i);
                break;
            }
        }
        
        cboContractType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        txtContractNumber.setText(tableModel.getValueAt(row, 3).toString());
        
        LocalDate startDate = (LocalDate) tableModel.getValueAt(row, 4);
        spnStartDate.setValue(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        
        Object endDateObj = tableModel.getValueAt(row, 5);
        if (endDateObj != null && endDateObj instanceof LocalDate) {
            LocalDate endDate = (LocalDate) endDateObj;
            spnEndDate.setValue(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        
        txtSalary. setText(tableModel.getValueAt(row, 6).toString());
        txtAllowance.setText(tableModel.getValueAt(row, 7).toString());
        txtSalaryCoefficient.setText(tableModel.getValueAt(row, 8).toString());
        txtPosition.setText(tableModel.getValueAt(row, 9) != null ? tableModel.getValueAt(row, 9).toString() : "");
        cboStatus.setSelectedItem(tableModel.getValueAt(row, 10).toString());
    }
    
    private Contract getContractFromForm() {
        Contract contract = new Contract();
        
        Employee emp = (Employee) cboEmployee.getSelectedItem();
        contract.setEmployeeId(emp. getId());
        
        contract. setContractType(cboContractType.getSelectedItem().toString());
        contract.setContractNumber(txtContractNumber.getText());
        
        Date startDate = (Date) spnStartDate.getValue();
        contract.setStartDate(startDate. toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        Date endDate = (Date) spnEndDate.getValue();
        contract.setEndDate(endDate. toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        contract.setSalary(Double.parseDouble(txtSalary.getText()));
        contract.setAllowance(Double.parseDouble(txtAllowance.getText().isEmpty() ? "0" : txtAllowance.getText()));
        contract.setSalaryCoefficient(Double.parseDouble(txtSalaryCoefficient.getText().isEmpty() ? "2.34" : txtSalaryCoefficient.getText()));
        contract.setPosition(txtPosition.getText());
        contract.setWorkLocation(txtWorkLocation.getText());
        contract.setStatus(cboStatus.getSelectedItem().toString());
        contract.setNotes(txtNotes.getText());
        
        return contract;
    }
    
    private boolean validateForm() {
        if (cboEmployee.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return false;
        }
        if (txtContractNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp đồng!");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        selectedContractId = -1;
        if (cboEmployee.getItemCount() > 0) cboEmployee.setSelectedIndex(0);
        cboContractType.setSelectedIndex(0);
        cboStatus.setSelectedIndex(0);
        txtContractNumber. setText("");
        txtSalary.setText("");
        txtAllowance.setText("");
        txtSalaryCoefficient.setText("2.34");
        txtPosition. setText("");
        txtWorkLocation. setText("");
        spnStartDate.setValue(new Date());
        spnEndDate.setValue(new Date());
        txtNotes.setText("");
        contractTable.clearSelection();
    }
}