package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.YeuCau.TrangThaiYeuCau;
import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.dao.CapPhatDAO;
import com.mycompany.device.dao.YeuCauDAO;
import com.mycompany.device.dao.ThietBiDAO;
import com.mycompany.device.dao.impl.CapPhatDAOMySQLImpl;
import com.mycompany.device.dao.impl.YeuCauDAOMySQLImpl;
import com.mycompany.device.dao.impl.ThietBiDAOMySQLImpl;
import com.mycompany.device.util.LogoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.mycompany.device.util.DatabaseConnection;

/**
 * Panel dành cho admin để xem và phê duyệt các yêu cầu
 * @author Kim Ngan - UI Layer
 */
public class AdminYeuCauPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminYeuCauPanel.class);
    
    // Table components
    private JTable tableYeuCau;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Filter components
    private JComboBox<String> cboTrangThaiFilter;
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    
    // Data
    private List<YeuCau> yeuCauList;
    private List<YeuCau> filteredYeuCauList;
    private List<ThietBi> thietBiList;
    private List<NhanVien> nhanVienList;
    private List<PhongBan> phongBanList;
    private List<CapPhat> capPhatList; // Thêm danh sách lịch sử cấp phát
    
    // DAO instances
    private CapPhatDAO capPhatDAO;
    private YeuCauDAO yeuCauDAO;
    private ThietBiDAO thietBiDAO;
    
    // Reference to LichSuCapPhatPanel for synchronization
    private LichSuCapPhatPanel lichSuCapPhatPanel;
    
    // Constants
    private static final String[] COLUMN_NAMES = {
        "ID", "Thiết bị", "Nhân viên", "Phòng ban", "Lý do", "Trạng thái", "Ngày tạo", "Thao tác"
    };
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public AdminYeuCauPanel() {
        initializeDAOs();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        
        logger.info("AdminYeuCauPanel đã được khởi tạo thành công");
    }
    
    private void initializeDAOs() {
        try {
            logger.info("Khởi tạo các DAO...");
            
            // Test database connection first
            if (!DatabaseConnection.getInstance().testConnection()) {
                logger.error("Database connection test FAILED!");
                throw new Exception("Database connection failed");
            }
            logger.info("✅ Database connection test PASSED");
            
            capPhatDAO = new CapPhatDAOMySQLImpl();
            logger.info("✅ CapPhatDAO created");
            
            yeuCauDAO = new YeuCauDAOMySQLImpl();
            logger.info("✅ YeuCauDAO created");
            
            thietBiDAO = new ThietBiDAOMySQLImpl();
            logger.info("✅ ThietBiDAO created");
            
            logger.info("✅ Đã khởi tạo tất cả DAO thành công");
        } catch (Exception e) {
            logger.error("❌ Lỗi khi khởi tạo DAO", e);
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi kết nối database: " + e.getMessage(), 
                "Lỗi Database", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeData() {
        yeuCauList = new ArrayList<>();
        filteredYeuCauList = new ArrayList<>();
        thietBiList = new ArrayList<>();
        nhanVienList = new ArrayList<>();
        phongBanList = new ArrayList<>();
        capPhatList = new ArrayList<>();
        
        // Load data from database
        loadDataFromDatabase();
    }
    
    private void loadDataFromDatabase() {
        try {
            // Load all data from database
            yeuCauList = yeuCauDAO.getAllYeuCau();
            thietBiList = thietBiDAO.findAll();
            capPhatList = capPhatDAO.getAllCapPhat();
            
            // Mock data for NhanVien and PhongBan (có thể thay bằng DAO thực tế)
            initializeMockNhanVienPhongBan();
            
            logger.info("Đã tải dữ liệu từ database: {} yêu cầu, {} thiết bị, {} cấp phát", 
                yeuCauList.size(), thietBiList.size(), capPhatList.size());
            
        } catch (Exception e) {
            logger.error("Lỗi khi tải dữ liệu từ database", e);
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi Database", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeMockNhanVienPhongBan() {
        // Mock data for NhanVien and PhongBan (có thể thay bằng DAO thực tế)
        nhanVienList.add(new NhanVien("NV001", "Nguyễn Văn An", "an.nguyen@company.com", 
                "password", "0901234567", NhanVien.NhanVienRole.STAFF, "PB001"));
        nhanVienList.add(new NhanVien("NV002", "Trần Thị Bình", "binh.tran@company.com", 
                "password", "0901234568", NhanVien.NhanVienRole.STAFF, "PB002"));
        nhanVienList.add(new NhanVien("NV003", "Lê Văn Cường", "cuong.le@company.com", 
                "password", "0901234569", NhanVien.NhanVienRole.STAFF, "PB001"));
        
        phongBanList.add(new PhongBan("PB001", "Phòng IT", "Phòng Công nghệ thông tin"));
        phongBanList.add(new PhongBan("PB002", "Phòng Kế toán", "Phòng Kế toán và Tài chính"));
        phongBanList.add(new PhongBan("PB003", "Phòng Nhân sự", "Phòng Quản lý Nhân sự"));
    }
    
    private void initializeComponents() {
        // Filter components
        cboTrangThaiFilter = new JComboBox<>();
        cboTrangThaiFilter.addItem("Tất cả");
        cboTrangThaiFilter.addItem(TrangThaiYeuCau.CHO_DUYET.getDisplayName());
        cboTrangThaiFilter.addItem(TrangThaiYeuCau.DA_DUYET.getDisplayName());
        cboTrangThaiFilter.addItem(TrangThaiYeuCau.TU_CHOI.getDisplayName());
        
        txtTimKiem = new JTextField(20);
        txtTimKiem.setToolTipText("Tìm kiếm theo tên thiết bị, người yêu cầu hoặc phòng ban");
        
        btnTimKiem = new JButton("Tìm kiếm");
        btnLamMoi = new JButton("Làm mới");
        
        // Style buttons
        styleButton(btnTimKiem);
        styleButton(btnLamMoi);
        
        // Table setup
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only action column is editable
            }
        };
        
        tableYeuCau = new JTable(tableModel);
        setupTable();
        
        scrollPane = new JScrollPane(tableYeuCau);
        scrollPane.setPreferredSize(new Dimension(800, 400));
    }
    
    private void setupTable() {
        // Set column widths
        tableYeuCau.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tableYeuCau.getColumnModel().getColumn(1).setPreferredWidth(180); // Thiết bị
        tableYeuCau.getColumnModel().getColumn(2).setPreferredWidth(130); // Người yêu cầu
        tableYeuCau.getColumnModel().getColumn(3).setPreferredWidth(120); // Phòng ban
        tableYeuCau.getColumnModel().getColumn(4).setPreferredWidth(180); // Lý do
        tableYeuCau.getColumnModel().getColumn(5).setPreferredWidth(100); // Trạng thái
        tableYeuCau.getColumnModel().getColumn(6).setPreferredWidth(120); // Ngày tạo
        tableYeuCau.getColumnModel().getColumn(7).setPreferredWidth(150); // Thao tác
        
        // Set action column renderer and editor
        tableYeuCau.getColumn("Thao tác").setCellRenderer(new ActionButtonRenderer());
        tableYeuCau.getColumn("Thao tác").setCellEditor(new ActionButtonEditor());
        
        // Set row height
        tableYeuCau.setRowHeight(35);
        
        // Alternate row colors
        tableYeuCau.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Quản lý Yêu cầu - Admin"));
        
        // Top panel for filters
        JPanel topPanel = createFilterPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for table
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for statistics
        JPanel bottomPanel = createStatisticsPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Bộ lọc"));
        
        panel.add(new JLabel("Trạng thái:"));
        panel.add(cboTrangThaiFilter);
        
        panel.add(Box.createHorizontalStrut(20));
        
        panel.add(new JLabel("Tìm kiếm:"));
        panel.add(txtTimKiem);
        panel.add(btnTimKiem);
        
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnLamMoi);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Thống kê"));
        
        // Statistics will be updated dynamically
        JLabel lblStats = new JLabel("Tổng yêu cầu: 0 | Chờ duyệt: 0 | Đã duyệt: 0 | Từ chối: 0");
        panel.add(lblStats);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        btnTimKiem.addActionListener(e -> applyFilters());
        btnLamMoi.addActionListener(e -> {
            cboTrangThaiFilter.setSelectedIndex(0);
            txtTimKiem.setText("");
            loadData();
        });
        
        cboTrangThaiFilter.addActionListener(e -> applyFilters());
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
    
    private void loadData() {
        filteredYeuCauList = new ArrayList<>(yeuCauList);
        updateTable();
        updateStatistics();
    }
    
    private void applyFilters() {
        String selectedStatus = (String) cboTrangThaiFilter.getSelectedItem();
        String searchText = txtTimKiem.getText().toLowerCase().trim();
        
        filteredYeuCauList = yeuCauList.stream()
            .filter(yeuCau -> {
                // Filter by status
                if (!"Tất cả".equals(selectedStatus)) {
                    if (!yeuCau.getTrangThai().getDisplayName().equals(selectedStatus)) {
                        return false;
                    }
                }
                
                // Filter by search text
                if (!searchText.isEmpty()) {
                    String tenThietBi = getThietBiName(yeuCau.getThietBiId()).toLowerCase();
                    String tenNhanVien = getNhanVienName(yeuCau.getNhanVienId()).toLowerCase();
                    String tenPhongBan = getPhongBanName(yeuCau.getNhanVienId()).toLowerCase();
                    
                    if (!tenThietBi.contains(searchText) && !tenNhanVien.contains(searchText) && !tenPhongBan.contains(searchText)) {
                        return false;
                    }
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        updateTable();
        updateStatistics();
    }
    
    private void updateTable() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add filtered data
        for (YeuCau yeuCau : filteredYeuCauList) {
            Object[] rowData = {
                yeuCau.getId(),
                getThietBiName(yeuCau.getThietBiId()),
                getNhanVienName(yeuCau.getNhanVienId()),
                getPhongBanName(yeuCau.getNhanVienId()),
                yeuCau.getLyDo(),
                yeuCau.getTrangThai().getDisplayName(),
                yeuCau.getNgayTao().format(DATE_FORMATTER),
                "Actions" // Placeholder for buttons
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void updateStatistics() {
        int total = filteredYeuCauList.size();
        int choDuyet = (int) filteredYeuCauList.stream()
            .filter(yc -> yc.getTrangThai() == TrangThaiYeuCau.CHO_DUYET)
            .count();
        int daDuyet = (int) filteredYeuCauList.stream()
            .filter(yc -> yc.getTrangThai() == TrangThaiYeuCau.DA_DUYET)
            .count();
        int tuChoi = (int) filteredYeuCauList.stream()
            .filter(yc -> yc.getTrangThai() == TrangThaiYeuCau.TU_CHOI)
            .count();
        
        // Update statistics label (find it in bottom panel)
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel lblStats = (JLabel) comp;
                lblStats.setText(String.format(
                    "Tổng yêu cầu: %d | Chờ duyệt: %d | Đã duyệt: %d | Từ chối: %d",
                    total, choDuyet, daDuyet, tuChoi));
                break;
            }
        }
    }
    
    private String getThietBiName(Long thietBiId) {
        return thietBiList.stream()
            .filter(tb -> tb.getId().equals(thietBiId))
            .findFirst()
            .map(tb -> tb.getGhiChu() != null ? tb.getGhiChu() : ("TB-" + tb.getId()))
            .orElse("N/A");
    }
    
    private String getNhanVienName(String nhanVienId) {
        return nhanVienList.stream()
            .filter(nv -> nv.getMaNhanVien().equals(nhanVienId))
            .findFirst()
            .map(NhanVien::getTenNhanVien)
            .orElse("N/A");
    }
    
    private String getPhongBanName(String nhanVienId) {
        return nhanVienList.stream()
            .filter(nv -> nv.getMaNhanVien().equals(nhanVienId))
            .findFirst()
            .map(NhanVien::getMaPhongBan)
            .map(maPhongBan -> phongBanList.stream()
                .filter(pb -> pb.getMaPhongBan().equals(maPhongBan))
                .findFirst()
                .map(PhongBan::getTenPhongBan)
                .orElse("N/A"))
            .orElse("N/A");
    }
    
    /**
     * Cập nhật method approveRequest để lưu vào database với debug logging
     */
    private void approveRequest(int row) {
        if (row >= 0 && row < filteredYeuCauList.size()) {
            YeuCau yeuCau = filteredYeuCauList.get(row);
            
            if (yeuCau.getTrangThai() == TrangThaiYeuCau.CHO_DUYET) {
                try {
                    logger.info("=== BẮT ĐẦU PHÊ DUYỆT YÊU CẦU ===");
                    logger.info("Yêu cầu ID: {}", yeuCau.getId());
                    logger.info("Thiết bị ID: {}", yeuCau.getThietBiId());
                    logger.info("Nhân viên ID: {}", yeuCau.getNhanVienId());
                    
                    // Kiểm tra database connection
                    if (capPhatDAO == null) {
                        logger.error("CapPhatDAO is NULL!");
                        LogoUtil.showMessageDialog(this, 
                            "Lỗi: CapPhatDAO chưa được khởi tạo",
                            "Lỗi Database", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Update status in database
                    logger.info("Cập nhật trạng thái yêu cầu...");
                    yeuCau.setTrangThai(TrangThaiYeuCau.DA_DUYET);
                    yeuCau.setNgayCapNhat(LocalDateTime.now());
                    boolean yeuCauUpdated = yeuCauDAO.updateYeuCau(yeuCau);
                    
                    if (!yeuCauUpdated) {
                        logger.error("Lỗi khi cập nhật trạng thái yêu cầu trong database");
                        LogoUtil.showMessageDialog(this, 
                            "Lỗi khi cập nhật trạng thái yêu cầu trong database",
                            "Lỗi Database", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    logger.info("✅ Đã cập nhật trạng thái yêu cầu thành công");
                    
                    // Create CapPhat record and save to database
                    logger.info("Tạo CapPhat record...");
                    CapPhat capPhat = new CapPhat();
                    capPhat.setYeuCauId(yeuCau.getId());
                    capPhat.setNgayCap(LocalDateTime.now());
                    capPhat.setNgayTra(null); // Chưa trả
                    capPhat.setTinhTrangTra(null); // Chưa có tình trạng trả
                    capPhat.setGhiChu("Thiết bị đã được cấp phát cho nhân viên");
                    
                    logger.info("CapPhat record details:");
                    logger.info("  - YeuCauId: {}", capPhat.getYeuCauId());
                    logger.info("  - NgayCap: {}", capPhat.getNgayCap());
                    logger.info("  - NgayTra: {}", capPhat.getNgayTra());
                    logger.info("  - TinhTrangTra: {}", capPhat.getTinhTrangTra());
                    logger.info("  - GhiChu: {}", capPhat.getGhiChu());
                    
                    // Save to database
                    logger.info("Lưu CapPhat vào database...");
                    boolean capPhatSaved = capPhatDAO.createCapPhat(capPhat);
                    
                    if (!capPhatSaved) {
                        logger.error("❌ Lỗi khi tạo lịch sử cấp phát trong database");
                        LogoUtil.showMessageDialog(this, 
                            "Lỗi khi tạo lịch sử cấp phát trong database",
                            "Lỗi Database", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    logger.info("✅ Đã tạo CapPhat thành công: ID={}", capPhat.getId());
                    
                    // Update device status in database
                    logger.info("Cập nhật trạng thái thiết bị...");
                    updateThietBiStatus(yeuCau.getThietBiId(), ThietBi.TrangThaiThietBi.DANG_CAP_PHAT);
                    
                    // Add to local list for UI update
                    capPhatList.add(capPhat);
                    
                    // Đồng bộ với LichSuCapPhatPanel nếu có
                    if (lichSuCapPhatPanel != null) {
                        lichSuCapPhatPanel.addCapPhat(capPhat);
                    }
                    
                    // Show success message
                    LogoUtil.showMessageDialog(this, 
                        "Yêu cầu đã được phê duyệt thành công!\n" +
                        "ID Yêu cầu: " + yeuCau.getId() + "\n" +
                        "Đã tạo lịch sử cấp phát với ID: " + capPhat.getId() + "\n" +
                        "Thiết bị đã được chuyển sang trạng thái 'Đang cấp phát'",
                        "Phê duyệt thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    logger.info("=== HOÀN THÀNH PHÊ DUYỆT YÊU CẦU ===");
                    logger.info("Yêu cầu ID: {}, CapPhat ID: {}", yeuCau.getId(), capPhat.getId());
                    
                    // Refresh data
                    updateTable();
                    updateStatistics();
                    
                } catch (Exception e) {
                    logger.error("❌ Lỗi khi phê duyệt yêu cầu", e);
                    LogoUtil.showMessageDialog(this, 
                        "Lỗi khi phê duyệt yêu cầu: " + e.getMessage(),
                        "Lỗi Database", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                LogoUtil.showMessageDialog(this, 
                    "Chỉ có thể phê duyệt yêu cầu đang ở trạng thái 'Chờ duyệt'",
                    "Không thể phê duyệt", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void rejectRequest(int row) {
        if (row >= 0 && row < filteredYeuCauList.size()) {
            YeuCau yeuCau = filteredYeuCauList.get(row);
            
            if (yeuCau.getTrangThai() == TrangThaiYeuCau.CHO_DUYET) {
                // Update status without asking for reason
                yeuCau.setTrangThai(TrangThaiYeuCau.TU_CHOI);
                yeuCau.setNgayCapNhat(LocalDateTime.now());
                
                // Show success message
                LogoUtil.showMessageDialog(this, 
                    "Yêu cầu đã được từ chối thành công!\nID Yêu cầu: " + yeuCau.getId(),
                    "Từ chối thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                updateTable();
                updateStatistics();
            } else {
                LogoUtil.showMessageDialog(this, 
                    "Chỉ có thể từ chối yêu cầu đang ở trạng thái 'Chờ duyệt'",
                    "Không thể từ chối", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    /**
     * Cập nhật trạng thái thiết bị
     */
    private void updateThietBiStatus(Long thietBiId, ThietBi.TrangThaiThietBi newStatus) {
        try {
            ThietBi thietBi = thietBiDAO.findById(thietBiId);
            if (thietBi != null) {
                thietBi.setTrangThai(newStatus);
                boolean updated = thietBiDAO.update(thietBi);
                if (updated) {
                    logger.info("Đã cập nhật trạng thái thiết bị: ID={}, Trạng thái={}", 
                        thietBiId, newStatus);
                } else {
                    logger.error("Lỗi khi cập nhật trạng thái thiết bị: ID={}", thietBiId);
                }
            }
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật trạng thái thiết bị", e);
        }
    }
    
    /**
     * Lấy danh sách lịch sử cấp phát (để sử dụng trong LichSuCapPhatPanel)
     */
    public List<CapPhat> getCapPhatList() {
        return capPhatList;
    }
    
    public List<YeuCau> getYeuCauList() {
        return yeuCauList;
    }
    
    public List<ThietBi> getThietBiList() {
        return thietBiList;
    }
    
    public List<NhanVien> getNhanVienList() {
        return nhanVienList;
    }
    
    public List<PhongBan> getPhongBanList() {
        return phongBanList;
    }
    
    /**
     * Thêm lịch sử cấp phát mới
     */
    public void addCapPhat(CapPhat capPhat) {
        capPhatList.add(capPhat);
    }
    
    /**
     * Đồng bộ dữ liệu giữa AdminYeuCauPanel và LichSuCapPhatPanel
     */
    public void syncDataBetweenPanels() {
        if (this != null && lichSuCapPhatPanel != null) {
            // Cập nhật dữ liệu từ AdminYeuCauPanel sang LichSuCapPhatPanel
            lichSuCapPhatPanel.updateDataFromAdminPanel(
                this.getCapPhatList(),
                this.getYeuCauList(),
                this.getThietBiList(),
                this.getNhanVienList(),
                this.getPhongBanList()
            );
        }
    }
    
    /**
     * Thông báo dữ liệu đã thay đổi (để MainFrame có thể đồng bộ)
     */
    private void notifyDataChanged() {
        // Có thể thêm observer pattern hoặc callback ở đây
        // Hiện tại chỉ log để debug
        System.out.println("Dữ liệu đã thay đổi - cần đồng bộ với LichSuCapPhatPanel");
    }
    
    // Custom renderer for action buttons
    private class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnApprove;
        private JButton btnReject;
        
        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            
            btnApprove = new JButton("Duyệt");
            btnApprove.setPreferredSize(new Dimension(60, 25));
            btnApprove.setBackground(new Color(40, 167, 69));
            btnApprove.setForeground(Color.WHITE);
            btnApprove.setFocusPainted(false);
            btnApprove.setBorder(BorderFactory.createEmptyBorder());
            btnApprove.setOpaque(true);
            btnApprove.setBorderPainted(false);
            
            btnReject = new JButton("Từ chối");
            btnReject.setPreferredSize(new Dimension(60, 25));
            btnReject.setBackground(new Color(220, 53, 69));
            btnReject.setForeground(Color.WHITE);
            btnReject.setFocusPainted(false);
            btnReject.setBorder(BorderFactory.createEmptyBorder());
            btnReject.setOpaque(true);
            btnReject.setBorderPainted(false);
            
            add(btnApprove);
            add(btnReject);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (row < filteredYeuCauList.size()) {
                YeuCau yeuCau = filteredYeuCauList.get(row);
                boolean canApprove = yeuCau.getTrangThai() == TrangThaiYeuCau.CHO_DUYET;
                
                btnApprove.setEnabled(canApprove);
                btnReject.setEnabled(canApprove);
                
                if (!canApprove) {
                    // Trạng thái đã duyệt hoặc từ chối - button màu xám
                    btnApprove.setBackground(Color.LIGHT_GRAY);
                    btnReject.setBackground(Color.LIGHT_GRAY);
                    btnApprove.setForeground(Color.DARK_GRAY);
                    btnReject.setForeground(Color.DARK_GRAY);
                } else {
                    // Trạng thái chờ duyệt - button có màu đặc trưng
                    btnApprove.setBackground(new Color(40, 167, 69)); // Xanh lá cây
                    btnReject.setBackground(new Color(220, 53, 69));  // Đỏ
                    btnApprove.setForeground(Color.WHITE);
                    btnReject.setForeground(Color.WHITE);
                }
                
                // Force repaint
                btnApprove.repaint();
                btnReject.repaint();
            }
            
            return this;
        }
    }
    
    // Custom editor for action buttons
    private class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnApprove;
        private JButton btnReject;
        private int currentRow;
        
        public ActionButtonEditor() {
            super(new JCheckBox());
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            
            btnApprove = new JButton("Duyệt");
            btnApprove.setPreferredSize(new Dimension(60, 25));
            btnApprove.setBackground(new Color(40, 167, 69));
            btnApprove.setForeground(Color.WHITE);
            btnApprove.setFocusPainted(false);
            btnApprove.setBorder(BorderFactory.createEmptyBorder());
            btnApprove.setOpaque(true);
            btnApprove.setBorderPainted(false);
            
            btnReject = new JButton("Từ chối");
            btnReject.setPreferredSize(new Dimension(60, 25));
            btnReject.setBackground(new Color(220, 53, 69));
            btnReject.setForeground(Color.WHITE);
            btnReject.setFocusPainted(false);
            btnReject.setBorder(BorderFactory.createEmptyBorder());
            btnReject.setOpaque(true);
            btnReject.setBorderPainted(false);
            
            btnApprove.addActionListener(e -> {
                fireEditingStopped();
                approveRequest(currentRow);
            });
            
            btnReject.addActionListener(e -> {
                fireEditingStopped();
                rejectRequest(currentRow);
            });
            
            panel.add(btnApprove);
            panel.add(btnReject);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.currentRow = row;
            
            if (row < filteredYeuCauList.size()) {
                YeuCau yeuCau = filteredYeuCauList.get(row);
                boolean canApprove = yeuCau.getTrangThai() == TrangThaiYeuCau.CHO_DUYET;
                
                btnApprove.setEnabled(canApprove);
                btnReject.setEnabled(canApprove);
                
                if (!canApprove) {
                    // Trạng thái đã duyệt hoặc từ chối - button màu xám
                    btnApprove.setBackground(Color.LIGHT_GRAY);
                    btnReject.setBackground(Color.LIGHT_GRAY);
                    btnApprove.setForeground(Color.DARK_GRAY);
                    btnReject.setForeground(Color.DARK_GRAY);
                } else {
                    // Trạng thái chờ duyệt - button có màu đặc trưng
                    btnApprove.setBackground(new Color(40, 167, 69)); // Xanh lá cây
                    btnReject.setBackground(new Color(220, 53, 69));  // Đỏ
                    btnApprove.setForeground(Color.WHITE);
                    btnReject.setForeground(Color.WHITE);
                }
            }
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
    
    // Alternating row colors
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(245, 245, 245));
                }
            }
            
            return c;
        }
    }

    /**
     * Set reference to LichSuCapPhatPanel for synchronization
     */
    public void setLichSuCapPhatPanel(LichSuCapPhatPanel lichSuCapPhatPanel) {
        this.lichSuCapPhatPanel = lichSuCapPhatPanel;
    }
    
    /**
     * Refresh data from database
     */
    public void refreshData() {
        loadDataFromDatabase();
        loadData();
        logger.info("Đã refresh dữ liệu từ database");
    }
}
