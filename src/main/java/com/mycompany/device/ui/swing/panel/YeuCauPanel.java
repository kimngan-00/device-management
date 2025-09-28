package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.YeuCau.TrangThaiYeuCau;
import com.mycompany.device.util.LogoUtil;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.LoaiThietBi;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.service.YeuCauService;
import com.mycompany.device.service.ThietBiService;
import com.mycompany.device.service.LoaiThietBiService;
import com.mycompany.device.service.impl.YeuCauServiceImpl;
import com.mycompany.device.service.impl.ThietBiServiceImpl;
import com.mycompany.device.service.impl.LoaiThietBiServiceImpl;
import com.mycompany.device.controller.AuthController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel quản lý yêu cầu mượn thiết bị với tích hợp service thực tế
 * @author Kim Ngan - UI Layer with Service Integration
 */
public class YeuCauPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(YeuCauPanel.class);
    
    // Services
    private final YeuCauService yeuCauService;
    private final ThietBiService thietBiService;
    private final LoaiThietBiService loaiThietBiService;
    private AuthController authController;
    
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
    
    /**
     * Constructor với dependency injection
     */
    public YeuCauPanel(AuthController authController) {
        this.authController = authController;
        this.yeuCauService = new YeuCauServiceImpl();
        this.thietBiService = new ThietBiServiceImpl();
        this.loaiThietBiService = new LoaiThietBiServiceImpl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDataFromServices();
        loadTableData();
    }
    
    /**
     * Constructor mặc định (fallback)
     */
    public YeuCauPanel() {
        this.yeuCauService = new YeuCauServiceImpl();
        this.thietBiService = new ThietBiServiceImpl();
        this.loaiThietBiService = new LoaiThietBiServiceImpl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDataFromServices();
        loadTableData();
    }
    
    private void initializeComponents() {
        logger.info("Khởi tạo các component UI cho YeuCauPanel");
        
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
        
        // Initialize form components
        cboThietBi = new JComboBox<>();
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
    
    /**
     * Load data từ các service thay vì mock data
     */
    private void loadDataFromServices() {
        try {
            logger.info("Bắt đầu load dữ liệu từ services");
            
            // Load loại thiết bị
            loaiThietBiList = loaiThietBiService.findAll();
            logger.info("Đã load {} loại thiết bị", loaiThietBiList.size());
            
            // Load thiết bị
            thietBiList = thietBiService.findAll();
            logger.info("Đã load {} thiết bị", thietBiList.size());
            
            // Load yêu cầu
            yeuCauList = yeuCauService.xemDanhSachYeuCau();
            logger.info("Đã load {} yêu cầu", yeuCauList.size());
            
            // Populate combo box với thiết bị có sẵn
            populateThietBiComboBox();
            
        } catch (Exception e) {
            logger.error("Lỗi khi load dữ liệu từ services", e);
            LogoUtil.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            
            // Fallback to empty lists
            yeuCauList = new ArrayList<>();
            thietBiList = new ArrayList<>();
            loaiThietBiList = new ArrayList<>();
        }
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
    
    /**
     * Xử lý sự kiện gửi yêu cầu - sử dụng YeuCauService thực tế
     */
    private void handleGuiYeuCau(ActionEvent e) {
        try {
            logger.info("Bắt đầu xử lý gửi yêu cầu");
            
            // Validation
            ThietBi selectedThietBi = (ThietBi) cboThietBi.getSelectedItem();
            if (selectedThietBi == null) {
                LogoUtil.showMessageDialog(this, "Vui lòng chọn thiết bị cần mượn!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String lyDo = txtLyDo.getText().trim();
            if (lyDo.isEmpty()) {
                LogoUtil.showMessageDialog(this, "Vui lòng nhập lý do mượn thiết bị!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get current user
            NhanVien user = getCurrentUser();
            if (user == null) {
                LogoUtil.showMessageDialog(this, "Bạn cần đăng nhập để gửi yêu cầu!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Kiểm tra thiết bị có sẵn không
            if (selectedThietBi.getTrangThai() != ThietBi.TrangThaiThietBi.TON_KHO) {
                LogoUtil.showMessageDialog(this, "Thiết bị này hiện không có sẵn!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Sử dụng trực tiếp maNhanVien (String)
            String nhanVienId = user.getMaNhanVien();
            
            // Tạo yêu cầu qua service
            boolean success = yeuCauService.taoYeuCau(selectedThietBi.getId(), nhanVienId, lyDo);
            
            if (success) {
                logger.info("Tạo yêu cầu thành công: thietBiId={}, nhanVienId={}", 
                           selectedThietBi.getId(), nhanVienId);
                
                // Refresh data và table
                refreshData();
                clearForm();
                
                LogoUtil.showMessageDialog(this, "Gửi yêu cầu thành công!", 
                                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                logger.warn("Tạo yêu cầu thất bại");
                LogoUtil.showMessageDialog(this, "Không thể gửi yêu cầu. Vui lòng thử lại!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            logger.error("Lỗi khi gửi yêu cầu", ex);
            LogoUtil.showMessageDialog(this, "Lỗi khi gửi yêu cầu: " + ex.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xử lý sự kiện hủy yêu cầu - sử dụng YeuCauService thực tế
     */
    private void handleHuyYeuCau(ActionEvent e) {
        try {
            int selectedRow = tableYeuCau.getSelectedRow();
            if (selectedRow < 0) {
                LogoUtil.showMessageDialog(this, "Vui lòng chọn yêu cầu cần hủy!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            YeuCau yeuCau = getFilteredYeuCauList().get(selectedRow);
            
            if (!yeuCau.isPending()) {
                LogoUtil.showMessageDialog(this, "Chỉ có thể hủy yêu cầu đang chờ duyệt!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = LogoUtil.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn hủy yêu cầu này?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = yeuCauService.huyYeuCau(yeuCau.getId(), "Người dùng hủy yêu cầu");
                
                if (success) {
                    logger.info("Hủy yêu cầu thành công: ID={}", yeuCau.getId());
                    refreshData();
                    clearForm();
                    
                    LogoUtil.showMessageDialog(this, "Đã hủy yêu cầu thành công!", 
                                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    LogoUtil.showMessageDialog(this, "Không thể hủy yêu cầu. Vui lòng thử lại!", 
                                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception ex) {
            logger.error("Lỗi khi hủy yêu cầu", ex);
            LogoUtil.showMessageDialog(this, "Lỗi khi hủy yêu cầu: " + ex.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLamMoi(ActionEvent e) {
        try {
            logger.info("Làm mới dữ liệu");
            refreshData();
            clearForm();
        } catch (Exception ex) {
            logger.error("Lỗi khi làm mới dữ liệu", ex);
            LogoUtil.showMessageDialog(this, "Lỗi khi làm mới: " + ex.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Refresh toàn bộ dữ liệu từ services
     */
    private void refreshData() {
        loadDataFromServices();
        loadTableData();
    }
    
    private void handleTimKiem(ActionEvent e) {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String searchIn = (String) cboTimKiem.getSelectedItem();
        
        if (keyword.isEmpty()) {
            loadYeuCauTableData();
            return;
        }
        
        List<YeuCau> filteredList = new ArrayList<>();
        List<YeuCau> userRequests = getFilteredYeuCauList();
        
        for (YeuCau yc : userRequests) {
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
        List<YeuCau> filteredList = getFilteredYeuCauList();
        if (row >= 0 && row < filteredList.size()) {
            YeuCau yc = filteredList.get(row);
            
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
            if (thietBi.getTrangThai() == ThietBi.TrangThaiThietBi.TON_KHO) {
                cboThietBi.setSelectedItem(thietBi);
            }
        }
    }
    
    private void clearForm() {
        if (cboThietBi.getItemCount() > 0) {
            cboThietBi.setSelectedIndex(0);
        }
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
    
    /**
     * Load dữ liệu yêu cầu của user hiện tại
     */
    private void loadYeuCauTableData() {
        tableModelYeuCau.setRowCount(0);
        
        List<YeuCau> userRequests = getFilteredYeuCauList();
        
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
    
    /**
     * Load dữ liệu thiết bị từ service
     */
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
                tb.getNgayMua() != null ? tb.getNgayMua().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "",
                tb.getGiaMua() != null ? tb.getGiaMua().toString() : ""
            };
            tableModelThietBi.addRow(row);
        }
    }
    
    /**
     * Lấy danh sách yêu cầu được filter theo user hiện tại
     */
    private List<YeuCau> getFilteredYeuCauList() {
        List<YeuCau> userRequests = new ArrayList<>();
        NhanVien user = getCurrentUser();
        
        if (user != null && yeuCauList != null) {
            String userNhanVienId = user.getMaNhanVien();
            for (YeuCau yc : yeuCauList) {
                if (userNhanVienId.equals(yc.getNhanVienId())) {
                    userRequests.add(yc);
                }
            }
        }
        
        return userRequests;
    }
    
    /**
     * Populate combo box với thiết bị có sẵn từ service
     */
    private void populateThietBiComboBox() {
        cboThietBi.removeAllItems();
        
        // Add default option
        cboThietBi.addItem(null);
        
        // Add available devices
        if (thietBiList != null) {
            for (ThietBi thietBi : thietBiList) {
                if (thietBi.getTrangThai() == ThietBi.TrangThaiThietBi.TON_KHO) {
                    cboThietBi.addItem(thietBi);
                }
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
        if (id == null || thietBiList == null) return null;
        return thietBiList.stream()
                .filter(tb -> id.equals(tb.getId()))
                .findFirst()
                .orElse(null);
    }
    
    private LoaiThietBi findLoaiThietBiById(Long id) {
        if (id == null || loaiThietBiList == null) return null;
        return loaiThietBiList.stream()
                .filter(loai -> id.equals(loai.getId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Lấy thông tin user hiện tại
     */
    private NhanVien getCurrentUser() {
        if (authController != null && authController.isLoggedIn()) {
            return authController.getCurrentUser();
        }
        return currentUser; // fallback
    }
    
    /**
     * Public method để set current user (fallback khi không có AuthController)
     */
    public void setCurrentUser(NhanVien user) {
        this.currentUser = user;
        if (user != null) {
            lblNhanVien.setText(user.getTenNhanVien());
            logger.info("Đã set current user: {}", user.getTenNhanVien());
        } else {
            lblNhanVien.setText("Chưa đăng nhập");
        }
        loadYeuCauTableData(); // Reload to show user's requests
    }
    
    /**
     * Set AuthController (để sử dụng authentication thực tế)
     */
    public void setAuthController(AuthController authController) {
        this.authController = authController;
        
        // Update current user from auth controller
        if (authController != null && authController.isLoggedIn()) {
            NhanVien user = authController.getCurrentUser();
            setCurrentUser(user);
        }
    }

    /**
     * Chuyển đổi maNhanVien (String) thành Long để tương thích với service
     * Sử dụng hash code để tạo Long từ String
     */
    private Long convertMaNhanVienToLong(String maNhanVien) {
        if (maNhanVien == null) {
            return 0L;
        }
        // Sử dụng hash code và đảm bảo là số dương
        return Math.abs((long) maNhanVien.hashCode());
    }
    
    /**
     * Chuyển đổi String nhanVienId thành String maNhanVien (reverse mapping)
     * Lưu ý: Đây là reverse mapping không hoàn toàn chính xác do hash collision
     * Chỉ sử dụng cho mục đích hiển thị
     */
    private String convertLongToMaNhanVien(String nhanVienId) {
        if (nhanVienId == null) {
            return "N/A";
        }
        // Tìm maNhanVien tương ứng bằng cách so sánh hash
        if (yeuCauList != null) {
            for (YeuCau yc : yeuCauList) {
                if (nhanVienId.equals(yc.getNhanVienId())) {
                    // Tìm trong danh sách nhân viên để lấy maNhanVien
                    // Đây là workaround tạm thời
                    return "NV" + nhanVienId.toString().substring(0, Math.min(3, nhanVienId.toString().length()));
                }
            }
        }
        return "NV" + nhanVienId.toString().substring(0, Math.min(3, nhanVienId.toString().length()));
    }
}
