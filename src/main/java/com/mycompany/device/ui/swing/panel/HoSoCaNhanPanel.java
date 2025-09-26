package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.NhanVien.NhanVienRole;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.controller.AuthController;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.service.impl.PhongBanServiceImpl;
import com.mycompany.device.util.LogoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;

/**
 * Panel hồ sơ cá nhân - cho phép người dùng chỉnh sửa thông tin cá nhân
 * @author Kim Ngan - UI Layer
 */
public class HoSoCaNhanPanel extends JPanel {

    // Form components
    private JTextField txtMaNhanVien;
    private JTextField txtTenNhanVien;
    private JTextField txtEmail;
    private JPasswordField txtMatKhauCu;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhanMatKhau;
    private JTextField txtSoDienThoai;
    private JLabel lblVaiTro;
    private JComboBox<PhongBan> cboPhongBan;
    private JTextField txtNgayTao;
    
    // Button components
    private JButton btnCapNhat;
    private JButton btnDoiMatKhau;
    private JButton btnHuyBo;
    
    // Current user
    private NhanVien currentUser;
    private AuthController authController;
    private PhongBanService phongBanService;
    
    public HoSoCaNhanPanel() {
        phongBanService = new PhongBanServiceImpl();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadPhongBanData();
    }
    
    /**
     * Set current user và load thông tin
     */
    public void setCurrentUser(NhanVien user, AuthController authController) {
        this.currentUser = user;
        this.authController = authController;
        loadUserInfo();
    }
    
