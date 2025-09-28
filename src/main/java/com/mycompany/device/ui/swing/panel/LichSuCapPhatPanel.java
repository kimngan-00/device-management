package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.dao.CapPhatDAO;
import com.mycompany.device.dao.ThietBiDAO;
import com.mycompany.device.dao.YeuCauDAO;
import com.mycompany.device.dao.NhanVienDAO;
import com.mycompany.device.dao.PhongBanDAO;
import com.mycompany.device.dao.impl.CapPhatDAOMySQLImpl;
import com.mycompany.device.dao.impl.ThietBiDAOMySQLImpl;
import com.mycompany.device.dao.impl.YeuCauDAOMySQLImpl;
import com.mycompany.device.dao.impl.NhanVienDAOMySQLImpl;
import com.mycompany.device.dao.impl.PhongBanDAOMySQLImpl;
import com.mycompany.device.util.LogoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Panel lịch sử cấp phát thiết bị cho admin
 * Hiển thị danh sách lịch sử cấp phát với bộ lọc theo thiết bị
 * @author Kim Ngan - UI Layer
 */
public class LichSuCapPhatPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(LichSuCapPhatPanel.class);
    
    // Fonts and Colors
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    
    // UI Components
    private JComboBox<ThietBi> cboThietBi;
    private JButton btnLoc;
    private JButton btnTatCa;
    private JTable tblLichSu;
    private DefaultTableModel tableModel;
    private JLabel lblTongSo;
    
    // Data
    private List<CapPhat> capPhatList;
    private List<ThietBi> thietBiList;
    private List<YeuCau> yeuCauList;
    private List<NhanVien> nhanVienList;
    private List<PhongBan> phongBanList;
    
    // DAO instances
    private CapPhatDAO capPhatDAO;
    private ThietBiDAO thietBiDAO;
    private YeuCauDAO yeuCauDAO;
    private NhanVienDAO nhanVienDAO;
    private PhongBanDAO phongBanDAO;
    
    public LichSuCapPhatPanel() {
        initializeDAOs();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadLichSuData();
        
        logger.info("LichSuCapPhatPanel đã được khởi tạo thành công");
    }
    
    private void initializeDAOs() {
        try {
            capPhatDAO = new CapPhatDAOMySQLImpl();
            thietBiDAO = new ThietBiDAOMySQLImpl();
            yeuCauDAO = new YeuCauDAOMySQLImpl();
            nhanVienDAO = new NhanVienDAOMySQLImpl();
            phongBanDAO = new PhongBanDAOMySQLImpl();
            logger.info("Đã khởi tạo các DAO thành công");
        } catch (Exception e) {
            logger.error("Lỗi khi khởi tạo DAO", e);
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi kết nối database: " + e.getMessage(), 
                "Lỗi Database", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeData() {
        capPhatList = new ArrayList<>();
        thietBiList = new ArrayList<>();
        yeuCauList = new ArrayList<>();
        nhanVienList = new ArrayList<>();
        phongBanList = new ArrayList<>();
        
        // Load data from database
        loadDataFromDatabase();
    }
    
    private void loadDataFromDatabase() {
        try {
            // Load all data from database
            capPhatList = capPhatDAO.getAllCapPhat();
            thietBiList = thietBiDAO.findAll();
            yeuCauList = yeuCauDAO.getAllYeuCau();
            nhanVienList = nhanVienDAO.getAllNhanVien();
            phongBanList = phongBanDAO.getAllPhongBan();
            
            logger.info("Đã tải dữ liệu từ database: {} cấp phát, {} thiết bị, {} yêu cầu, {} nhân viên, {} phòng ban", 
                capPhatList.size(), thietBiList.size(), yeuCauList.size(), nhanVienList.size(), phongBanList.size());
            
            // Validate business logic after loading data
            validateBusinessLogic();
            
        } catch (Exception e) {
            logger.error("Lỗi khi tải dữ liệu từ database", e);
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi Database", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeComponents() {
        // ComboBox thiết bị
        cboThietBi = new JComboBox<>();
        cboThietBi.setFont(LABEL_FONT);
        cboThietBi.setRenderer(new ThietBiComboRenderer());
        loadThietBiComboBox();
        
        // Buttons
        btnLoc = new JButton("Lọc");
        btnLoc.setFont(LABEL_FONT);
        btnLoc.setBackground(PRIMARY_COLOR);
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorderPainted(false);
        
        btnTatCa = new JButton("Tất cả");
        btnTatCa.setFont(LABEL_FONT);
        btnTatCa.setBackground(Color.GRAY);
        btnTatCa.setForeground(Color.WHITE);
        btnTatCa.setFocusPainted(false);
        btnTatCa.setBorderPainted(false);
        
        // Table
        String[] columnNames = {
            "ID", "Tên thiết bị", "Người được cấp", "Phòng ban", 
            "Ngày tạo yêu cầu", "Ngày cấp", "Ngày trả", "Tình trạng", "Ghi chú"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblLichSu = new JTable(tableModel);
        
        tblLichSu.setFont(LABEL_FONT);
        tblLichSu.getTableHeader().setFont(TABLE_HEADER_FONT);
        tblLichSu.setRowHeight(45); // Tăng chiều cao row để có chỗ cho wrapped text
        tblLichSu.getTableHeader().setPreferredSize(new Dimension(0, 35)); // Tăng chiều cao header
        tblLichSu.setGridColor(Color.LIGHT_GRAY);
        tblLichSu.setSelectionBackground(new Color(184, 207, 229));
        tblLichSu.setShowGrid(true);
        tblLichSu.setIntercellSpacing(new Dimension(1, 1));
        
        // Add custom renderer cho tất cả các cột để wrap text
        tblLichSu.getColumnModel().getColumn(0).setCellRenderer(new WrapTextCellRenderer(false)); // ID
        tblLichSu.getColumnModel().getColumn(1).setCellRenderer(new WrapTextCellRenderer(true));  // Tên thiết bị
        tblLichSu.getColumnModel().getColumn(2).setCellRenderer(new WrapTextCellRenderer(true));  // Người được cấp
        tblLichSu.getColumnModel().getColumn(3).setCellRenderer(new WrapTextCellRenderer(true));  // Phòng ban
        tblLichSu.getColumnModel().getColumn(4).setCellRenderer(new WrapTextCellRenderer(false)); // Ngày tạo yêu cầu
        tblLichSu.getColumnModel().getColumn(5).setCellRenderer(new WrapTextCellRenderer(false)); // Ngày cấp
        tblLichSu.getColumnModel().getColumn(6).setCellRenderer(new NgayTraCellRenderer());       // Ngày trả (special renderer)
        tblLichSu.getColumnModel().getColumn(7).setCellRenderer(new WrapTextCellRenderer(false)); // Tình trạng
        tblLichSu.getColumnModel().getColumn(8).setCellRenderer(new WrapTextCellRenderer(true));  // Ghi chú
        
        // Adjust column widths để hiển thị đầy đủ nội dung với horizontal scroll
        tblLichSu.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID
        tblLichSu.getColumnModel().getColumn(0).setMinWidth(60);
        
        tblLichSu.getColumnModel().getColumn(1).setPreferredWidth(250);  // Tên thiết bị
        tblLichSu.getColumnModel().getColumn(1).setMinWidth(200);
        
        tblLichSu.getColumnModel().getColumn(2).setPreferredWidth(180);  // Người được cấp  
        tblLichSu.getColumnModel().getColumn(2).setMinWidth(140);
        
        tblLichSu.getColumnModel().getColumn(3).setPreferredWidth(160);  // Phòng ban
        tblLichSu.getColumnModel().getColumn(3).setMinWidth(120);
        
        tblLichSu.getColumnModel().getColumn(4).setPreferredWidth(150);  // Ngày tạo yêu cầu
        tblLichSu.getColumnModel().getColumn(4).setMinWidth(130);
        
        tblLichSu.getColumnModel().getColumn(5).setPreferredWidth(130);  // Ngày cấp
        tblLichSu.getColumnModel().getColumn(5).setMinWidth(110);
        
        tblLichSu.getColumnModel().getColumn(6).setPreferredWidth(130);  // Ngày trả
        tblLichSu.getColumnModel().getColumn(6).setMinWidth(110);
        
        tblLichSu.getColumnModel().getColumn(7).setPreferredWidth(130);  // Tình trạng
        tblLichSu.getColumnModel().getColumn(7).setMinWidth(100);
        
        tblLichSu.getColumnModel().getColumn(8).setPreferredWidth(250);  // Ghi chú
        tblLichSu.getColumnModel().getColumn(8).setMinWidth(200);
        
        // Tổng số label
        lblTongSo = new JLabel("Tổng số: 0");
        lblTongSo.setFont(LABEL_FONT);
        lblTongSo.setForeground(PRIMARY_COLOR);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title và description
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel("📊 Lịch sử cấp phát thiết bị");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel descLabel = new JLabel("Theo dõi toàn bộ lịch sử cấp phát và trả thiết bị trong hệ thống");
        descLabel.setFont(LABEL_FONT);
        descLabel.setForeground(Color.GRAY);
        
        JLabel hintLabel = new JLabel("💡 Tip: Sử dụng thanh scroll ngang để xem đầy đủ thông tin các cột");
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hintLabel.setForeground(new Color(108, 117, 125));
        
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(descLabel);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(hintLabel);
        
        // Chú thích màu sắc
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        legendPanel.setBackground(CARD_COLOR);
        
        JPanel chuaTraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chuaTraPanel.setBackground(new Color(255, 230, 230));
        chuaTraPanel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        JLabel chuaTraLabel = new JLabel("Chưa trả");
        chuaTraLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        chuaTraPanel.add(chuaTraLabel);
        
        JPanel daTraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        daTraPanel.setBackground(new Color(230, 255, 230));
        daTraPanel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        JLabel daTraLabel = new JLabel("Đã trả");
        daTraLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        daTraPanel.add(daTraLabel);
        
        legendPanel.add(new JLabel("Chú thích: "));
        legendPanel.add(chuaTraPanel);
        legendPanel.add(Box.createHorizontalStrut(5));
        legendPanel.add(daTraPanel);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(legendPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Filter controls
        JPanel filterControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterControlPanel.setBackground(CARD_COLOR);
        filterControlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        filterControlPanel.add(new JLabel("Chọn thiết bị:"));
        filterControlPanel.add(Box.createHorizontalStrut(10));
        filterControlPanel.add(cboThietBi);
        filterControlPanel.add(Box.createHorizontalStrut(15));
        filterControlPanel.add(btnLoc);
        filterControlPanel.add(Box.createHorizontalStrut(10));
        filterControlPanel.add(btnTatCa);
        
        mainPanel.add(filterControlPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Configure scroll pane with both vertical and horizontal scrollbars
        JScrollPane scrollPane = new JScrollPane(tblLichSu);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Ensure table doesn't auto-resize columns to fit viewport
        tblLichSu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(lblTongSo, BorderLayout.SOUTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private void setupEventHandlers() {
        btnLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByThietBi();
            }
        });
        
        btnTatCa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cboThietBi.setSelectedIndex(0); // Reset to "Chọn thiết bị"
                loadLichSuData();
            }
        });
        
        // Auto-filter khi chọn thiết bị từ combobox
        cboThietBi.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object selectedItem = e.getItem();
                    if (selectedItem instanceof ThietBi) {
                        filterByThietBi();
                    } else if (selectedItem == null) {
                        loadLichSuData();
                    }
                }
            }
        });
    }
    
    private void loadThietBiComboBox() {
        cboThietBi.removeAllItems();
        cboThietBi.addItem(null); // "Chọn thiết bị" option
        for (ThietBi thietBi : thietBiList) {
            cboThietBi.addItem(thietBi);
        }
    }
    
    private void loadLichSuData() {
        tableModel.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Sort CapPhat list by ngayCap descending (newest first)
        List<CapPhat> sortedCapPhatList = capPhatList.stream()
            .sorted((cp1, cp2) -> {
                if (cp1.getNgayCap() != null && cp2.getNgayCap() != null) {
                    return cp2.getNgayCap().compareTo(cp1.getNgayCap());
                }
                return 0;
            })
            .collect(Collectors.toList());
        
        for (CapPhat capPhat : sortedCapPhatList) {
            YeuCau yeuCau = findYeuCauById(capPhat.getYeuCauId());
            if (yeuCau == null) continue;
            
            ThietBi thietBi = findThietBiById(yeuCau.getThietBiId());
            NhanVien nhanVien = findNhanVienById(yeuCau.getNhanVienId());
            PhongBan phongBan = null;
            
            if (nhanVien != null) {
                phongBan = findPhongBanByMa(nhanVien.getMaPhongBan());
            }
            
            // Determine tình trạng based on ngayTra  
            String tinhTrangTraHienThi = capPhat.getTinhTrangTra() != null ? 
                capPhat.getTinhTrangTra().getDisplayName() : 
                (capPhat.getNgayTra() != null ? "N/A" : "Đang sử dụng");
            
            Object[] rowData = {
                capPhat.getId(),
                thietBi != null ? thietBi.getGhiChu() : "N/A",
                nhanVien != null ? nhanVien.getTenNhanVien() : "N/A",
                phongBan != null ? phongBan.getTenPhongBan() : "N/A",
                yeuCau.getNgayTao() != null ? yeuCau.getNgayTao().format(formatter) : "N/A",
                capPhat.getNgayCap() != null ? capPhat.getNgayCap().format(formatter) : "N/A",
                capPhat.getNgayTra() != null ? capPhat.getNgayTra().format(formatter) : "Chưa trả",
                tinhTrangTraHienThi,
                capPhat.getGhiChu() != null ? capPhat.getGhiChu() : ""
            };
            
            tableModel.addRow(rowData);
        }
        
        updateTongSoLabel();
    }
    
    private void filterByThietBi() {
        ThietBi selectedThietBi = (ThietBi) cboThietBi.getSelectedItem();
        if (selectedThietBi == null) {
            // Nếu không chọn thiết bị nào, hiển thị tất cả
            loadLichSuData();
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Filter all CapPhat records for the selected device
        List<CapPhat> filteredCapPhat = capPhatList.stream()
            .filter(capPhat -> {
                YeuCau yeuCau = findYeuCauById(capPhat.getYeuCauId());
                return yeuCau != null && yeuCau.getThietBiId().equals(selectedThietBi.getId());
            })
            .sorted((cp1, cp2) -> {
                // Sort by ngayCap descending (newest first)
                if (cp1.getNgayCap() != null && cp2.getNgayCap() != null) {
                    return cp2.getNgayCap().compareTo(cp1.getNgayCap());
                }
                return 0;
            })
            .collect(Collectors.toList());
        
        // Display filtered results
        for (CapPhat capPhat : filteredCapPhat) {
            YeuCau yeuCau = findYeuCauById(capPhat.getYeuCauId());
            if (yeuCau == null) continue;
            
            ThietBi thietBi = findThietBiById(yeuCau.getThietBiId());
            NhanVien nhanVien = findNhanVienById(yeuCau.getNhanVienId());
            PhongBan phongBan = null;
            
            if (nhanVien != null) {
                phongBan = findPhongBanByMa(nhanVien.getMaPhongBan());
            }
            
            // Determine tình trạng based on ngayTra
            String tinhTrangTraHienThi = capPhat.getTinhTrangTra() != null ? 
                capPhat.getTinhTrangTra().getDisplayName() : 
                (capPhat.getNgayTra() != null ? "N/A" : "Đang sử dụng");
            
            Object[] rowData = {
                capPhat.getId(),
                thietBi != null ? thietBi.getGhiChu() : "N/A",
                nhanVien != null ? nhanVien.getTenNhanVien() : "N/A",
                phongBan != null ? phongBan.getTenPhongBan() : "N/A",
                yeuCau.getNgayTao() != null ? yeuCau.getNgayTao().format(formatter) : "N/A",
                capPhat.getNgayCap() != null ? capPhat.getNgayCap().format(formatter) : "N/A",
                capPhat.getNgayTra() != null ? capPhat.getNgayTra().format(formatter) : "Chưa trả",
                tinhTrangTraHienThi,
                capPhat.getGhiChu() != null ? capPhat.getGhiChu() : ""
            };
            
            tableModel.addRow(rowData);
        }
        
        updateTongSoLabel();
        
        // Log the filter result
        logger.info("Đã lọc " + filteredCapPhat.size() + " lượt cấp phát cho thiết bị: " + 
                   selectedThietBi.getGhiChu());
    }
    
    private void updateTongSoLabel() {
        int totalRows = tableModel.getRowCount();
        int daTra = 0, chuaTra = 0;
        
        // Đếm số lượng đã trả và chưa trả
        for (int i = 0; i < totalRows; i++) {
            String ngayTra = (String) tableModel.getValueAt(i, 6); // Cột ngày trả
            if (ngayTra != null && !ngayTra.trim().isEmpty() && !ngayTra.equals("Chưa trả")) {
                daTra++;
            } else {
                chuaTra++;
            }
        }
        
        lblTongSo.setText(String.format("Tổng: %d | Đã trả: %d | Chưa trả: %d", totalRows, daTra, chuaTra));
    }
    
    // Helper methods
    private YeuCau findYeuCauById(Long id) {
        return yeuCauList.stream()
            .filter(yc -> yc.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private ThietBi findThietBiById(Long id) {
        return thietBiList.stream()
            .filter(tb -> tb.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private NhanVien findNhanVienById(String id) {
        if (id != null) {
            return nhanVienList.stream()
                .filter(nv -> nv.getMaNhanVien().equals(id))
                .findFirst()
                .orElse(null);
        }
        return null;
    }
    
    private PhongBan findPhongBanByMa(String maPhongBan) {
        return phongBanList.stream()
            .filter(pb -> pb.getMaPhongBan().equals(maPhongBan))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Validate business logic: Một thiết bị chỉ có thể có tối đa 1 lượt cấp phát đang hoạt động (chưa trả)
     */
    private void validateBusinessLogic() {
        Map<Long, List<CapPhat>> deviceActiveAllocations = new HashMap<>();
        
        // Group active allocations by device ID
        for (CapPhat capPhat : capPhatList) {
            if (capPhat.getNgayTra() == null) { // Chưa trả
                YeuCau yeuCau = findYeuCauById(capPhat.getYeuCauId());
                if (yeuCau != null) {
                    Long thietBiId = yeuCau.getThietBiId();
                    deviceActiveAllocations.computeIfAbsent(thietBiId, k -> new ArrayList<>()).add(capPhat);
                }
            }
        }
        
        // Check for violations
        List<String> violations = new ArrayList<>();
        for (Map.Entry<Long, List<CapPhat>> entry : deviceActiveAllocations.entrySet()) {
            if (entry.getValue().size() > 1) {
                Long thietBiId = entry.getKey();
                ThietBi thietBi = findThietBiById(thietBiId);
                String thietBiTen = thietBi != null ? thietBi.getGhiChu() : "ID: " + thietBiId;
                
                violations.add("⚠️ CẢNH BÁO: Thiết bị '" + thietBiTen + "' có " + 
                    entry.getValue().size() + " lượt cấp phát đang hoạt động cùng lúc!");
            }
        }
        
        // Show violations if any
        if (!violations.isEmpty()) {
            String message = "❌ PHÁT HIỆN VI PHẠM LOGIC NGHIỆP VỤ:\n\n" +
                String.join("\n", violations) + 
                "\n\n✅ Logic đúng: Mỗi thiết bị chỉ có thể có 1 lượt cấp phát đang hoạt động.";
            
            JOptionPane.showMessageDialog(this, message, "Vi phạm Logic", JOptionPane.WARNING_MESSAGE);
        } else {
            logger.info("✅ Validation passed: Tất cả thiết bị tuân thủ logic nghiệp vụ");
        }
    }
    
    /**
     * Custom renderer for ThietBi ComboBox
     */
    private class ThietBiComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value == null) {
                setText("-- Chọn thiết bị --");
            } else if (value instanceof ThietBi) {
                ThietBi thietBi = (ThietBi) value;
                setText(thietBi.getSoSerial() + " - " + thietBi.getGhiChu());
            }
            
            return this;
        }
    }
    
    /**
     * Custom renderer for NgayTra column to highlight status
     */
    private class NgayTraCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String cellValue = value != null ? value.toString() : "";
            
            // Apply padding
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            
            // Center align for date column
            setHorizontalAlignment(SwingConstants.CENTER);
            
            // Set tooltip
            if (cellValue.length() > 10) {
                setToolTipText("<html><div style='padding: 5px;'>" + cellValue + "</div></html>");
            } else {
                setToolTipText(null);
            }
            
            if (!isSelected) {
                if ("Chưa trả".equals(cellValue)) {
                    setBackground(new Color(255, 235, 235)); // Light red background
                    setForeground(new Color(220, 53, 69)); // Red text
                } else {
                    setBackground(new Color(235, 255, 235)); // Light green background  
                    setForeground(new Color(40, 167, 69)); // Green text
                }
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }
    
    /**
     * Custom renderer for displaying text in table cells with proper formatting
     */
    private class WrapTextCellRenderer extends DefaultTableCellRenderer {
        private final boolean allowWrap;
        
        public WrapTextCellRenderer(boolean allowWrap) {
            this.allowWrap = allowWrap;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String cellValue = value != null ? value.toString() : "";
            
            // Apply padding
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            
            // Set tooltip for long text
            if (cellValue.length() > 20) {
                setToolTipText("<html><div style='width: 400px; padding: 5px;'>" + cellValue + "</div></html>");
            } else {
                setToolTipText(null);
            }
            
            // With horizontal scroll, we can display full text in most cases
            // Only wrap if text is very long and wrap is allowed
            if (allowWrap && cellValue.length() > 50) {
                int columnWidth = table.getColumnModel().getColumn(column).getPreferredWidth();
                int maxWidth = Math.max(180, columnWidth - 25); // Use preferred width
                setText("<html><div style='width: " + maxWidth + "px; line-height: 1.3;'>" + cellValue + "</div></html>");
            } else {
                // Display full text without wrapping for better readability
                setText(cellValue);
            }
            
            // Apply default colors if not selected
            if (!isSelected) {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            
            // Set horizontal alignment for better readability
            if (column == 0) { // ID column
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            return this;
        }
    }

    /**
     * Thêm cấp phát mới vào danh sách và cập nhật UI
     */
    public void addCapPhat(CapPhat capPhat) {
        if (capPhat != null) {
            capPhatList.add(capPhat);
            loadLichSuData(); // Refresh UI
            logger.info("Đã thêm cấp phát mới: ID={}", capPhat.getId());
        }
    }

    /**
     * Cập nhật dữ liệu từ AdminPanel
     */
    public void updateDataFromAdminPanel(List<CapPhat> capPhatList, List<YeuCau> yeuCauList, 
                                       List<ThietBi> thietBiList, List<NhanVien> nhanVienList, 
                                       List<PhongBan> phongBanList) {
        if (capPhatList != null) this.capPhatList = capPhatList;
        if (yeuCauList != null) this.yeuCauList = yeuCauList;
        if (thietBiList != null) this.thietBiList = thietBiList;
        if (nhanVienList != null) this.nhanVienList = nhanVienList;
        if (phongBanList != null) this.phongBanList = phongBanList;
        
        loadLichSuData(); // Refresh UI
        logger.info("Đã cập nhật dữ liệu từ AdminPanel");
    }
    
    /**
     * Refresh data from database
     */
    public void refreshData() {
        loadDataFromDatabase();
        loadLichSuData();
        loadThietBiComboBox();
        logger.info("Đã refresh dữ liệu từ database");
    }
}
