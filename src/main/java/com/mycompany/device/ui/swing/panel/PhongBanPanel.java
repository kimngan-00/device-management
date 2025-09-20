package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.PhongBan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel quản lý phòng ban với Swing - View trong MVC pattern
 * @author Kim Ngan
 */
public class PhongBanPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanPanel.class);
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField maPhongBanField;
    private JTextField tenPhongBanField;
    private JTextArea moTaTextArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearButton;
    private JTextField searchField;
    private JButton searchButton;
    
    public PhongBanPanel() {
        initializeUI();
        logger.info("Khởi tạo PhongBanPanel thành công");
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo các thành phần
        createSearchPanel();
        createTablePanel();
        createFormPanel();
        createButtonPanel();
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createSearchPanel(), BorderLayout.NORTH);
        topPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createFormPanel(), BorderLayout.CENTER);
        bottomPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.6);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        
        searchPanel.add(new JLabel("Tên phòng ban:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        searchButton = new JButton("Tìm kiếm");
        styleButton(searchButton);
        // Event listener sẽ được thiết lập bởi Controller
        searchPanel.add(searchButton);
        
        refreshButton = new JButton("Làm mới");
        styleButton(refreshButton);
        // Event listener sẽ được thiết lập bởi Controller
        searchPanel.add(refreshButton);
        
        return searchPanel;
    }
    
    private JScrollPane createTablePanel() {
        // Tạo table model
        String[] columnNames = {"Mã Phòng Ban", "Tên Phòng Ban", "Mô Tả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp trong table
            }
        };
        
        // Tạo table
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Table selection listener sẽ được thiết lập bởi Controller
        
        // Thiết lập độ rộng cột
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(300);
        
        // Thiết lập style
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(184, 207, 229));
        
        return new JScrollPane(table);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phòng ban"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mã phòng ban
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã phòng ban:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        maPhongBanField = new JTextField(20);
        formPanel.add(maPhongBanField, gbc);
        
        // Tên phòng ban
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Tên phòng ban:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        tenPhongBanField = new JTextField(20);
        formPanel.add(tenPhongBanField, gbc);
        
        // Mô tả
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        moTaTextArea = new JTextArea(4, 20);
        moTaTextArea.setLineWrap(true);
        moTaTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(moTaTextArea);
        formPanel.add(scrollPane, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Thêm");
        styleButton(addButton);
        // Event listener sẽ được thiết lập bởi Controller
        
        updateButton = new JButton("Cập nhật");
        styleButton(updateButton);
        // Event listener sẽ được thiết lập bởi Controller
        
        deleteButton = new JButton("Xóa");
        styleButton(deleteButton);
        // Event listener sẽ được thiết lập bởi Controller
        
        clearButton = new JButton("Xóa form");
        styleButton(clearButton);
        // Event listener sẽ được thiết lập bởi Controller
        
       
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }
    
    // ========================= CONTROLLER INTERFACE METHODS =========================
    
    /**
     * Thiết lập event listener cho button Thêm
     */
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    /**
     * Thiết lập event listener cho button Cập nhật
     */
    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }
    
    /**
     * Thiết lập event listener cho button Xóa
     */
    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    /**
     * Thiết lập event listener cho button Tìm kiếm
     */
    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    /**
     * Thiết lập event listener cho button Làm mới
     */
    public void setRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    /**
     * Thiết lập event listener cho button Xóa form
     */
    public void setClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }
    
    /**
     * Lấy mã phòng ban từ form
     */
    public String getMaPhongBan() {
        return maPhongBanField.getText().trim();
    }
    
    /**
     * Lấy tên phòng ban từ form
     */
    public String getTenPhongBan() {
        return tenPhongBanField.getText().trim();
    }
    
    /**
     * Lấy mô tả từ form
     */
    public String getMoTa() {
        return moTaTextArea.getText().trim();
    }
    
    /**
     * Lấy text tìm kiếm
     */
    public String getSearchText() {
        return searchField.getText().trim();
    }
    
    /**
     * Lấy phòng ban được chọn từ table
     */
    public PhongBan getSelectedPhongBan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String maPhongBan = (String) tableModel.getValueAt(selectedRow, 0);
            String tenPhongBan = (String) tableModel.getValueAt(selectedRow, 1);
            String moTa = (String) tableModel.getValueAt(selectedRow, 2);
            return new PhongBan(maPhongBan, tenPhongBan, moTa.isEmpty() ? null : moTa);
        }
        return null;
    }
    
    /**
     * Populate form với dữ liệu phòng ban
     */
    public void populateForm(PhongBan phongBan) {
        if (phongBan != null) {
            maPhongBanField.setText(phongBan.getMaPhongBan());
            tenPhongBanField.setText(phongBan.getTenPhongBan());
            moTaTextArea.setText(phongBan.getMoTa() != null ? phongBan.getMoTa() : "");
        }
    }
    
    /**
     * Xóa sạch form
     */
    public void clearForm() {
        maPhongBanField.setText("");
        tenPhongBanField.setText("");
        moTaTextArea.setText("");
        table.clearSelection();
    }
    
    /**
     * Xóa sạch tìm kiếm
     */
    public void clearSearch() {
        searchField.setText("");
    }
    
    /**
     * Cập nhật dữ liệu table
     */
    public void updateTableData(List<PhongBan> phongBanList) {
        tableModel.setRowCount(0);
        for (PhongBan pb : phongBanList) {
            Object[] row = {
                pb.getMaPhongBan(),
                pb.getTenPhongBan(),
                pb.getMoTa() != null ? pb.getMoTa() : ""
            };
            tableModel.addRow(row);
        }
        logger.debug("Đã cập nhật table với {} bản ghi", phongBanList.size());
    }
    
    /**
     * Hiển thị thông báo thành công
     */
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Hiển thị thông báo lỗi
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Hiển thị thông báo cảnh báo
     */
    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Hiển thị dialog xác nhận
     */
    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    
    /**
     * Thiết lập listener cho việc chọn row trong table
     * Controller sẽ sử dụng để biết khi nào user chọn một phòng ban
     */
    public void setTableSelectionListener(Runnable onSelectionChanged) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onSelectionChanged != null) {
                onSelectionChanged.run();
            }
        });
    }
    
    /**
     * Style button với padding và border màu đen
     */
    private void styleButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16) // top, left, bottom, right padding
        ));
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        // Thêm hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }
}
