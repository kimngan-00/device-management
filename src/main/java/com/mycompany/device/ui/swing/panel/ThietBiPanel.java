package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.controller.ThietBiController;
import com.mycompany.device.dao.LoaiThietBiDAO;
import com.mycompany.device.dao.impl.LoaiThietBiDAOMySQLImpl;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.util.LogoUtil;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Panel quản lý thiết bị
 * Đã kết nối với ThietBiController để thao tác với Database.
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
    
    // Data & Controller
    private List<ThietBi> thietBiList;
    private List<LoaiThietBi> loaiThietBiList;
    private final ThietBiController thietBiController; // Controller mới
    private final LoaiThietBiDAO loaiThietBiDAO; // DAO cho Loại thiết bị
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ThietBiPanel() {
        // Khởi tạo Controller và DAO
        this.thietBiController = new ThietBiController();
        // Giả định LoaiThietBiDAOMySQLImpl đã tồn tại và triển khai LoaiThietBiDAO
        this.loaiThietBiDAO = new LoaiThietBiDAOMySQLImpl(); 
        this.thietBiList = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        loadLoaiThietBiData(); // Tải dữ liệu Loại TB từ DB
        loadTableData(thietBiController.getAllThietBi()); // Tải dữ liệu TB từ DB
        clearForm();
    }
    
    private void initializeComponents() {
        // Initialize table
        String[] columnNames = {"ID", "Số Serial", "Loại thiết bị", "Trạng thái", "Ngày mua", "Giá mua", "Ghi chú", "Ngày tạo"};
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
        // Không cần initializeLoaiThietBiData() ở đây nữa
        cboLoaiThietBi = new JComboBox<>();
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

    // *** REMOVED initializeMockData() ***
    
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
                if (selectedRow >= 0 && selectedRow < thietBiList.size()) {
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
    
    /**
     * Tải dữ liệu thiết bị vào JTable.
     * @param dataList Danh sách thiết bị cần hiển thị.
     */
    private void loadTableData(List<ThietBi> dataList) {
        tableModel.setRowCount(0);
        this.thietBiList = dataList; // Cập nhật danh sách hiện tại
        
        for (ThietBi tb : thietBiList) {
            // Tìm tên loại thiết bị từ ID
            String tenLoai = loaiThietBiList.stream()
                                .filter(loai -> tb.getLoaiId() != null && loai.getId().equals(tb.getLoaiId()))
                                .findFirst()
                                .map(LoaiThietBi::getTenLoai)
                                .orElse("Không xác định"); 
            
            Object[] row = {
                tb.getId(), // Thêm ID vào cột đầu tiên (Ẩn nếu muốn, nhưng tiện cho việc tìm kiếm)
                tb.getSoSerial(),
                tenLoai, 
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
        // Code cập nhật chiều cao hàng không thay đổi.
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
            Optional<LoaiThietBi> selectedLoai = loaiThietBiList.stream()
                .filter(loai -> tb.getLoaiId() != null && loai.getId().equals(tb.getLoaiId()))
                .findFirst();
            cboLoaiThietBi.setSelectedItem(selectedLoai.orElse(null));
            
            cboTrangThai.setSelectedItem(tb.getTrangThai());
            txtNgayMua.setText(tb.getNgayMua() != null ? tb.getNgayMua().format(DATE_FORMATTER) : "");
            txtGiaMua.setText(tb.getGiaMua() != null ? tb.getGiaMua().toString() : "");
            txtGhiChu.setText(tb.getGhiChu() != null ? tb.getGhiChu() : "");
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtSoSerial.setText("");
        // Đặt lại ComboBox về option mặc định (null)
        if (cboLoaiThietBi.getItemCount() > 0) {
            cboLoaiThietBi.setSelectedIndex(0); 
        } 
        cboTrangThai.setSelectedIndex(0);
        txtNgayMua.setText("");
        txtGiaMua.setText("");
        txtGhiChu.setText("");
    }
    
    /**
     * Chuyển dữ liệu từ Form thành đối tượng ThietBi
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    private ThietBi createThietBiFromForm() throws IllegalArgumentException {
        try {
            String soSerial = txtSoSerial.getText().trim();
            if (soSerial.isEmpty()) {
                throw new IllegalArgumentException("Số Serial không được để trống.");
            }
            
            LoaiThietBi selectedLoai = (LoaiThietBi) cboLoaiThietBi.getSelectedItem();
            if (selectedLoai == null || selectedLoai.getId() == null) {
                throw new IllegalArgumentException("Vui lòng chọn loại thiết bị.");
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
                // Xử lý loại bỏ dấu phẩy (nếu người dùng nhập định dạng tiền tệ)
                giaMuaStr = giaMuaStr.replace(",", "").replace(".", ""); 
                giaMua = new BigDecimal(giaMuaStr);
            }
            
            String ghiChu = txtGhiChu.getText().trim();
            if (ghiChu.isEmpty()) {
                ghiChu = null;
            }
            
            // Khởi tạo đối tượng ThietBi (không có ID, createdAt, updatedAt)
            return new ThietBi(soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu);
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Định dạng Ngày mua không hợp lệ. Phải là yyyy-MM-dd.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá mua không hợp lệ.");
        } catch (IllegalArgumentException e) {
            throw e; // Ném lại các lỗi đã bắt
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu nhập không hợp lệ: " + e.getMessage());
        }
    }
    
    // =========================================================================
    //                            CÁC PHƯƠNG THỨC XỬ LÝ SỰ KIỆN MỚI
    // =========================================================================

    private void handleThem(ActionEvent e) {
        try {
            ThietBi newThietBi = createThietBiFromForm();
            
            // Gọi Controller để thực hiện thêm vào DB
            boolean success = thietBiController.createThietBi(newThietBi);
            
            if (success) {
                loadTableData(thietBiController.getAllThietBi()); // Tải lại toàn bộ bảng
                clearForm();
                LogoUtil.showMessageDialog(this, "Thêm thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                // Chọn hàng mới được thêm vào (nếu cần)
            } else {
                 LogoUtil.showMessageDialog(this, "Thêm thiết bị thất bại (Lỗi DB không xác định)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Bắt lỗi nghiệp vụ từ createThietBiFromForm hoặc Service (Số Serial trùng)
            LogoUtil.showMessageDialog(this, ex.getMessage(), "Lỗi Dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            LogoUtil.showMessageDialog(this, "Lỗi hệ thống khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleSua(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            LogoUtil.showMessageDialog(this, "Vui lòng chọn thiết bị cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Lấy ID của thiết bị đang được chọn trong danh sách hiện tại
            Long idToUpdate = thietBiList.get(selectedRow).getId(); 
            
            ThietBi updatedThietBi = createThietBiFromForm();
            updatedThietBi.setId(idToUpdate); // Gán lại ID cho đối tượng
            
            // Giữ nguyên createdAt từ đối tượng cũ
            ThietBi existingThietBi = thietBiList.get(selectedRow);
            updatedThietBi.setCreatedAt(existingThietBi.getCreatedAt()); 
            
            // Gọi Controller để thực hiện cập nhật vào DB
            boolean success = thietBiController.updateThietBi(updatedThietBi);
            
            if (success) {
                loadTableData(thietBiController.getAllThietBi()); // Tải lại toàn bộ bảng
                LogoUtil.showMessageDialog(this, "Cập nhật thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                LogoUtil.showMessageDialog(this, "Cập nhật thiết bị thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Bắt lỗi nghiệp vụ từ createThietBiFromForm hoặc Service (Số Serial trùng)
            LogoUtil.showMessageDialog(this, ex.getMessage(), "Lỗi Dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            LogoUtil.showMessageDialog(this, "Lỗi hệ thống khi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleXoa(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            LogoUtil.showMessageDialog(this, "Vui lòng chọn thiết bị cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ThietBi thietBi = thietBiList.get(selectedRow);
        int result = LogoUtil.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa thiết bị: " + thietBi.getSoSerial() + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Gọi Controller để xóa khỏi DB
            boolean success = thietBiController.deleteThietBi(thietBi.getId());
            
            if (success) {
                loadTableData(thietBiController.getAllThietBi()); // Tải lại toàn bộ bảng
                clearForm();
                LogoUtil.showMessageDialog(this, "Xóa thiết bị thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                LogoUtil.showMessageDialog(this, "Xóa thiết bị thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleLamMoi(ActionEvent e) {
        clearForm();
        table.clearSelection();
        loadTableData(thietBiController.getAllThietBi()); // Tải lại dữ liệu gốc từ DB
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
    }
    
    private void handleTimKiem(ActionEvent e) {
        // *** ĐẢM BẢO KEYWORD KHÔNG CÓ KHOẢNG TRẮNG DƯ THỪA TRƯỚC KHI TRUYỀN ***
        String keyword = txtTimKiem.getText().trim(); 
        String searchType = (String) cboTimKiem.getSelectedItem();
        
        // DÒNG DEBUG: Giúp bạn kiểm tra giá trị đang được truyền đi
        System.out.println("DEBUG: Searching for '" + keyword + "' using type '" + searchType + "'");
        
        // Gọi Controller để thực hiện tìm kiếm
        List<ThietBi> filteredList = thietBiController.searchThietBi(keyword, searchType);
        
        // Hiển thị kết quả tìm kiếm lên bảng
        loadTableData(filteredList); 
    }
    
    private void handleLamSach(ActionEvent e) {
        txtTimKiem.setText("");
        cboTimKiem.setSelectedIndex(0);
        loadTableData(thietBiController.getAllThietBi());
    }
    
    /**
     * Tải dữ liệu Loại thiết bị từ DAO và điền vào ComboBox.
     */
    private void loadLoaiThietBiData() {
        try {
            loaiThietBiList = loaiThietBiDAO.findAll();
            populateLoaiThietBiComboBox();
        } catch (Exception e) {
             LogoUtil.showMessageDialog(this, "Lỗi tải Loại Thiết Bị: " + e.getMessage() + ". Vui lòng kiểm tra kết nối DB.", "Lỗi DB", JOptionPane.ERROR_MESSAGE);
             loaiThietBiList = new ArrayList<>();
        }
    }

    // *** REMOVED initializeLoaiThietBiData() ***
    
    /**
     * Thiết lập giao diện bảng với padding và styling
     */
    private void setupTableAppearance() {
        // ... (Code setupTableAppearance không thay đổi, đã thêm ID vào cột 0)
        table.setRowHeight(35);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(184, 207, 229));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        javax.swing.table.TableCellRenderer cellRenderer = new javax.swing.table.TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JTextArea textArea = new JTextArea();
                textArea.setText(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(true);
                
                textArea.setFont(table.getFont());
                textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    if (row % 2 == 0) {
                        textArea.setBackground(Color.WHITE);
                    } else {
                        textArea.setBackground(new Color(248, 249, 250));
                    }
                    textArea.setForeground(table.getForeground());
                }
                
                int colWidth = table.getColumnModel().getColumn(column).getWidth();
                textArea.setSize(colWidth - 20, table.getRowHeight(row) - 10); // Trừ padding
                
                return textArea;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        table.getColumnModel().addColumnModelListener(new javax.swing.event.TableColumnModelListener() {
            @Override public void columnAdded(javax.swing.event.TableColumnModelEvent e) {}
            @Override public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {}
            @Override public void columnMoved(javax.swing.event.TableColumnModelEvent e) {}
            
            @Override
            public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
                SwingUtilities.invokeLater(() -> updateRowHeights());
            }
            
            @Override public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {}
        });
        
        // Thiết lập độ rộng cột (Đã thêm cột ID)
        if (table.getColumnCount() >= 8) {
            table.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
            table.getColumnModel().getColumn(1).setPreferredWidth(100); // Số Serial
            table.getColumnModel().getColumn(2).setPreferredWidth(120); // Loại thiết bị
            table.getColumnModel().getColumn(3).setPreferredWidth(90);  // Trạng thái
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Ngày mua
            table.getColumnModel().getColumn(5).setPreferredWidth(100); // Giá mua
            table.getColumnModel().getColumn(6).setPreferredWidth(180); // Ghi chú
            table.getColumnModel().getColumn(7).setPreferredWidth(120); // Ngày tạo
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
                    // Kiểm tra null cho maLoai (đảm bảo an toàn)
                    String maLoai = loai.getMaLoai() != null ? loai.getMaLoai() : "N/A";
                    setText(loai.getTenLoai() + " (" + maLoai + ")");
                }
                
                return this;
            }
        });
    }
}