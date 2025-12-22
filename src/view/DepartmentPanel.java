package view;

import dao.DepartmentDAO;
import dao. PositionDAO;
import model.Department;
import model.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DepartmentPanel extends JPanel {
    private final DepartmentDAO departmentDAO;
    private final PositionDAO positionDAO;
    
    private JTable departmentTable, positionTable;
    private DefaultTableModel deptTableModel, posTableModel;
    
    // Department form
    private JTextField txtDeptCode, txtDeptName, txtDeptDesc;
    private JComboBox<String> cboDeptStatus;
    private JButton btnAddDept, btnUpdateDept, btnDeleteDept, btnClearDept;
    
    // Position form
    private JTextField txtPosCode, txtPosName, txtPosDesc;
    private JComboBox<String> cboPosLevel;
    private JButton btnAddPos, btnUpdatePos, btnDeletePos, btnClearPos;
    
    private int selectedDeptId = -1;
    private int selectedPosId = -1;
    
    public DepartmentPanel() {
        departmentDAO = new DepartmentDAO();
        positionDAO = new PositionDAO();
        
        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        setupLayout();
        loadData();
        setupEventListeners();
    }
    
    private void initComponents() {
        // Department Table
        String[] deptColumns = {"ID", "Mã", "Tên Phòng Ban", "Mô Tả", "Trạng Thái"};
        deptTableModel = new DefaultTableModel(deptColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        departmentTable = new JTable(deptTableModel);
        departmentTable.setRowHeight(25);
        
        // Hide ID
        departmentTable.getColumnModel().getColumn(0).setMinWidth(0);
        departmentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Position Table
        String[] posColumns = {"ID", "Mã", "Tên Chức Vụ", "Cấp Bậc", "Mô Tả"};
        posTableModel = new DefaultTableModel(posColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        positionTable = new JTable(posTableModel);
        positionTable.setRowHeight(25);
        
        // Hide ID
        positionTable.getColumnModel().getColumn(0).setMinWidth(0);
        positionTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Department Form
        txtDeptCode = new JTextField(15);
        txtDeptName = new JTextField(15);
        txtDeptDesc = new JTextField(15);
        cboDeptStatus = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        
        btnAddDept = createButton("➕ Thêm", new Color(46, 204, 113));
        btnUpdateDept = createButton("✏️ Sửa", new Color(52, 152, 219));
        btnDeleteDept = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClearDept = createButton("🔄 Clear", new Color(149, 165, 166));
        
        // Position Form
        txtPosCode = new JTextField(15);
        txtPosName = new JTextField(15);
        txtPosDesc = new JTextField(15);
        cboPosLevel = new JComboBox<>(new String[]{"STAFF", "TEAM_LEADER", "MANAGER", "DIRECTOR"});
        
        btnAddPos = createButton("➕ Thêm", new Color(46, 204, 113));
        btnUpdatePos = createButton("✏️ Sửa", new Color(52, 152, 219));
        btnDeletePos = createButton("🗑️ Xóa", new Color(231, 76, 60));
        btnClearPos = createButton("🔄 Clear", new Color(149, 165, 166));
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        return button;
    }
    
    private void setupLayout() {
        // === DEPARTMENT PANEL ===
        JPanel deptPanel = new JPanel(new BorderLayout(5, 5));
        deptPanel.setBorder(BorderFactory.createTitledBorder("QUẢN LÝ PHÒNG BAN"));
        
        // Form
        JPanel deptFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        deptFormPanel. add(new JLabel("Mã: "), gbc);
        gbc.gridx = 1;
        deptFormPanel.add(txtDeptCode, gbc);
        
        gbc. gridx = 0; gbc.gridy = 1;
        deptFormPanel.add(new JLabel("Tên: "), gbc);
        gbc.gridx = 1;
        deptFormPanel.add(txtDeptName, gbc);
        
        gbc.gridx = 0; gbc. gridy = 2;
        deptFormPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        deptFormPanel. add(txtDeptDesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        deptFormPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        deptFormPanel.add(cboDeptStatus, gbc);
        
        JPanel deptBtnPanel = new JPanel(new FlowLayout());
        deptBtnPanel. add(btnAddDept);
        deptBtnPanel.add(btnUpdateDept);
        deptBtnPanel.add(btnDeleteDept);
        deptBtnPanel.add(btnClearDept);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        deptFormPanel.add(deptBtnPanel, gbc);
        
        deptPanel.add(deptFormPanel, BorderLayout.NORTH);
        deptPanel.add(new JScrollPane(departmentTable), BorderLayout.CENTER);
        
        // === POSITION PANEL ===
        JPanel posPanel = new JPanel(new BorderLayout(5, 5));
        posPanel.setBorder(BorderFactory.createTitledBorder("QUẢN LÝ CHỨC VỤ"));
        
        JPanel posFormPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        posFormPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 1;
        posFormPanel.add(txtPosCode, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        posFormPanel. add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        posFormPanel.add(txtPosName, gbc);
        
        gbc. gridx = 0; gbc.gridy = 2;
        posFormPanel.add(new JLabel("Cấp bậc:"), gbc);
        gbc.gridx = 1;
        posFormPanel.add(cboPosLevel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        posFormPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        posFormPanel.add(txtPosDesc, gbc);
        
        JPanel posBtnPanel = new JPanel(new FlowLayout());
        posBtnPanel.add(btnAddPos);
        posBtnPanel.add(btnUpdatePos);
        posBtnPanel.add(btnDeletePos);
        posBtnPanel.add(btnClearPos);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        posFormPanel.add(posBtnPanel, gbc);
        
        posPanel.add(posFormPanel, BorderLayout.NORTH);
        posPanel.add(new JScrollPane(positionTable), BorderLayout.CENTER);
        
        // Add to main panel
        add(deptPanel);
        add(posPanel);
    }
    
    private void setupEventListeners() {
        // Department buttons
        btnAddDept.addActionListener(e -> addDepartment());
        btnUpdateDept.addActionListener(e -> updateDepartment());
        btnDeleteDept.addActionListener(e -> deleteDepartment());
        btnClearDept.addActionListener(e -> clearDeptForm());
        
        // Position buttons
        btnAddPos.addActionListener(e -> addPosition());
        btnUpdatePos.addActionListener(e -> updatePosition());
        btnDeletePos.addActionListener(e -> deletePosition());
        btnClearPos.addActionListener(e -> clearPosForm());
        
        // Table selections
        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedDepartment();
        });
        
        positionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPosition();
        });
    }
    
    private void loadData() {
        loadDepartments();
        loadPositions();
    }
    
    private void loadDepartments() {
        deptTableModel.setRowCount(0);
        List<Department> departments = departmentDAO. getAllDepartments();
        
        for (Department dept : departments) {
            Object[] row = {
                dept. getId(),
                dept.getCode(),
                dept.getName(),
                dept.getDescription(),
                dept.getStatus()
            };
            deptTableModel.addRow(row);
        }
    }
    
    private void loadPositions() {
        posTableModel.setRowCount(0);
        List<Position> positions = positionDAO.getAllPositions();
        
        for (Position pos : positions) {
            Object[] row = {
                pos. getId(),
                pos.getCode(),
                pos.getName(),
                pos.getLevel(),
                pos.getDescription()
            };
            posTableModel.addRow(row);
        }
    }
    
    private void addDepartment() {
        if (txtDeptCode.getText().trim().isEmpty() || txtDeptName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        Department dept = new Department();
        dept.setCode(txtDeptCode.getText().trim());
        dept.setName(txtDeptName.getText().trim());
        dept.setDescription(txtDeptDesc.getText().trim());
        dept.setStatus(cboDeptStatus.getSelectedItem().toString());
        
        if (departmentDAO.addDepartment(dept)) {
            JOptionPane.showMessageDialog(this, "Thêm phòng ban thành công!");
            loadDepartments();
            clearDeptForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm phòng ban thất bại!");
        }
    }
    
    private void updateDepartment() {
        if (selectedDeptId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng ban!");
            return;
        }
        
        Department dept = new Department();
        dept.setId(selectedDeptId);
        dept.setCode(txtDeptCode.getText().trim());
        dept.setName(txtDeptName.getText().trim());
        dept.setDescription(txtDeptDesc.getText().trim());
        dept.setStatus(cboDeptStatus.getSelectedItem().toString());
        
        if (departmentDAO.updateDepartment(dept)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDepartments();
            clearDeptForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void deleteDepartment() {
        if (selectedDeptId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng ban!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa? ");
        if (confirm == JOptionPane.YES_OPTION) {
            if (departmentDAO. deleteDepartment(selectedDeptId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDepartments();
                clearDeptForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void addPosition() {
        if (txtPosCode.getText().trim().isEmpty() || txtPosName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        Position pos = new Position();
        pos.setCode(txtPosCode.getText().trim());
        pos.setName(txtPosName.getText().trim());
        pos.setLevel(cboPosLevel.getSelectedItem().toString());
        pos.setDescription(txtPosDesc.getText().trim());
        
        if (positionDAO.addPosition(pos)) {
            JOptionPane.showMessageDialog(this, "Thêm chức vụ thành công!");
            loadPositions();
            clearPosForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chức vụ thất bại!");
        }
    }
    
    private void updatePosition() {
        if (selectedPosId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!");
            return;
        }
        
        Position pos = new Position();
        pos.setId(selectedPosId);
        pos.setCode(txtPosCode.getText().trim());
        pos.setName(txtPosName.getText().trim());
        pos.setLevel(cboPosLevel.getSelectedItem().toString());
        pos.setDescription(txtPosDesc.getText().trim());
        
        if (positionDAO.updatePosition(pos)) {
            JOptionPane. showMessageDialog(this, "Cập nhật thành công!");
            loadPositions();
            clearPosForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void deletePosition() {
        if (selectedPosId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa? ");
        if (confirm == JOptionPane.YES_OPTION) {
            if (positionDAO.deletePosition(selectedPosId)) {
                JOptionPane. showMessageDialog(this, "Xóa thành công!");
                loadPositions();
                clearPosForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void loadSelectedDepartment() {
        int row = departmentTable.getSelectedRow();
        if (row != -1) {
            selectedDeptId = (int) departmentTable.getValueAt(row, 0);
            txtDeptCode.setText(departmentTable.getValueAt(row, 1).toString());
            txtDeptName.setText(departmentTable.getValueAt(row, 2).toString());
            txtDeptDesc.setText(departmentTable.getValueAt(row, 3).toString());
            cboDeptStatus.setSelectedItem(departmentTable.getValueAt(row, 4).toString());
        }
    }
    
    private void loadSelectedPosition() {
        int row = positionTable.getSelectedRow();
        if (row != -1) {
            selectedPosId = (int) positionTable.getValueAt(row, 0);
            txtPosCode. setText(positionTable.getValueAt(row, 1).toString());
            txtPosName. setText(positionTable.getValueAt(row, 2).toString());
            cboPosLevel. setSelectedItem(positionTable. getValueAt(row, 3).toString());
            txtPosDesc.setText(positionTable. getValueAt(row, 4).toString());
        }
    }
    
    private void clearDeptForm() {
        selectedDeptId = -1;
        txtDeptCode.setText("");
        txtDeptName.setText("");
        txtDeptDesc. setText("");
        cboDeptStatus.setSelectedIndex(0);
        departmentTable.clearSelection();
    }
    
    private void clearPosForm() {
        selectedPosId = -1;
        txtPosCode.setText("");
        txtPosName.setText("");
        txtPosDesc.setText("");
        cboPosLevel.setSelectedIndex(0);
        positionTable.clearSelection();
    }
}