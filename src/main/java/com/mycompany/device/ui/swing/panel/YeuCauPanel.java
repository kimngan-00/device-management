package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.YeuCau.TrangThaiYeuCau;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.LoaiThietBi;
import com.mycompany.device.model.NhanVien;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel quản lý yêu cầu mượn thiết bị
 * @author Kim Ngan - UI Layer
 */
public class YeuCauPanel extends JPanel {
    
    // Table components for displaying requests
    private JTable tableYeuCau;
    private DefaultTableModel tableModelYeuCau;
    private JScrollPane scrollPaneYeuCau;
    
    // Table components for available devices
    private JTable tableThietBi;
    private DefaultTableModel tableModelThietBi;
    private JScrollPane scrollPaneThietBi;
    
    // Form components
    private JComboBox<ThietBi> cboThietBi;
    private JTextArea txtLyDo;
    private JLabel lblNhanVien;
    private JLabel lblTrangThai;
    private JLabel lblNgayTao;
    
    // Button components
    private JButton btnGuiYeuCau;
    private JButton btnHuyYeuCau;
    private JButton btnLamMoi;
    
    // Search components
    private JTextField txtTimKiem;
    private JComboBox<String> cboTimKiem;
    private JButton btnTimKiem;
    private JButton btnLamSach;
    
