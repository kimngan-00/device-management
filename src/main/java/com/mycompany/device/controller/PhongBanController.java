package com.mycompany.device.controller;

import com.mycompany.device.model.PhongBan;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.ui.swing.panel.PhongBanPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller cho PhongBan trong MVC pattern
 * Kết nối giữa PhongBanPanel (View) và PhongBanService (Model)
 * Xử lý tất cả business logic và events
 * 
 * @author MVC Team
 */
public class PhongBanController {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanController.class);
    
    private final PhongBanPanel view;
    private final PhongBanService service;
    
    /**
     * Constructor - khởi tạo Controller với View và Service
     */
    public PhongBanController(PhongBanPanel view, PhongBanService service) {
        this.view = view;
        this.service = service;
        
        setupEventHandlers();
        loadInitialData();
        
        logger.info("PhongBanController đã được khởi tạo thành công");
    }
    
    /**
     * Thiết lập các event handlers cho View
     */
    private void setupEventHandlers() {
        view.setAddButtonListener(new AddPhongBanAction());
        view.setUpdateButtonListener(new UpdatePhongBanAction());
        view.setDeleteButtonListener(new DeletePhongBanAction());
        view.setSearchButtonListener(new SearchPhongBanAction());
        view.setRefreshButtonListener(new RefreshAction());
        view.setClearButtonListener(new ClearFormAction());
        
        // Thiết lập table selection listener - sử dụng anonymous lambda
        view.setTableSelectionListener(() -> {
            PhongBan selected = view.getSelectedPhongBan();
            if (selected != null) {
                view.populateForm(selected);
                logger.debug("Đã chọn phòng ban: {}", selected.getMaPhongBan());
            }
        });
        
        logger.debug("Đã thiết lập tất cả event handlers");
    }
    
    /**
     * Load dữ liệu ban đầu khi khởi tạo
     */
    private void loadInitialData() {
        try {
            List<PhongBan> phongBanList = service.xemDanhSachPhongBan();
            view.updateTableData(phongBanList);
            logger.info("Đã load {} phòng ban", phongBanList.size());
        } catch (Exception e) {
            logger.error("Lỗi khi load dữ liệu ban đầu", e);
            view.showErrorMessage("Không thể tải dữ liệu: " + e.getMessage());
        }
    }
    
    /**
     * Action class cho nút Thêm
     */
    private class AddPhongBanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleAddPhongBan();
        }
    }
    
    /**
     * Action class cho nút Cập nhật  
     */
    private class UpdatePhongBanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleUpdatePhongBan();
        }
    }
    
    /**
     * Action class cho nút Xóa
     */
    private class DeletePhongBanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleDeletePhongBan();
        }
    }
    
    /**
     * Action class cho nút Tìm kiếm
     */
    private class SearchPhongBanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleSearchPhongBan();
        }
    }
    
    /**
     * Action class cho nút Làm mới
     */
    private class RefreshAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleRefresh();
        }
    }
    
    /**
     * Action class cho nút Xóa form
     */
    private class ClearFormAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.clearForm();
            logger.debug("Đã xóa form");
        }
    }
    
    /**
     * Xử lý thêm phòng ban mới
     */
    private void handleAddPhongBan() {
        try {
            // Validate dữ liệu
            String maPhongBan = view.getMaPhongBan();
            String tenPhongBan = view.getTenPhongBan();
            String moTa = view.getMoTa();
            
            if (!validatePhongBanData(maPhongBan, tenPhongBan)) {
                return;
            }
            
            // Tạo phòng ban mới
            PhongBan phongBan = new PhongBan(maPhongBan, tenPhongBan, 
                moTa.isEmpty() ? null : moTa);
            
            // Gọi service để thêm
            service.taoPhongBan(phongBan);
            
            // Cập nhật UI
            refreshData();
            view.clearForm();
            view.showSuccessMessage("Thêm phòng ban thành công!");
            
            logger.info("Đã thêm phòng ban: {}", maPhongBan);
            
        } catch (Exception e) {
            logger.error("Lỗi khi thêm phòng ban", e);
            view.showErrorMessage("Lỗi khi thêm phòng ban: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý cập nhật phòng ban
     */
    private void handleUpdatePhongBan() {
        try {
            PhongBan selected = view.getSelectedPhongBan();
            if (selected == null) {
                view.showWarningMessage("Vui lòng chọn phòng ban cần cập nhật!");
                return;
            }
            
            // Validate dữ liệu
            String maPhongBan = view.getMaPhongBan();
            String tenPhongBan = view.getTenPhongBan();
            String moTa = view.getMoTa();
            
            if (!validatePhongBanData(maPhongBan, tenPhongBan)) {
                return;
            }
            
            // Tạo phòng ban với dữ liệu mới
            PhongBan updatedPhongBan = new PhongBan(maPhongBan, tenPhongBan, 
                moTa.isEmpty() ? null : moTa);
            
            // Gọi service để cập nhật
            service.capNhatPhongBan(updatedPhongBan);
            
            // Cập nhật UI
            refreshData();
            view.clearForm();
            view.showSuccessMessage("Cập nhật phòng ban thành công!");
            
            logger.info("Đã cập nhật phòng ban: {}", maPhongBan);
            
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật phòng ban", e);
            view.showErrorMessage("Lỗi khi cập nhật phòng ban: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý xóa phòng ban
     */
    private void handleDeletePhongBan() {
        try {
            PhongBan selected = view.getSelectedPhongBan();
            if (selected == null) {
                view.showWarningMessage("Vui lòng chọn phòng ban cần xóa!");
                return;
            }
            
            // Xác nhận xóa
            int confirm = view.showConfirmDialog(
                "Bạn có chắc chắn muốn xóa phòng ban '" + selected.getTenPhongBan() + "'?",
                "Xác nhận xóa"
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi service để xóa
                service.xoaPhongBan(selected.getMaPhongBan());
                
                // Cập nhật UI
                refreshData();
                view.clearForm();
                view.showSuccessMessage("Xóa phòng ban thành công!");
                
                logger.info("Đã xóa phòng ban: {}", selected.getMaPhongBan());
            }
            
        } catch (Exception e) {
            logger.error("Lỗi khi xóa phòng ban", e);
            view.showErrorMessage("Lỗi khi xóa phòng ban: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý tìm kiếm phòng ban
     */
    private void handleSearchPhongBan() {
        try {
            String searchText = view.getSearchText();
            List<PhongBan> results;
            
            if (searchText.isEmpty()) {
                results = service.xemDanhSachPhongBan();
            } else {
                results = service.timKiemPhongBanTheoTen(searchText);
            }
            
            view.updateTableData(results);
            logger.debug("Tìm kiếm '{}' trả về {} kết quả", searchText, results.size());
            
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm phòng ban", e);
            view.showErrorMessage("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý làm mới dữ liệu
     */
    private void handleRefresh() {
        view.clearSearch();
        refreshData();
        view.clearForm();
        logger.debug("Đã làm mới dữ liệu");
    }
    
    /**
     * Refresh dữ liệu từ service
     */
    private void refreshData() {
        try {
            List<PhongBan> phongBanList = service.xemDanhSachPhongBan();
            view.updateTableData(phongBanList);
        } catch (Exception e) {
            logger.error("Lỗi khi refresh dữ liệu", e);
            view.showErrorMessage("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
    
    /**
     * Validate dữ liệu phòng ban
     */
    private boolean validatePhongBanData(String maPhongBan, String tenPhongBan) {
        if (maPhongBan.isEmpty()) {
            view.showWarningMessage("Mã phòng ban không được để trống!");
            return false;
        }
        
        if (tenPhongBan.isEmpty()) {
            view.showWarningMessage("Tên phòng ban không được để trống!");
            return false;
        }
        
        if (maPhongBan.length() > 10) {
            view.showWarningMessage("Mã phòng ban không được quá 10 ký tự!");
            return false;
        }
        
        if (tenPhongBan.length() > 100) {
            view.showWarningMessage("Tên phòng ban không được quá 100 ký tự!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Public method để refresh từ bên ngoài (nếu cần)
     */
    public void refresh() {
        handleRefresh();
    }
}
