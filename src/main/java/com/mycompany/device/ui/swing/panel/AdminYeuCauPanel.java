package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.YeuCau.TrangThaiYeuCau;
import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.util.LogoUtil;

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

/**
 * Panel dành cho admin để xem và phê duyệt các yêu cầu
 * @author Kim Ngan - UI Layer
 */
public class AdminYeuCauPanel extends JPanel {
    
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
    
    // Constants
    private static final String[] COLUMN_NAMES = {
        "ID", "Thiết bị", "Người yêu cầu", "Phòng ban", "Lý do", 
        "Trạng thái", "Ngày tạo", "Thao tác"
    };
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public AdminYeuCauPanel() {
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeData() {
        yeuCauList = new ArrayList<>();
        thietBiList = new ArrayList<>();
        nhanVienList = new ArrayList<>();
        phongBanList = new ArrayList<>();
        
        // Mock data for testing
        initializeMockData();
    }
    
    private void initializeMockData() {
        // Mock PhongBan data
        phongBanList.add(new PhongBan("1", "Phòng IT", "Phòng Công nghệ thông tin"));
        phongBanList.add(new PhongBan("2", "Phòng Kế toán", "Phòng Kế toán và Tài chính"));
        phongBanList.add(new PhongBan("3", "Phòng Nhân sự", "Phòng Quản lý Nhân sự"));
        
        // Mock ThietBi data
        thietBiList.add(new ThietBi(1L, "TB001", 1L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "Laptop Dell Inspiron 15", null, null));
        thietBiList.add(new ThietBi(2L, "TB002", 2L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "Mouse Logitech MX Master", null, null));
        thietBiList.add(new ThietBi(3L, "TB003", 1L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "MacBook Pro 13 inch", null, null));
        
        // Mock NhanVien data
        nhanVienList.add(new NhanVien("NV001", "Nguyễn Văn A", "nva@company.com", 
                "password", "0901234567", NhanVien.NhanVienRole.ADMIN, "1"));
        nhanVienList.add(new NhanVien("NV002", "Trần Thị B", "ttb@company.com", 
                "password", "0901234568", NhanVien.NhanVienRole.STAFF, "2"));
        nhanVienList.add(new NhanVien("NV003", "Lê Văn C", "lvc@company.com", 
                "password", "0901234569", NhanVien.NhanVienRole.STAFF, "1"));
        
        // Mock YeuCau data - Chỉ có trạng thái CHO_DUYET để admin có thể duyệt/từ chối
        yeuCauList.add(new YeuCau(1L, 1L, "NV002", TrangThaiYeuCau.CHO_DUYET, 
                "Cần laptop để làm dự án mới", 
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2)));
        yeuCauList.add(new YeuCau(2L, 2L, "NV003", TrangThaiYeuCau.CHO_DUYET, 
                "Mouse cũ bị hỏng, cần thay thế", 
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1)));
        yeuCauList.add(new YeuCau(3L, 3L, "NV002", TrangThaiYeuCau.DA_DUYET, 
                "Cần MacBook cho công việc design", 
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1)));
        yeuCauList.add(new YeuCau(4L, 1L, "NV003", TrangThaiYeuCau.TU_CHOI, 
                "Yêu cầu laptop thứ 2", 
                LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(2)));
        yeuCauList.add(new YeuCau(5L, 2L, "NV002", TrangThaiYeuCau.CHO_DUYET, 
                "Mouse cho nhân viên mới", 
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5)));
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
    
    private void approveRequest(int row) {
        if (row >= 0 && row < filteredYeuCauList.size()) {
            YeuCau yeuCau = filteredYeuCauList.get(row);
            
            if (yeuCau.getTrangThai() == TrangThaiYeuCau.CHO_DUYET) {
                // Update status
                yeuCau.setTrangThai(TrangThaiYeuCau.DA_DUYET);
                yeuCau.setNgayCapNhat(LocalDateTime.now());
                
                // Create CapPhat record (would be saved to database in real implementation)
                @SuppressWarnings("unused")
                CapPhat capPhat = new CapPhat(yeuCau.getId());
                
                // Show success message
                LogoUtil.showMessageDialog(this, 
                    "Yêu cầu đã được phê duyệt thành công!\nID Yêu cầu: " + yeuCau.getId(),
                    "Phê duyệt thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                updateTable();
                updateStatistics();
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
}