    // Data
    private List<YeuCau> yeuCauList;
    private List<ThietBi> thietBiList;
    private List<LoaiThietBi> loaiThietBiList;
    private NhanVien currentUser;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public YeuCauPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        initializeMockData();
        loadTableData();
    }
    
    private void initializeComponents() {
        // Initialize yeu cau table
        String[] yeuCauColumns = {"ID", "Thiết bị", "Lý do", "Trạng thái", "Ngày tạo", "Ngày cập nhật"};
        tableModelYeuCau = new DefaultTableModel(yeuCauColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableYeuCau = new JTable(tableModelYeuCau);
        tableYeuCau.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableYeuCau.getTableHeader().setReorderingAllowed(false);
        setupYeuCauTableAppearance();
        scrollPaneYeuCau = new JScrollPane(tableYeuCau);
        
        // Initialize thiet bi table  
        String[] thietBiColumns = {"Số Serial", "Loại thiết bị", "Trạng thái", "Ngày mua", "Giá mua"};
        tableModelThietBi = new DefaultTableModel(thietBiColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThietBi = new JTable(tableModelThietBi);
        tableThietBi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableThietBi.getTableHeader().setReorderingAllowed(false);
        setupThietBiTableAppearance();
        scrollPaneThietBi = new JScrollPane(tableThietBi);
        
        // Initialize mock data
        initializeLoaiThietBiData();
        initializeThietBiData();
        
        // Initialize form components
        cboThietBi = new JComboBox<>();
        populateThietBiComboBox();
        txtLyDo = new JTextArea(4, 30);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        
        lblNhanVien = new JLabel("Chưa đăng nhập");
        lblTrangThai = new JLabel("Chờ duyệt");
        lblNgayTao = new JLabel(LocalDateTime.now().format(DATE_FORMATTER));
        
        // Initialize buttons
        btnGuiYeuCau = new JButton("Gửi yêu cầu");
        btnHuyYeuCau = new JButton("Hủy yêu cầu");
        btnLamMoi = new JButton("Làm mới");
        
        styleButton(btnGuiYeuCau);
        styleButton(btnHuyYeuCau);
        styleButton(btnLamMoi);
        
        // Initially disable cancel button
        btnHuyYeuCau.setEnabled(false);
        
        // Search components
        txtTimKiem = new JTextField(20);
        String[] searchOptions = {"Tất cả", "Thiết bị", "Trạng thái", "Lý do"};
        cboTimKiem = new JComboBox<>(searchOptions);
        btnTimKiem = new JButton("Tìm kiếm");
        btnLamSach = new JButton("Làm sạch");
        
        styleButton(btnTimKiem);
        styleButton(btnLamSach);
        
        // Set preferred sizes
        Dimension fieldSize = new Dimension(250, 25);
        cboThietBi.setPreferredSize(fieldSize);
        txtTimKiem.setPreferredSize(new Dimension(200, 25));
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
    
    private void setupYeuCauTableAppearance() {
        tableYeuCau.setRowHeight(35);
        tableYeuCau.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        tableYeuCau.setBackground(Color.WHITE);
        tableYeuCau.setSelectionBackground(new Color(184, 207, 229));
        tableYeuCau.setSelectionForeground(Color.BLACK);
        tableYeuCau.setGridColor(new Color(230, 230, 230));
        
        tableYeuCau.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        tableYeuCau.getTableHeader().setBackground(new Color(240, 240, 240));
        tableYeuCau.getTableHeader().setForeground(Color.BLACK);
        tableYeuCau.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        tableYeuCau.setShowGrid(true);
        tableYeuCau.setIntercellSpacing(new Dimension(1, 1));
    }
    
    private void setupThietBiTableAppearance() {
        tableThietBi.setRowHeight(30);
        tableThietBi.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        tableThietBi.setBackground(Color.WHITE);
        tableThietBi.setSelectionBackground(new Color(184, 207, 229));
        tableThietBi.setSelectionForeground(Color.BLACK);
        tableThietBi.setGridColor(new Color(230, 230, 230));
        
        tableThietBi.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        tableThietBi.getTableHeader().setBackground(new Color(240, 240, 240));
        tableThietBi.getTableHeader().setForeground(Color.BLACK);
        tableThietBi.getTableHeader().setPreferredSize(new Dimension(0, 30));
        
        tableThietBi.setShowGrid(true);
        tableThietBi.setIntercellSpacing(new Dimension(1, 1));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(300);
        
        // Top panel for request form
        JPanel topPanel = createRequestFormPanel();
        
        // Bottom panel for tables
        JPanel bottomPanel = createTablesPanel();
        
        mainSplitPane.setTopComponent(topPanel);
        mainSplitPane.setBottomComponent(bottomPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private JPanel createRequestFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Gửi yêu cầu sử dụng thiết bị"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // Nhân viên
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(lblNhanVien, gbc);
        
        // Thiết bị
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Thiết bị:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(cboThietBi, gbc);
        
        // Lý do
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Lý do:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(txtLyDo), gbc);
        
        // Trạng thái và ngày tạo (readonly)
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(lblTrangThai, gbc);
        
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Ngày tạo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(lblNgayTao, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createRequestButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRequestButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(btnGuiYeuCau);
        panel.add(btnHuyYeuCau);
        panel.add(btnLamMoi);
        return panel;
    }
    
    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Split pane for two tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        
        // Left panel - Yêu cầu của tôi
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Yêu cầu của tôi"));
        
        // Search panel for requests
        JPanel searchPanel = createSearchPanel();
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPaneYeuCau, BorderLayout.CENTER);
        
        // Right panel - Thiết bị có sẵn
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Thiết bị có sẵn"));
        rightPanel.add(scrollPaneThietBi, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(new JLabel("Tìm kiếm:"));
        panel.add(txtTimKiem);
        panel.add(new JLabel("Trong:"));
        panel.add(cboTimKiem);
        panel.add(btnTimKiem);
        panel.add(btnLamSach);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        btnGuiYeuCau.addActionListener(this::handleGuiYeuCau);
        btnHuyYeuCau.addActionListener(this::handleHuyYeuCau);
        btnLamMoi.addActionListener(this::handleLamMoi);
        btnTimKiem.addActionListener(this::handleTimKiem);
        btnLamSach.addActionListener(this::handleLamSach);
        
        // Table selection listeners
        tableYeuCau.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableYeuCau.getSelectedRow();
                if (selectedRow >= 0) {
                    loadYeuCauFromTable(selectedRow);
                }
            }
        });
        
        tableThietBi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableThietBi.getSelectedRow();
                if (selectedRow >= 0) {
                    selectThietBiFromTable(selectedRow);
                }
            }
        });
    }
    
    private void handleGuiYeuCau(ActionEvent e) {
        try {
            // Validation
            ThietBi selectedThietBi = (ThietBi) cboThietBi.getSelectedItem();
            if (selectedThietBi == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị cần mượn!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String lyDo = txtLyDo.getText().trim();
            if (lyDo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do mượn thiết bị!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Bạn cần đăng nhập để gửi yêu cầu!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new request  
            YeuCau yeuCau = new YeuCau(selectedThietBi.getId(), (long)(yeuCauList.size() + 1), lyDo);
            yeuCauList.add(yeuCau);
            
            // Refresh table
            loadYeuCauTableData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Gửi yêu cầu thành công!", 
                                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi yêu cầu: " + ex.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleHuyYeuCau(ActionEvent e) {
        int selectedRow = tableYeuCau.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn yêu cầu cần hủy!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        YeuCau yeuCau = yeuCauList.get(selectedRow);
        
        if (!yeuCau.isPending()) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể hủy yêu cầu đang chờ duyệt!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn hủy yêu cầu này?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            yeuCau.updateTrangThai(TrangThaiYeuCau.DA_HUY);
            loadYeuCauTableData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Đã hủy yêu cầu thành công!", 
                                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleLamMoi(ActionEvent e) {
        clearForm();
        loadYeuCauTableData();
        loadThietBiTableData();
    }
    
    private void handleTimKiem(ActionEvent e) {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String searchIn = (String) cboTimKiem.getSelectedItem();
        
        if (keyword.isEmpty()) {
            loadYeuCauTableData();
            return;
        }
        
        List<YeuCau> filteredList = new ArrayList<>();
        for (YeuCau yc : yeuCauList) {
            boolean matches = false;
            
            switch (searchIn) {
                case "Tất cả":
                    matches = matchesKeyword(yc, keyword);
                    break;
                case "Thiết bị":
                    ThietBi thietBi = findThietBiById(yc.getThietBiId());
                    matches = thietBi != null && thietBi.getSoSerial().toLowerCase().contains(keyword);
                    break;
                case "Trạng thái":
                    matches = yc.getTrangThai().getDisplayName().toLowerCase().contains(keyword);
                    break;
                case "Lý do":
                    matches = yc.getLyDo() != null && yc.getLyDo().toLowerCase().contains(keyword);
                    break;
            }
            
            if (matches) {
                filteredList.add(yc);
            }
        }
        
        displayFilteredYeuCau(filteredList);
    }
    
    private boolean matchesKeyword(YeuCau yc, String keyword) {
        if (yc.getLyDo() != null && yc.getLyDo().toLowerCase().contains(keyword)) {
            return true;
        }
        if (yc.getTrangThai().getDisplayName().toLowerCase().contains(keyword)) {
            return true;
        }
        ThietBi thietBi = findThietBiById(yc.getThietBiId());
        return thietBi != null && thietBi.getSoSerial().toLowerCase().contains(keyword);
    }
    
    private void displayFilteredYeuCau(List<YeuCau> filteredList) {
        tableModelYeuCau.setRowCount(0);
        
        for (YeuCau yc : filteredList) {
            ThietBi thietBi = findThietBiById(yc.getThietBiId());
            String thietBiInfo = thietBi != null ? thietBi.getSoSerial() : "N/A";
            
            Object[] row = {
                yc.getId(),
                thietBiInfo,
                yc.getLyDo(),
                yc.getTrangThai().getDisplayName(),
                yc.getNgayTao() != null ? yc.getNgayTao().format(DATE_FORMATTER) : "",
                yc.getNgayCapNhat() != null ? yc.getNgayCapNhat().format(DATE_FORMATTER) : ""
            };
            tableModelYeuCau.addRow(row);
        }
    }
    
    private void handleLamSach(ActionEvent e) {
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
        loadYeuCauTableData();
    }
    
    private void loadYeuCauFromTable(int row) {
        if (row >= 0 && row < yeuCauList.size()) {
            YeuCau yc = yeuCauList.get(row);
            
            ThietBi thietBi = findThietBiById(yc.getThietBiId());
            if (thietBi != null) {
                cboThietBi.setSelectedItem(thietBi);
            }
            
            txtLyDo.setText(yc.getLyDo() != null ? yc.getLyDo() : "");
            lblTrangThai.setText(yc.getTrangThai().getDisplayName());
            lblNgayTao.setText(yc.getNgayTao() != null ? yc.getNgayTao().format(DATE_FORMATTER) : "");
            
            // Enable/disable cancel button
            btnHuyYeuCau.setEnabled(yc.isPending());
        }
    }
    
    private void selectThietBiFromTable(int row) {
        if (row >= 0 && row < thietBiList.size()) {
            ThietBi thietBi = thietBiList.get(row);
            cboThietBi.setSelectedItem(thietBi);
        }
    }
    
    private void clearForm() {
        cboThietBi.setSelectedIndex(0);
        txtLyDo.setText("");
        lblTrangThai.setText("Chờ duyệt");
        lblNgayTao.setText(LocalDateTime.now().format(DATE_FORMATTER));
        btnHuyYeuCau.setEnabled(false);
        
        // Clear table selections
        tableYeuCau.clearSelection();
        tableThietBi.clearSelection();
    }
    
    private void loadTableData() {
        loadYeuCauTableData();
        loadThietBiTableData();
    }
    
    private void loadYeuCauTableData() {
        tableModelYeuCau.setRowCount(0);
        
        // Chỉ hiển thị yêu cầu của user hiện tại
        List<YeuCau> userRequests = new ArrayList<>();
        if (currentUser != null) {
            for (YeuCau yc : yeuCauList) {
                if (yc.getNhanVienId().equals(1L)) { // Temporary: assume user ID = 1
                    userRequests.add(yc);
                }
            }
        }
        
        for (YeuCau yc : userRequests) {
            ThietBi thietBi = findThietBiById(yc.getThietBiId());
            String thietBiInfo = thietBi != null ? thietBi.getSoSerial() : "N/A";
            
            Object[] row = {
                yc.getId(),
                thietBiInfo,
                yc.getLyDo(),
                yc.getTrangThai().getDisplayName(),
                yc.getNgayTao() != null ? yc.getNgayTao().format(DATE_FORMATTER) : "",
                yc.getNgayCapNhat() != null ? yc.getNgayCapNhat().format(DATE_FORMATTER) : ""
            };
            tableModelYeuCau.addRow(row);
        }
    }
    
    private void loadThietBiTableData() {
        tableModelThietBi.setRowCount(0);
        
        for (ThietBi tb : thietBiList) {
            // Tìm tên loại thiết bị
            String tenLoai = "";
            if (tb.getLoaiId() != null) {
                for (LoaiThietBi loai : loaiThietBiList) {
                    if (loai.getId().equals(tb.getLoaiId())) {
                        tenLoai = loai.getTenLoai();
                        break;
                    }
                }
            }
            
            Object[] row = {
                tb.getSoSerial(),
                tenLoai,
                tb.getTrangThai().getDisplayName(),
                tb.getNgayMua() != null ? tb.getNgayMua().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "",
                tb.getGiaMua() != null ? tb.getGiaMua().toString() : ""
            };
            tableModelThietBi.addRow(row);
        }
    }
    
    // Mock data methods
    private void initializeMockData() {
        yeuCauList = new ArrayList<>();
        
        // Add some sample requests (will be filtered by user)
        yeuCauList.add(new YeuCau(1L, 1L, 1L, TrangThaiYeuCau.CHO_DUYET, 
            "Cần laptop để làm việc tại nhà", 
            LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1)));
            
        yeuCauList.add(new YeuCau(2L, 2L, 1L, TrangThaiYeuCau.DA_DUYET, 
            "Máy in để in tài liệu", 
            LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2)));
    }
    
    private void initializeLoaiThietBiData() {
        loaiThietBiList = new ArrayList<>();
        
        loaiThietBiList.add(new LoaiThietBi(1L, "LT001", "Máy tính để bàn", "Máy tính cá nhân dành cho văn phòng"));
        loaiThietBiList.add(new LoaiThietBi(2L, "LT002", "Laptop", "Máy tính xách tay"));
        loaiThietBiList.add(new LoaiThietBi(3L, "LT003", "Máy in", "Thiết bị in ấn văn phòng"));
        loaiThietBiList.add(new LoaiThietBi(4L, "LT004", "Máy chiếu", "Thiết bị trình chiếu"));
        loaiThietBiList.add(new LoaiThietBi(5L, "LT005", "Màn hình", "Màn hình máy tính"));
    }
    
    private void initializeThietBiData() {
        thietBiList = new ArrayList<>();
        
        ThietBi tb1 = new ThietBi("LAP001", 2L, ThietBi.TrangThaiThietBi.TON_KHO, 
            java.time.LocalDate.now().minusMonths(6), new java.math.BigDecimal("15000000"), 
            "Laptop Dell Inspiron 15");
        tb1.setId(1L);
        thietBiList.add(tb1);
            
        ThietBi tb2 = new ThietBi("PC001", 1L, ThietBi.TrangThaiThietBi.TON_KHO, 
            java.time.LocalDate.now().minusMonths(12), new java.math.BigDecimal("12000000"), 
            "PC Dell OptiPlex");
        tb2.setId(2L);
        thietBiList.add(tb2);
            
        ThietBi tb3 = new ThietBi("PRINT001", 3L, ThietBi.TrangThaiThietBi.TON_KHO, 
            java.time.LocalDate.now().minusMonths(8), new java.math.BigDecimal("3000000"), 
            "Máy in HP LaserJet");
        tb3.setId(3L);
        thietBiList.add(tb3);
            
        ThietBi tb4 = new ThietBi("PROJ001", 4L, ThietBi.TrangThaiThietBi.TON_KHO, 
            java.time.LocalDate.now().minusMonths(10), new java.math.BigDecimal("8000000"), 
            "Máy chiếu Epson");
        tb4.setId(4L);
        thietBiList.add(tb4);
            
        ThietBi tb5 = new ThietBi("MON001", 5L, ThietBi.TrangThaiThietBi.TON_KHO, 
            java.time.LocalDate.now().minusMonths(4), new java.math.BigDecimal("4000000"), 
            "Màn hình Samsung 24 inch");
        tb5.setId(5L);
        thietBiList.add(tb5);
    }
    
    private void populateThietBiComboBox() {
        cboThietBi.removeAllItems();
        
        // Add default option
        cboThietBi.addItem(null);
        
        // Add available devices
        for (ThietBi thietBi : thietBiList) {
            if (thietBi.getTrangThai() == ThietBi.TrangThaiThietBi.TON_KHO) {
                cboThietBi.addItem(thietBi);
            }
        }
        
        // Custom renderer
        cboThietBi.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value == null) {
                    setText("-- Chọn thiết bị --");
                } else if (value instanceof ThietBi) {
                    ThietBi tb = (ThietBi) value;
                    LoaiThietBi loai = findLoaiThietBiById(tb.getLoaiId());
                    String loaiName = loai != null ? loai.getTenLoai() : "N/A";
                    setText(tb.getSoSerial() + " - " + loaiName);
                }
                
                return this;
            }
        });
    }
    
    private ThietBi findThietBiById(Long id) {
        if (id == null) return null;
        for (ThietBi tb : thietBiList) {
            if (id.equals(tb.getId())) {
                return tb;
            }
        }
        return null;
    }
    
    private LoaiThietBi findLoaiThietBiById(Long id) {
        if (id == null) return null;
        for (LoaiThietBi loai : loaiThietBiList) {
            if (id.equals(loai.getId())) {
                return loai;
            }
        }
        return null;
    }
    
    // Public method to set current user
    public void setCurrentUser(NhanVien user) {
        this.currentUser = user;
        if (user != null) {
            lblNhanVien.setText(user.getTenNhanVien());
        } else {
            lblNhanVien.setText("Chưa đăng nhập");
        }
        loadYeuCauTableData(); // Reload to show user's requests
    }
}
