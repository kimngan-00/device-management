package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.NhanVien.NhanVienRole;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.service.impl.PhongBanServiceImpl;
import com.mycompany.device.util.LogoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel quản lý nhân viên
 * @author Kim Ngan - UI Layer
 */
public class NhanVienPanel extends JPanel {

    // Components
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    
    // Form components
    private JTextField txtMaNhanVien;
    private JTextField txtTenNhanVien;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtSoDienThoai;
    private JComboBox<NhanVienRole> cboRole;
    private JComboBox<PhongBan> cboPhongBan;
    private JTextField txtNgayTao;
    
    // Button components
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;
    private JButton btnLamSach;
    
    // Search components
    private JTextField txtTimKiem;
    private JComboBox<String> cboTimKiem;
    
    // Mock data
    private List<NhanVien> nhanVienList;
    
    // Services
    private PhongBanService phongBanService;
    
    // Table columns
    private final String[] columnNames = {
        "Mã NV", "Tên nhân viên", "Email", "Số điện thoại", 
        "Vai trò", "Mã phòng ban", "Ngày tạo"
    };
    
    public NhanVienPanel() {
        phongBanService = new PhongBanServiceImpl();
        initializeComponents();
        initializeMockData();
        setupLayout();
        setupEventHandlers();
        loadDataToTable();
        loadPhongBanData();
    }
    
    private void initializeComponents() {
        // Initialize table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table = new JTable(tableModel);
        tableSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(tableSorter);
        
        // Table settings
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Initialize form components
        txtMaNhanVien = new JTextField();
        txtTenNhanVien = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtSoDienThoai = new JTextField();
        cboRole = new JComboBox<>(NhanVienRole.values());
        cboPhongBan = new JComboBox<>();
        txtNgayTao = new JTextField();
        txtNgayTao.setEditable(false); // Read-only
        
        // Set preferred sizes for better display
        Dimension textFieldSize = new Dimension(200, 25);
        txtMaNhanVien.setPreferredSize(textFieldSize);
        txtTenNhanVien.setPreferredSize(textFieldSize);
        txtEmail.setPreferredSize(textFieldSize);
        txtPassword.setPreferredSize(textFieldSize);
        txtSoDienThoai.setPreferredSize(textFieldSize);
        cboPhongBan.setPreferredSize(textFieldSize);
        txtNgayTao.setPreferredSize(textFieldSize);
        cboRole.setPreferredSize(textFieldSize);
        
        // Initialize buttons
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnTimKiem = new JButton("Tìm kiếm");
        btnLamSach = new JButton("Làm sạch");
        
        // Style buttons
        styleButton(btnThem);
        styleButton(btnSua);
        styleButton(btnXoa);
        styleButton(btnLamMoi);
        styleButton(btnTimKiem);
        styleButton(btnLamSach);
        
        // Initialize search components
        txtTimKiem = new JTextField(20);
        cboTimKiem = new JComboBox<>(new String[]{
            "Tất cả", "Mã nhân viên", "Tên nhân viên", "Email", 
            "Số điện thoại", "Vai trò", "Mã phòng ban"
        });
    }
    
