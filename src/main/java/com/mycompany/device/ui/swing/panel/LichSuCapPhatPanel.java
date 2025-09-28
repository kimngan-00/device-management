package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.YeuCau;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.model.PhongBan;
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
    
    public LichSuCapPhatPanel() {
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadLichSuData();
        
        logger.info("LichSuCapPhatPanel đã được khởi tạo thành công");
    }
    
    private void initializeData() {
        capPhatList = new ArrayList<>();
        thietBiList = new ArrayList<>();
        yeuCauList = new ArrayList<>();
        nhanVienList = new ArrayList<>();
        phongBanList = new ArrayList<>();
        
        // Mock data
        initializeMockData();
    }
    
    private void initializeMockData() {
        // Mock PhongBan data
        phongBanList.add(new PhongBan("PB001", "Phòng IT", "Phòng Công nghệ thông tin"));
        phongBanList.add(new PhongBan("PB002", "Phòng Kế toán", "Phòng Kế toán và Tài chính"));
        phongBanList.add(new PhongBan("PB003", "Phòng Nhân sự", "Phòng Quản lý Nhân sự"));
        phongBanList.add(new PhongBan("PB004", "Phòng Marketing", "Phòng Marketing và Truyền thông"));
        phongBanList.add(new PhongBan("PB005", "Phòng R&D", "Phòng Nghiên cứu và Phát triển"));
        phongBanList.add(new PhongBan("PB006", "Phòng Kinh doanh", "Phòng Kinh doanh và Bán hàng"));
        
        // Mock NhanVien data
        nhanVienList.add(new NhanVien("NV001", "Nguyễn Văn An", "an.nguyen@company.com", 
                "password", "0901234567", NhanVien.NhanVienRole.STAFF, "PB001"));
        nhanVienList.add(new NhanVien("NV002", "Trần Thị Bình", "binh.tran@company.com", 
                "password", "0901234568", NhanVien.NhanVienRole.STAFF, "PB002"));
        nhanVienList.add(new NhanVien("NV003", "Lê Văn Cường", "cuong.le@company.com", 
                "password", "0901234569", NhanVien.NhanVienRole.STAFF, "PB001"));
        nhanVienList.add(new NhanVien("NV004", "Phạm Thị Dung", "dung.pham@company.com", 
                "password", "0901234570", NhanVien.NhanVienRole.STAFF, "PB003"));
        nhanVienList.add(new NhanVien("NV005", "Hoàng Văn Em", "em.hoang@company.com", 
                "password", "0901234571", NhanVien.NhanVienRole.STAFF, "PB004"));
        nhanVienList.add(new NhanVien("NV006", "Võ Thị Phương", "phuong.vo@company.com", 
                "password", "0901234572", NhanVien.NhanVienRole.STAFF, "PB005"));
        nhanVienList.add(new NhanVien("NV007", "Đặng Minh Quân", "quan.dang@company.com", 
                "password", "0901234573", NhanVien.NhanVienRole.STAFF, "PB006"));
        nhanVienList.add(new NhanVien("NV008", "Ngô Thị Thu", "thu.ngo@company.com", 
                "password", "0901234574", NhanVien.NhanVienRole.STAFF, "PB002"));
        nhanVienList.add(new NhanVien("NV009", "Bùi Văn Sơn", "son.bui@company.com", 
                "password", "0901234575", NhanVien.NhanVienRole.STAFF, "PB003"));
        nhanVienList.add(new NhanVien("NV010", "Lý Thị Mai", "mai.ly@company.com", 
                "password", "0901234576", NhanVien.NhanVienRole.STAFF, "PB001"));
        
        // Mock ThietBi data
        thietBiList.add(new ThietBi(1L, "TB001", 1L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "Laptop Dell Inspiron 15 3000", null, null));
        thietBiList.add(new ThietBi(2L, "TB002", 2L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "Mouse Logitech MX Master 3", null, null));
        thietBiList.add(new ThietBi(3L, "TB003", 1L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "MacBook Pro 13 inch M1", null, null));
        thietBiList.add(new ThietBi(4L, "TB004", 3L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "iPhone 13 Pro Max", null, null));
        thietBiList.add(new ThietBi(5L, "TB005", 2L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "Samsung Galaxy Tab S8", null, null));
        thietBiList.add(new ThietBi(6L, "TB006", 1L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "HP EliteBook 840 G8", null, null));
        thietBiList.add(new ThietBi(7L, "TB007", 4L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "Canon EOS R5 Camera", null, null));
        thietBiList.add(new ThietBi(8L, "TB008", 2L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "Keyboard Mechanical RGB", null, null));
        thietBiList.add(new ThietBi(9L, "TB009", 3L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "iPad Pro 12.9 inch M2", null, null));
        thietBiList.add(new ThietBi(10L, "TB010", 1L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "ASUS ROG Gaming Laptop", null, null));
        thietBiList.add(new ThietBi(11L, "TB011", 2L, ThietBi.TrangThaiThietBi.DANG_CAP_PHAT, 
                null, null, "Monitor Dell 27 inch 4K", null, null));
        thietBiList.add(new ThietBi(12L, "TB012", 5L, ThietBi.TrangThaiThietBi.TON_KHO, 
                null, null, "Projector Epson EB-2250U", null, null));
        
        // Mock YeuCau data
        yeuCauList.add(new YeuCau(1L, 1L, "NV001", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Cần laptop cho công việc lập trình", 
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(8)));
        yeuCauList.add(new YeuCau(2L, 2L, "NV002", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Mouse cũ bị hỏng, cần thay thế", 
                LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(12)));
        yeuCauList.add(new YeuCau(3L, 3L, "NV003", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Cần MacBook cho công việc design", 
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(18)));
        yeuCauList.add(new YeuCau(4L, 4L, "NV004", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "iPhone để test ứng dụng mobile", 
                LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(5)));
        yeuCauList.add(new YeuCau(5L, 5L, "NV005", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Tablet cho presentation", 
                LocalDateTime.now().minusDays(12), LocalDateTime.now().minusDays(10)));
        yeuCauList.add(new YeuCau(6L, 6L, "NV006", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Laptop cho nhân viên mới", 
                LocalDateTime.now().minusDays(25), LocalDateTime.now().minusDays(23)));
        yeuCauList.add(new YeuCau(7L, 7L, "NV007", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Camera để chụp sản phẩm", 
                LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(28)));
        yeuCauList.add(new YeuCau(8L, 1L, "NV008", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Laptop Dell thứ 2 cho team", 
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3)));
        yeuCauList.add(new YeuCau(9L, 9L, "NV009", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "iPad cho manager", 
                LocalDateTime.now().minusDays(18), LocalDateTime.now().minusDays(16)));
        yeuCauList.add(new YeuCau(10L, 11L, "NV010", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Monitor để làm việc", 
                LocalDateTime.now().minusDays(14), LocalDateTime.now().minusDays(12)));
        yeuCauList.add(new YeuCau(11L, 3L, "NV001", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "MacBook thứ 2 cho dự án", 
                LocalDateTime.now().minusDays(8), LocalDateTime.now().minusDays(6)));
        yeuCauList.add(new YeuCau(12L, 2L, "NV008", YeuCau.TrangThaiYeuCau.DA_CAP_PHAT, 
                "Mouse backup", 
                LocalDateTime.now().minusDays(22), LocalDateTime.now().minusDays(20)));
        
        // Mock CapPhat data - Tuân thủ logic: 1 thiết bị chỉ có tối đa 1 lượt cấp phát chưa trả
        
        // Thiết bị 1 (Laptop Dell) - Đã trả gần đây
        capPhatList.add(new CapPhat(1L, 1L, LocalDateTime.now().minusDays(8), 
                LocalDateTime.now().minusDays(2), CapPhat.TinhTrangTra.TOT, "Hoạt động tốt"));
        
        // Thiết bị 2 (Mouse Logitech) - Đang được sử dụng
        capPhatList.add(new CapPhat(2L, 2L, LocalDateTime.now().minusDays(12), 
                null, null, null));
        
        // Thiết bị 3 (MacBook Pro) - Đang được sử dụng 
        capPhatList.add(new CapPhat(3L, 3L, LocalDateTime.now().minusDays(18), 
                null, null, null));
        
        // Thiết bị 4 (iPhone 13) - Đã trả
        capPhatList.add(new CapPhat(4L, 4L, LocalDateTime.now().minusDays(5), 
                LocalDateTime.now().minusDays(1), CapPhat.TinhTrangTra.TRAY_XUOC, "Có một vài trầy xước nhỏ"));
        
        // Thiết bị 5 (iPad Pro) - Đã trả
        capPhatList.add(new CapPhat(5L, 5L, LocalDateTime.now().minusDays(10), 
                LocalDateTime.now().minusDays(3), CapPhat.TinhTrangTra.TOT, "Trả đúng hạn"));
        
        // Thiết bị 6 (HP EliteBook) - Đã trả
        capPhatList.add(new CapPhat(6L, 6L, LocalDateTime.now().minusDays(23), 
                LocalDateTime.now().minusDays(15), CapPhat.TinhTrangTra.TOT, "Trả đúng hạn"));
        
        // Thiết bị 7 (Canon Camera) - Đang được sử dụng
        capPhatList.add(new CapPhat(7L, 7L, LocalDateTime.now().minusDays(28), 
                null, null, null));
        
        // Thiết bị 8 (Laptop Dell thứ 2) - Đã trả
        capPhatList.add(new CapPhat(8L, 8L, LocalDateTime.now().minusDays(3), 
                LocalDateTime.now().minusDays(1), CapPhat.TinhTrangTra.TOT, "Trả đúng hạn"));
        
        // Thiết bị 9 (iPad Air) - Đã trả
        capPhatList.add(new CapPhat(9L, 9L, LocalDateTime.now().minusDays(16), 
                LocalDateTime.now().minusDays(8), CapPhat.TinhTrangTra.TOT, "Tình trạng tốt"));
        
        // Thiết bị 10 (ASUS ROG) - Đang được sử dụng
        capPhatList.add(new CapPhat(10L, 10L, LocalDateTime.now().minusDays(12), 
                null, null, null));
        
        // Thiết bị 11 (Monitor Dell) - Đã trả
        capPhatList.add(new CapPhat(11L, 11L, LocalDateTime.now().minusDays(6), 
                LocalDateTime.now().minusDays(2), CapPhat.TinhTrangTra.TOT, "Hoạt động bình thường"));
        
        // Thiết bị 12 (Projector Epson) - Đã trả
        capPhatList.add(new CapPhat(12L, 12L, LocalDateTime.now().minusDays(20), 
                LocalDateTime.now().minusDays(12), CapPhat.TinhTrangTra.TRAY_XUOC, "Một vài vết xước nhỏ"));
        
        // Thêm các lượt cấp phát cũ cho cùng thiết bị (tất cả đã trả để không vi phạm logic)
        capPhatList.add(new CapPhat(13L, 1L, LocalDateTime.now().minusDays(35), 
                LocalDateTime.now().minusDays(25), CapPhat.TinhTrangTra.TOT, "Lần cấp phát trước"));
        capPhatList.add(new CapPhat(14L, 3L, LocalDateTime.now().minusDays(45), 
                LocalDateTime.now().minusDays(30), CapPhat.TinhTrangTra.HU_HONG, "Pin hỏng"));
        capPhatList.add(new CapPhat(15L, 2L, LocalDateTime.now().minusDays(50), 
                LocalDateTime.now().minusDays(40), CapPhat.TinhTrangTra.TOT, "Hoạt động bình thường"));
        
        // Thêm một vài lượt cấp phát khác cho thiết bị 1 (đã trả)
        capPhatList.add(new CapPhat(16L, 1L, LocalDateTime.now().minusDays(60), 
                LocalDateTime.now().minusDays(50), CapPhat.TinhTrangTra.TOT, "Lần cấp phát cũ hơn"));
        capPhatList.add(new CapPhat(17L, 4L, LocalDateTime.now().minusDays(25), 
                LocalDateTime.now().minusDays(20), CapPhat.TinhTrangTra.TOT, "Sử dụng tốt"));
        capPhatList.add(new CapPhat(18L, 5L, LocalDateTime.now().minusDays(40), 
                LocalDateTime.now().minusDays(35), CapPhat.TinhTrangTra.TOT, "Hoạt động ổn định"));
        
        // Validate business logic after loading mock data
        validateBusinessLogic();
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
        // For mock data, we use position in list as ID
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
}
