package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.ThietBi.TrangThaiThietBi;
import com.mycompany.device.model.LoaiThietBi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel quản lý thiết bị
 * @author Kim Ngan - UI Layer
 */
public class ThietBiPanel extends JPanel {
    
    // Table components
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Form components
    private JTextField txtId;
    private JTextField txtSoSerial;
    private JComboBox<LoaiThietBi> cboLoaiThietBi;
    private JComboBox<TrangThaiThietBi> cboTrangThai;
    private JTextField txtNgayMua;
    private JTextField txtGiaMua;
    private JTextArea txtGhiChu;
    
    // Button components
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    
    // Search components
    private JTextField txtTimKiem;
    private JComboBox<String> cboTimKiem;
    private JButton btnTimKiem;
    private JButton btnLamSach;
    
    // Data
    private List<ThietBi> thietBiList;
    private List<LoaiThietBi> loaiThietBiList;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ThietBiPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        initializeMockData();
        loadTableData();
    }
    
    private void initializeComponents() {
        // Initialize table
        String[] columnNames = {"Số Serial", "Loại thiết bị", "Trạng thái", "Ngày mua", "Giá mua", "Ghi chú", "Ngày tạo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Configure table appearance with padding
        setupTableAppearance();
        
        scrollPane = new JScrollPane(table);
        
        // Initialize form components
        txtId = new JTextField();
        txtSoSerial = new JTextField();
        initializeLoaiThietBiData();
        cboLoaiThietBi = new JComboBox<>();
        populateLoaiThietBiComboBox();
        cboTrangThai = new JComboBox<>(TrangThaiThietBi.values());
        txtNgayMua = new JTextField();
        txtGiaMua = new JTextField();
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        
        // Set read-only for ID
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        
        // Set preferred sizes for better display
        Dimension textFieldSize = new Dimension(200, 25);
        txtId.setPreferredSize(textFieldSize);
        txtSoSerial.setPreferredSize(textFieldSize);
        cboLoaiThietBi.setPreferredSize(textFieldSize);
        cboTrangThai.setPreferredSize(textFieldSize);
        txtNgayMua.setPreferredSize(textFieldSize);
        txtGiaMua.setPreferredSize(textFieldSize);
        
        // Initialize buttons
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        
        // Style buttons
        styleButton(btnThem);
        styleButton(btnSua);
        styleButton(btnXoa);
        styleButton(btnLamMoi);
        
        // Initialize search components
        txtTimKiem = new JTextField(20);
        cboTimKiem = new JComboBox<>(new String[]{
            "Tất cả", "Số Serial", "Loại ID", "Trạng thái", "Ghi chú"
        });
        btnTimKiem = new JButton("Tìm kiếm");
        btnLamSach = new JButton("Làm sạch");
        
        styleButton(btnTimKiem);
        styleButton(btnLamSach);
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
        thietBiList = new ArrayList<>();
        
        // Sample data
        thietBiList.add(new ThietBi(1L, "TB001", 1L, TrangThaiThietBi.TON_KHO,
                LocalDate.of(2024, 1, 15), new BigDecimal("15000000"), "Laptop Dell Inspiron",
                LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(30)));
        
        thietBiList.add(new ThietBi(2L, "TB002", 2L, TrangThaiThietBi.DANG_CAP_PHAT,
                LocalDate.of(2024, 2, 20), new BigDecimal("8000000"), "Màn hình LG 24 inch",
                LocalDateTime.now().minusDays(25), LocalDateTime.now().minusDays(20)));
        
        thietBiList.add(new ThietBi(3L, "TB003", 1L, TrangThaiThietBi.DANG_BAO_TRI,
                LocalDate.of(2023, 12, 10), new BigDecimal("12000000"), "Laptop HP Pavilion - cần thay pin",
                LocalDateTime.now().minusDays(45), LocalDateTime.now().minusDays(5)));
        
        thietBiList.add(new ThietBi(4L, "TB004", 3L, TrangThaiThietBi.HU_HONG,
                LocalDate.of(2023, 8, 5), new BigDecimal("3000000"), "Bàn phím cơ - bị đổ nước",
                LocalDateTime.now().minusDays(60), LocalDateTime.now().minusDays(10)));
        
        thietBiList.add(new ThietBi(5L, "TB005", 4L, TrangThaiThietBi.TON_KHO,
                LocalDate.of(2024, 3, 1), new BigDecimal("5000000"), "Chuột gaming",
                LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(15)));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panels
        JPanel topPanel = createSearchPanel();
        JPanel centerPanel = createTablePanel();
        JPanel rightPanel = createFormPanel();
        JPanel bottomPanel = createButtonPanel();
        
        // Layout
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        panel.setBorder(new TitledBorder("Danh sách thiết bị"));
        
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Thông tin thiết bị"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(txtId, gbc);
        
        // Số Serial
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Số Serial:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(txtSoSerial, gbc);
        
        // Loại thiết bị
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Loại thiết bị:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(cboLoaiThietBi, gbc);
        
        // Trạng thái
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(cboTrangThai, gbc);
        
        // Ngày mua
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Ngày mua (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(txtNgayMua, gbc);
        
        // Giá mua
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Giá mua (VND):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(txtGiaMua, gbc);
        
        // Ghi chú
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(txtGhiChu), gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
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
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    loadFormFromTable(selectedRow);
                }
            }
        });
        
        // Button listeners
        btnThem.addActionListener(this::handleThem);
        btnSua.addActionListener(this::handleSua);
        btnXoa.addActionListener(this::handleXoa);
        btnLamMoi.addActionListener(this::handleLamMoi);
        btnTimKiem.addActionListener(this::handleTimKiem);
        btnLamSach.addActionListener(this::handleLamSach);
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        
        for (ThietBi tb : thietBiList) {
            // Tìm tên loại thiết bị từ ID
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
                tenLoai,  // Hiển thị tên loại thay vì ID
                tb.getTrangThai().getDisplayName(),
                tb.getNgayMua() != null ? tb.getNgayMua().format(DATE_FORMATTER) : "",
                tb.getGiaMua() != null ? tb.getGiaMua().toString() : "",
                tb.getGhiChu() != null ? tb.getGhiChu() : "",
                tb.getCreatedAt() != null ? tb.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : ""
            };
            tableModel.addRow(row);
        }
        
        // Cập nhật row heights sau khi load data để hỗ trợ wrap text
        updateRowHeights();
    }
    
    /**
     * Cập nhật chiều cao các row để phù hợp với nội dung wrap text
     */
    private void updateRowHeights() {
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxHeight = 35; // Chiều cao tối thiểu
            
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object value = table.getValueAt(row, col);
                if (value != null) {
                    JTextArea textArea = new JTextArea();
                    textArea.setText(value.toString());
                    textArea.setFont(table.getFont());
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    
                    // Lấy width của cột
                    int colWidth = table.getColumnModel().getColumn(col).getWidth();
                    if (colWidth > 0) {
                        textArea.setSize(colWidth - 20, Short.MAX_VALUE); // Trừ padding
                        int preferredHeight = textArea.getPreferredSize().height + 10; // Thêm padding
                        maxHeight = Math.max(maxHeight, preferredHeight);
                    }
                }
            }
            
            table.setRowHeight(row, maxHeight);
        }
    }
    
    private void loadFormFromTable(int row) {
        if (row >= 0 && row < thietBiList.size()) {
            ThietBi tb = thietBiList.get(row);
            
            txtId.setText(tb.getId() != null ? tb.getId().toString() : "");
            txtSoSerial.setText(tb.getSoSerial() != null ? tb.getSoSerial() : "");
            
            // Set selected loai thiet bi
            LoaiThietBi selectedLoai = null;
            if (tb.getLoaiId() != null) {
                for (LoaiThietBi loai : loaiThietBiList) {
                    if (loai.getId().equals(tb.getLoaiId())) {
                        selectedLoai = loai;
                        break;
                    }
                }
            }
            cboLoaiThietBi.setSelectedItem(selectedLoai);
            
            cboTrangThai.setSelectedItem(tb.getTrangThai());
            txtNgayMua.setText(tb.getNgayMua() != null ? tb.getNgayMua().format(DATE_FORMATTER) : "");
            txtGiaMua.setText(tb.getGiaMua() != null ? tb.getGiaMua().toString() : "");
            txtGhiChu.setText(tb.getGhiChu() != null ? tb.getGhiChu() : "");
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtSoSerial.setText("");
        cboLoaiThietBi.setSelectedIndex(0);  // Select default "Chọn loại thiết bị"
        cboTrangThai.setSelectedIndex(0);
        txtNgayMua.setText("");
        txtGiaMua.setText("");
        txtGhiChu.setText("");
    }
    
    private ThietBi createThietBiFromForm() {
        try {
            String soSerial = txtSoSerial.getText().trim();
            if (soSerial.isEmpty()) {
                throw new IllegalArgumentException("Số Serial không được để trống");
            }
            
            LoaiThietBi selectedLoai = (LoaiThietBi) cboLoaiThietBi.getSelectedItem();
            if (selectedLoai == null) {
                throw new IllegalArgumentException("Vui lòng chọn loại thiết bị");
            }
            Long loaiId = selectedLoai.getId();
            
            TrangThaiThietBi trangThai = (TrangThaiThietBi) cboTrangThai.getSelectedItem();
            
            LocalDate ngayMua = null;
            String ngayMuaStr = txtNgayMua.getText().trim();
            if (!ngayMuaStr.isEmpty()) {
                ngayMua = LocalDate.parse(ngayMuaStr, DATE_FORMATTER);
            }
            
            BigDecimal giaMua = null;
            String giaMuaStr = txtGiaMua.getText().trim();
            if (!giaMuaStr.isEmpty()) {
                giaMua = new BigDecimal(giaMuaStr);
            }
            
            String ghiChu = txtGhiChu.getText().trim();
            if (ghiChu.isEmpty()) {
                ghiChu = null;
            }
            
            return new ThietBi(soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu nhập không hợp lệ: " + e.getMessage());
        }
    }
    
    private void handleThem(ActionEvent e) {
        try {
            ThietBi newThietBi = createThietBiFromForm();
            
            // Generate new ID
            Long maxId = thietBiList.stream()
                    .mapToLong(tb -> tb.getId() != null ? tb.getId() : 0L)
                    .max()
                    .orElse(0L);
            newThietBi.setId(maxId + 1);
            
            thietBiList.add(newThietBi);
            loadTableData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Thêm thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleSua(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            ThietBi updatedThietBi = createThietBiFromForm();
            ThietBi existingThietBi = thietBiList.get(selectedRow);
            
            // Keep the original ID and timestamps
            updatedThietBi.setId(existingThietBi.getId());
            updatedThietBi.setCreatedAt(existingThietBi.getCreatedAt());
            updatedThietBi.setUpdatedAt(LocalDateTime.now());
            
            thietBiList.set(selectedRow, updatedThietBi);
            loadTableData();
            
            JOptionPane.showMessageDialog(this, "Cập nhật thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleXoa(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ThietBi thietBi = thietBiList.get(selectedRow);
        int result = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa thiết bị: " + thietBi.getSoSerial() + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            thietBiList.remove(selectedRow);
            loadTableData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Xóa thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleLamMoi(ActionEvent e) {
        clearForm();
        table.clearSelection();
        loadTableData();
    }
    
    private void handleTimKiem(ActionEvent e) {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String searchType = (String) cboTimKiem.getSelectedItem();
        
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        
        List<ThietBi> filteredList = new ArrayList<>();
        
        for (ThietBi tb : thietBiList) {
            boolean matches = false;
            
            switch (searchType) {
                case "Số Serial":
                    matches = tb.getSoSerial() != null && tb.getSoSerial().toLowerCase().contains(keyword);
                    break;
                case "Loại ID":
                    matches = tb.getLoaiId() != null && tb.getLoaiId().toString().contains(keyword);
                    break;
                case "Trạng thái":
                    matches = tb.getTrangThai().getDisplayName().toLowerCase().contains(keyword);
                    break;
                case "Ghi chú":
                    matches = tb.getGhiChu() != null && tb.getGhiChu().toLowerCase().contains(keyword);
                    break;
                default: // "Tất cả"
                    matches = (tb.getSoSerial() != null && tb.getSoSerial().toLowerCase().contains(keyword)) ||
                             (tb.getLoaiId() != null && tb.getLoaiId().toString().contains(keyword)) ||
                             (tb.getTrangThai().getDisplayName().toLowerCase().contains(keyword)) ||
                             (tb.getGhiChu() != null && tb.getGhiChu().toLowerCase().contains(keyword));
                    break;
            }
            
            if (matches) {
                filteredList.add(tb);
            }
        }
        
        // Temporarily replace the list for display
        List<ThietBi> originalList = new ArrayList<>(thietBiList);
        thietBiList = filteredList;
        loadTableData();
        thietBiList = originalList;
    }
    
    private void handleLamSach(ActionEvent e) {
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
        loadTableData();
    }
    
    /**
     * Khởi tạo dữ liệu mẫu cho loại thiết bị
     */
    private void initializeLoaiThietBiData() {
        loaiThietBiList = new ArrayList<>();
        
        // Thêm các loại thiết bị mẫu
        loaiThietBiList.add(new LoaiThietBi(1L, "LT001", "Máy tính để bàn", "Máy tính cá nhân dành cho văn phòng"));
        loaiThietBiList.add(new LoaiThietBi(2L, "LT002", "Laptop", "Máy tính xách tay"));
        loaiThietBiList.add(new LoaiThietBi(3L, "LT003", "Máy in", "Thiết bị in ấn văn phòng"));
        loaiThietBiList.add(new LoaiThietBi(4L, "LT004", "Máy chiếu", "Thiết bị trình chiếu"));
        loaiThietBiList.add(new LoaiThietBi(5L, "LT005", "Màn hình", "Màn hình máy tính"));
        loaiThietBiList.add(new LoaiThietBi(6L, "LT006", "Bàn phím", "Bàn phím máy tính"));
        loaiThietBiList.add(new LoaiThietBi(7L, "LT007", "Chuột", "Chuột máy tính"));
        loaiThietBiList.add(new LoaiThietBi(8L, "LT008", "Switch mạng", "Thiết bị chuyển mạch"));
    }
    
    /**
     * Thiết lập giao diện bảng với padding và styling
     */
    private void setupTableAppearance() {
        // Tăng chiều cao hàng để có padding (sẽ được tự động điều chỉnh với wrap text)
        table.setRowHeight(35);
        
        // Thiết lập font và màu sắc
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(184, 207, 229));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        
        // Thiết lập header
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        // Thêm border cho cells
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Custom cell renderer để thêm padding và wrap text
        javax.swing.table.TableCellRenderer cellRenderer = new javax.swing.table.TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JTextArea textArea = new JTextArea();
                textArea.setText(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(true);
                
                // Font và padding
                textArea.setFont(table.getFont());
                textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Màu sắc
                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    // Màu xen kẽ cho các hàng
                    if (row % 2 == 0) {
                        textArea.setBackground(Color.WHITE);
                    } else {
                        textArea.setBackground(new Color(248, 249, 250));
                    }
                    textArea.setForeground(table.getForeground());
                }
                
                // Điều chỉnh size cho phù hợp với cột
                int colWidth = table.getColumnModel().getColumn(column).getWidth();
                textArea.setSize(colWidth - 20, table.getRowHeight(row) - 10); // Trừ padding
                
                return textArea;
            }
        };
        
        // Áp dụng renderer cho tất cả các cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Thêm listener để cập nhật row heights khi resize cột
        table.getColumnModel().addColumnModelListener(new javax.swing.event.TableColumnModelListener() {
            @Override
            public void columnAdded(javax.swing.event.TableColumnModelEvent e) {}
            
            @Override
            public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {}
            
            @Override
            public void columnMoved(javax.swing.event.TableColumnModelEvent e) {}
            
            @Override
            public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
                // Cập nhật row heights khi thay đổi kích thước cột
                SwingUtilities.invokeLater(() -> updateRowHeights());
            }
            
            @Override
            public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {}
        });
        
        // Thiết lập độ rộng cột
        if (table.getColumnCount() >= 7) {
            table.getColumnModel().getColumn(0).setPreferredWidth(120); // Số Serial
            table.getColumnModel().getColumn(1).setPreferredWidth(150); // Loại thiết bị
            table.getColumnModel().getColumn(2).setPreferredWidth(100); // Trạng thái
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Ngày mua
            table.getColumnModel().getColumn(4).setPreferredWidth(120); // Giá mua
            table.getColumnModel().getColumn(5).setPreferredWidth(200); // Ghi chú
            table.getColumnModel().getColumn(6).setPreferredWidth(150); // Ngày tạo
        }
    }
    
    /**
     * Tải dữ liệu vào ComboBox loại thiết bị
     */
    private void populateLoaiThietBiComboBox() {
        cboLoaiThietBi.removeAllItems();
        
        // Thêm option mặc định
        cboLoaiThietBi.addItem(null);
        
        // Thêm các loại thiết bị
        for (LoaiThietBi loai : loaiThietBiList) {
            cboLoaiThietBi.addItem(loai);
        }
        
        // Custom renderer để hiển thị tên loại thay vì toString()
        cboLoaiThietBi.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value == null) {
                    setText("-- Chọn loại thiết bị --");
                } else if (value instanceof LoaiThietBi) {
                    LoaiThietBi loai = (LoaiThietBi) value;
                    setText(loai.getTenLoai() + " (" + loai.getMaLoai() + ")");
                }
                
                return this;
            }
        });
    }
}