    private void styleButton(JButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });
    }
    
    private void initializeMockData() {
        nhanVienList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Sample data
        nhanVienList.add(new NhanVien("NV001", "Nguyễn Văn A", "nva@company.com", "password123", 
                                     "0901234567", NhanVienRole.ADMIN, "PB001", 
                                     LocalDateTime.parse("2024-01-15 09:00:00", formatter)));
        
        nhanVienList.add(new NhanVien("NV002", "Trần Thị B", "ttb@company.com", "password123", 
                                     "0901234568", NhanVienRole.STAFF, "PB002", 
                                     LocalDateTime.parse("2024-02-10 10:30:00", formatter)));
        
        nhanVienList.add(new NhanVien("NV003", "Lê Văn C", "lvc@company.com", "password123", 
                                     "0901234569", NhanVienRole.STAFF, "PB001", 
                                     LocalDateTime.parse("2024-03-05 14:15:00", formatter)));
        
        nhanVienList.add(new NhanVien("NV004", "Phạm Thị D", "ptd@company.com", "password123", 
                                     "0901234570", NhanVienRole.STAFF, "PB003", 
                                     LocalDateTime.parse("2024-04-20 11:20:00", formatter)));
        
        nhanVienList.add(new NhanVien("NV005", "Hoàng Văn E", "hve@company.com", "password123", 
                                     "0901234571", NhanVienRole.ADMIN, "PB002", 
                                     LocalDateTime.parse("2024-05-12 16:45:00", formatter)));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create main panels
        JPanel topPanel = createSearchPanel();
        JPanel centerPanel = createTablePanel();
        JPanel rightPanel = createFormPanel();
        JPanel bottomPanel = createButtonPanel();
        
        // Add panels to main layout
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(new TitledBorder("Tìm kiếm"));
        
        panel.add(new JLabel("Tìm theo:"));
        panel.add(cboTimKiem);
        panel.add(new JLabel("Từ khóa:"));
        panel.add(txtTimKiem);
        panel.add(btnTimKiem);
        panel.add(btnLamSach);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Danh sách nhân viên"));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Thông tin nhân viên"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: Mã nhân viên
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panel.add(new JLabel("Mã nhân viên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtMaNhanVien, gbc);
        
        // Row 1: Tên nhân viên
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Tên nhân viên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtTenNhanVien, gbc);
        
        // Row 2: Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtEmail, gbc);
        
        // Row 3: Mật khẩu
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtPassword, gbc);
        
        // Row 4: Số điện thoại
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtSoDienThoai, gbc);
        
        // Row 5: Vai trò
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0;
        panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(cboRole, gbc);
        
        // Row 6: Phòng ban
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
        panel.add(new JLabel("Phòng ban:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(cboPhongBan, gbc);
        
        // Row 7: Ngày tạo
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.0;
        panel.add(new JLabel("Ngày tạo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtNgayTao, gbc);
        
        // Add vertical spacer at the end
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(new TitledBorder("Thao tác"));
        
        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });
        
        // Button listeners
        btnThem.addActionListener(this::handleThem);
        btnSua.addActionListener(this::handleSua);
        btnXoa.addActionListener(this::handleXoa);
        btnLamMoi.addActionListener(this::handleLamMoi);
        btnTimKiem.addActionListener(this::handleTimKiem);
        btnLamSach.addActionListener(this::handleLamSach);
        
        // Enter key for search
        txtTimKiem.addActionListener(this::handleTimKiem);
    }
    
    private void loadDataToTable() {
        tableModel.setRowCount(0); // Clear existing data
        
        for (NhanVien nv : nhanVienList) {
            Object[] row = {
                nv.getMaNhanVien(),
                nv.getTenNhanVien(),
                nv.getEmail(),
                nv.getSoDienThoai(),
                nv.getRole().getDisplayName(),
                nv.getMaPhongBan(),
                nv.getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadSelectedRowToForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Convert view index to model index
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            // Find corresponding NhanVien object
            String maNhanVien = (String) tableModel.getValueAt(modelRow, 0);
            NhanVien nv = nhanVienList.stream()
                    .filter(n -> n.getMaNhanVien().equals(maNhanVien))
                    .findFirst().orElse(null);
            
            if (nv != null) {
                txtMaNhanVien.setText(nv.getMaNhanVien());
                txtTenNhanVien.setText(nv.getTenNhanVien());
                txtEmail.setText(nv.getEmail());
                txtPassword.setText(""); // Don't display password
                txtSoDienThoai.setText(nv.getSoDienThoai());
                cboRole.setSelectedItem(nv.getRole());
                setSelectedPhongBan(nv.getMaPhongBan());
                txtNgayTao.setText(nv.getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        }
    }
    
    private void clearForm() {
        txtMaNhanVien.setText("");
        txtTenNhanVien.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtSoDienThoai.setText("");
        cboRole.setSelectedIndex(0);
        cboPhongBan.setSelectedIndex(-1);
        txtNgayTao.setText("");
    }
    
    private boolean validateForm() {
        if (txtMaNhanVien.getText().trim().isEmpty()) {
            LogoUtil.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaNhanVien.requestFocus();
            return false;
        }
        
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            LogoUtil.showMessageDialog(this, "Vui lòng nhập tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNhanVien.requestFocus();
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            LogoUtil.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        if (!txtEmail.getText().contains("@")) {
            LogoUtil.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private String generateNextMaNhanVien() {
        int maxNum = 0;
        for (NhanVien nv : nhanVienList) {
            String ma = nv.getMaNhanVien();
            if (ma.startsWith("NV")) {
                try {
                    int num = Integer.parseInt(ma.substring(2));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    // Skip invalid format
                }
            }
        }
        return String.format("NV%03d", maxNum + 1);
    }
    
    // Event handlers
    private void handleThem(ActionEvent e) {
        if (!validateForm()) return;
        
        String maNhanVien = txtMaNhanVien.getText().trim();
        
        // Check if maNhanVien already exists
        if (nhanVienList.stream().anyMatch(nv -> nv.getMaNhanVien().equals(maNhanVien))) {
            LogoUtil.showMessageDialog(this, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create new NhanVien
        PhongBan selectedPhongBan = (PhongBan) cboPhongBan.getSelectedItem();
        String maPhongBan = selectedPhongBan != null ? selectedPhongBan.getMaPhongBan() : "";
        
        NhanVien newNhanVien = new NhanVien(
            maNhanVien,
            txtTenNhanVien.getText().trim(),
            txtEmail.getText().trim(),
            new String(txtPassword.getPassword()),
            txtSoDienThoai.getText().trim(),
            (NhanVienRole) cboRole.getSelectedItem(),
            maPhongBan,
            LocalDateTime.now()
        );
        
        nhanVienList.add(newNhanVien);
        loadDataToTable();
        clearForm();
        
        LogoUtil.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleSua(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            LogoUtil.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) return;
        
        // Convert view index to model index
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String maNhanVien = (String) tableModel.getValueAt(modelRow, 0);
        
        // Find and update NhanVien
        NhanVien nhanVien = nhanVienList.stream()
                .filter(nv -> nv.getMaNhanVien().equals(maNhanVien))
                .findFirst().orElse(null);
        
        if (nhanVien != null) {
            nhanVien.setTenNhanVien(txtTenNhanVien.getText().trim());
            nhanVien.setEmail(txtEmail.getText().trim());
            if (!new String(txtPassword.getPassword()).trim().isEmpty()) {
                nhanVien.setPassword(new String(txtPassword.getPassword()));
            }
            nhanVien.setSoDienThoai(txtSoDienThoai.getText().trim());
            nhanVien.setRole((NhanVienRole) cboRole.getSelectedItem());
            
            // Update phòng ban
            PhongBan selectedPhongBan = (PhongBan) cboPhongBan.getSelectedItem();
            if (selectedPhongBan != null) {
                nhanVien.setMaPhongBan(selectedPhongBan.getMaPhongBan());
            }
            
            loadDataToTable();
            LogoUtil.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleXoa(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            LogoUtil.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = LogoUtil.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa nhân viên này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Convert view index to model index
            int modelRow = table.convertRowIndexToModel(selectedRow);
            String maNhanVien = (String) tableModel.getValueAt(modelRow, 0);
            
            // Remove from list
            nhanVienList.removeIf(nv -> nv.getMaNhanVien().equals(maNhanVien));
            loadDataToTable();
            clearForm();
            
            LogoUtil.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleLamMoi(ActionEvent e) {
        loadDataToTable();
        clearForm();
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
        tableSorter.setRowFilter(null);
    }
    
    private void handleTimKiem(ActionEvent e) {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String searchType = (String) cboTimKiem.getSelectedItem();
        
        if (keyword.isEmpty()) {
            tableSorter.setRowFilter(null);
            return;
        }
        
        RowFilter<DefaultTableModel, Object> rowFilter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if ("Tất cả".equals(searchType)) {
                    // Search in all columns
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        if (entry.getStringValue(i).toLowerCase().contains(keyword)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    // Search in specific column
                    int columnIndex = getColumnIndex(searchType);
                    if (columnIndex >= 0) {
                        return entry.getStringValue(columnIndex).toLowerCase().contains(keyword);
                    }
                    return false;
                }
            }
        };
        
        tableSorter.setRowFilter(rowFilter);
    }
    
    private int getColumnIndex(String searchType) {
        switch (searchType) {
            case "Mã nhân viên": return 0;
            case "Tên nhân viên": return 1;
            case "Email": return 2;
            case "Số điện thoại": return 3;
            case "Vai trò": return 4;
            case "Mã phòng ban": return 5;
            default: return -1;
        }
    }
    
    private void handleLamSach(ActionEvent e) {
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
        tableSorter.setRowFilter(null);
    }
    
    /**
     * Load phòng ban data vào combobox
     */
    private void loadPhongBanData() {
        try {
            java.util.List<PhongBan> phongBanList = phongBanService.xemDanhSachPhongBan();
            cboPhongBan.removeAllItems();
            
            // Set custom renderer để hiển thị tên phòng ban
            cboPhongBan.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof PhongBan) {
                        PhongBan pb = (PhongBan) value;
                        setText(pb.getTenPhongBan() + " (" + pb.getMaPhongBan() + ")");
                    }
                    return this;
                }
            });
            
            for (PhongBan phongBan : phongBanList) {
                cboPhongBan.addItem(phongBan);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load dữ liệu phòng ban: " + e.getMessage());
        }
    }
    
    /**
     * Set selected phòng ban theo mã phòng ban
     */
    private void setSelectedPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            cboPhongBan.setSelectedIndex(-1);
            return;
        }
        
        for (int i = 0; i < cboPhongBan.getItemCount(); i++) {
            PhongBan phongBan = cboPhongBan.getItemAt(i);
            if (phongBan.getMaPhongBan().equals(maPhongBan)) {
                cboPhongBan.setSelectedIndex(i);
                break;
            }
        }
    }
    
    public void refreshData() {
        loadDataToTable();
    }
}