    private void initializeComponents() {
        // Initialize form components
        txtMaNhanVien = new JTextField();
        txtTenNhanVien = new JTextField();
        txtEmail = new JTextField();
        txtMatKhauCu = new JPasswordField();
        txtMatKhauMoi = new JPasswordField();
        txtXacNhanMatKhau = new JPasswordField();
        txtSoDienThoai = new JTextField();
        lblVaiTro = new JLabel();
        cboPhongBan = new JComboBox<>();
        cboPhongBan.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PhongBan) {
                    PhongBan phongBan = (PhongBan) value;
                    setText(phongBan.getTenPhongBan());
                }
                return this;
            }
        });
        txtNgayTao = new JTextField();
        
        // Set read-only fields
        txtMaNhanVien.setEditable(false);
        lblVaiTro.setOpaque(true);
        lblVaiTro.setBackground(Color.LIGHT_GRAY);
        lblVaiTro.setBorder(BorderFactory.createLoweredBevelBorder());
        txtNgayTao.setEditable(false);
        
        // Set preferred sizes for better display
        Dimension textFieldSize = new Dimension(250, 28);
        txtMaNhanVien.setPreferredSize(textFieldSize);
        txtTenNhanVien.setPreferredSize(textFieldSize);
        txtEmail.setPreferredSize(textFieldSize);
        txtMatKhauCu.setPreferredSize(textFieldSize);
        txtMatKhauMoi.setPreferredSize(textFieldSize);
        txtXacNhanMatKhau.setPreferredSize(textFieldSize);
        txtSoDienThoai.setPreferredSize(textFieldSize);
        lblVaiTro.setPreferredSize(textFieldSize);
        cboPhongBan.setPreferredSize(textFieldSize);
        txtNgayTao.setPreferredSize(textFieldSize);
        
        // Initialize buttons
        btnCapNhat = new JButton("Cập nhật thông tin");
        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnHuyBo = new JButton("Hủy bỏ");
        
        // Style buttons
        styleButton(btnCapNhat);
        styleButton(btnDoiMatKhau);
        styleButton(btnHuyBo);
        
        // Set colors for buttons - all same style as "Đổi mật khẩu" button
        btnCapNhat.setBackground(new Color(255, 193, 7));
        btnCapNhat.setForeground(Color.BLACK);
        
        btnDoiMatKhau.setBackground(new Color(255, 193, 7));
        btnDoiMatKhau.setForeground(Color.BLACK);
        
        btnHuyBo.setBackground(new Color(255, 193, 7));
        btnHuyBo.setForeground(Color.BLACK);
    }
    
    private void styleButton(JButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Store original colors for hover effect
        final Color originalBackground = new Color(255, 193, 7); // Yellow color
        final Color hoverBackground = new Color(218, 165, 32); // Darker yellow for hover
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBackground);
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 249, 250));
        
        // Title
        JLabel titleLabel = new JLabel("Hồ Sơ Cá Nhân", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Create form panels
        JPanel thongTinCoBanPanel = createThongTinCoBanPanel();
        JPanel doiMatKhauPanel = createDoiMatKhauPanel();
        JPanel buttonPanel = createButtonPanel();
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add form panels
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(thongTinCoBanPanel, gbc);
        
        gbc.gridy = 1;
        contentPanel.add(doiMatKhauPanel, gbc);
        
        gbc.gridy = 2; gbc.insets = new Insets(30, 0, 0, 0);
        contentPanel.add(buttonPanel, gbc);
        
        // Add vertical glue
        gbc.gridy = 3; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(Box.createVerticalGlue(), gbc);
        
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createThongTinCoBanPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), 
                                        "Thông tin cơ bản", 
                                        TitledBorder.LEFT, 
                                        TitledBorder.TOP,
                                        new Font("Arial", Font.BOLD, 14)));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0: Mã nhân viên
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panel.add(new JLabel("Mã nhân viên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtMaNhanVien, gbc);
        
        // Row 1: Tên nhân viên
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tên nhân viên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtTenNhanVien, gbc);
        
        // Row 2: Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtEmail, gbc);
        
        // Row 3: Số điện thoại
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtSoDienThoai, gbc);
        
        // Row 4: Vai trò
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lblVaiTro, gbc);
        
        // Row 5: Phòng ban
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Phòng ban:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(cboPhongBan, gbc);
        
        // Row 6: Ngày tạo
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Ngày tạo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtNgayTao, gbc);
        
        return panel;
    }
    
    private JPanel createDoiMatKhauPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), 
                                        "Đổi mật khẩu", 
                                        TitledBorder.LEFT, 
                                        TitledBorder.TOP,
                                        new Font("Arial", Font.BOLD, 14)));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0: Mật khẩu cũ
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panel.add(new JLabel("Mật khẩu cũ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtMatKhauCu, gbc);
        
        // Row 1: Mật khẩu mới
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtMatKhauMoi, gbc);
        
        // Row 2: Xác nhận mật khẩu
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtXacNhanMatKhau, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);
        
        panel.add(btnCapNhat);
        panel.add(btnDoiMatKhau);
        panel.add(btnHuyBo);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        btnCapNhat.addActionListener(this::handleCapNhatThongTin);
        btnDoiMatKhau.addActionListener(this::handleDoiMatKhau);
        btnHuyBo.addActionListener(this::handleHuyBo);
    }
    
    private void loadUserInfo() {
        if (currentUser != null) {
            txtMaNhanVien.setText(currentUser.getMaNhanVien());
            txtTenNhanVien.setText(currentUser.getTenNhanVien());
            txtEmail.setText(currentUser.getEmail());
            txtSoDienThoai.setText(currentUser.getSoDienThoai());
            lblVaiTro.setText(currentUser.getRole().getDisplayName());
            
            // Set selected phong ban
            setSelectedPhongBan(currentUser.getMaPhongBan());
            
            if (currentUser.getNgayTao() != null) {
                txtNgayTao.setText(currentUser.getNgayTao().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
            
            // Clear password fields
            txtMatKhauCu.setText("");
            txtMatKhauMoi.setText("");
            txtXacNhanMatKhau.setText("");
        }
    }
    
    private boolean validateThongTinCoBan() {
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNhanVien.requestFocus();
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        if (!txtEmail.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean validateMatKhau() {
        String matKhauCu = new String(txtMatKhauCu.getPassword());
        String matKhauMoi = new String(txtMatKhauMoi.getPassword());
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword());
        
        if (matKhauCu.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu cũ!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.requestFocus();
            return false;
        }
        
        if (matKhauMoi.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return false;
        }
        
        if (matKhauMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return false;
        }
        
        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtXacNhanMatKhau.requestFocus();
            return false;
        }
        
        // Verify old password
        if (currentUser != null && !currentUser.checkPassword(matKhauCu)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // Event handlers
    private void handleCapNhatThongTin(ActionEvent e) {
        if (!validateThongTinCoBan()) return;
        
        if (currentUser != null) {
            // Update user information (mock update)
            currentUser.setTenNhanVien(txtTenNhanVien.getText().trim());
            currentUser.setEmail(txtEmail.getText().trim());
            currentUser.setSoDienThoai(txtSoDienThoai.getText().trim());
            
            // Update phòng ban
            PhongBan selectedPhongBan = (PhongBan) cboPhongBan.getSelectedItem();
            if (selectedPhongBan != null) {
                currentUser.setMaPhongBan(selectedPhongBan.getMaPhongBan());
            }
            
            // In real app, this would call a service to update in database
            // userService.updateUser(currentUser);
            
            LogoUtil.showMessageDialog(this, 
                "Cập nhật thông tin cá nhân thành công!\n" +
                "Phòng ban: " + ((PhongBan) cboPhongBan.getSelectedItem()).getTenPhongBan() + "\n" +
                "Lưu ý: Trong ứng dụng thực tế, thông tin sẽ được lưu vào cơ sở dữ liệu.", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleDoiMatKhau(ActionEvent e) {
        if (!validateMatKhau()) return;
        
        if (currentUser != null) {
            String matKhauMoi = new String(txtMatKhauMoi.getPassword());
            
            // Update password (mock update)
            // In real app: currentUser.setPassword(PasswordUtil.hashPasswordSimple(matKhauMoi));
            // userService.updatePassword(currentUser);
            
            // Clear password fields
            txtMatKhauCu.setText("");
            txtMatKhauMoi.setText("");
            txtXacNhanMatKhau.setText("");
            
            JOptionPane.showMessageDialog(this, 
                "Đổi mật khẩu thành công!\n" +
                "Lưu ý: Trong ứng dụng thực tế, mật khẩu sẽ được mã hóa và lưu vào cơ sở dữ liệu.", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleHuyBo(ActionEvent e) {
        // Reset form to original values
        loadUserInfo();
        
        JOptionPane.showMessageDialog(this, 
            "Đã hủy bỏ các thay đổi!", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Load phòng ban data vào combobox
     */
    private void loadPhongBanData() {
        try {
            java.util.List<PhongBan> phongBanList = phongBanService.xemDanhSachPhongBan();
            cboPhongBan.removeAllItems();
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
    
    /**
     * Refresh data (called when panel is activated)
     */
    public void refreshData() {
        if (authController != null && authController.isLoggedIn()) {
            setCurrentUser(authController.getCurrentUser(), authController);
        }
    }
}
