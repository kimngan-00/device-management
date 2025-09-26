package com.mycompany.device.ui.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Router chịu trách nhiệm điều hướng giữa các screen trong ứng dụng
 * Đây KHÔNG PHẢI là Controller trong MVC pattern mà là Router/Navigator
 * @author Kim Ngan
 */
public class ScreenRouter {
    
    private static final Logger logger = LoggerFactory.getLogger(ScreenRouter.class);
    
    // View components
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private Map<String, JPanel> screens;
    private String currentScreen;
    
    // Navigation constants
    public static final String DASHBOARD = "DASHBOARD";
    public static final String PHONG_BAN = "PHONG_BAN";
    public static final String NHAN_VIEN = "NHAN_VIEN";
    public static final String THIET_BI = "THIET_BI";
    public static final String YEU_CAU = "YEU_CAU";
    public static final String ADMIN_YEU_CAU = "ADMIN_YEU_CAU";
    public static final String LICH_SU_CAP_PHAT = "LICH_SU_CAP_PHAT";
    public static final String HO_SO_CA_NHAN = "HO_SO_CA_NHAN";
    public static final String BAO_CAO = "BAO_CAO";
    public static final String CAI_DAT = "CAI_DAT";
    
    /**
     * Constructor
     * @param cardLayout CardLayout để quản lý screens
     * @param mainContentPanel Panel chứa các screens
     */
    public ScreenRouter(CardLayout cardLayout, JPanel mainContentPanel) {
        this.cardLayout = cardLayout;
        this.mainContentPanel = mainContentPanel;
        this.screens = new HashMap<>();
        this.currentScreen = DASHBOARD;
        
        logger.info("ScreenRouter đã được khởi tạo");
    }
    
    /**
     * Đăng ký một screen với controller
     * @param screenId ID của screen
     * @param panel Panel component của screen
     */
    public void registerScreen(String screenId, JPanel panel) {
        screens.put(screenId, panel);
        mainContentPanel.add(panel, screenId);
        logger.debug("Đã đăng ký screen: {}", screenId);
    }
    
    /**
     * Điều hướng đến một screen cụ thể
     * @param screenId ID của screen cần chuyển đến
     */
    public void navigateToScreen(String screenId) {
        if (!screens.containsKey(screenId)) {
            logger.warn("Screen không tồn tại: {}", screenId);
            return;
        }
        
        String previousScreen = currentScreen;
        currentScreen = screenId;
        
        // Thực hiện chuyển screen
        cardLayout.show(mainContentPanel, screenId);
        
        // Log navigation
        logger.info("Điều hướng từ {} đến {}", previousScreen, screenId);
        
       
        onScreenChanged(previousScreen, currentScreen);
    }
    
    /**
     * Quay lại screen trước đó
     */
    public void goBack() {
       
        navigateToScreen(DASHBOARD);
    }
    
    /**
     * Lấy screen hiện tại
     * @return ID của screen hiện tại
     */
    public String getCurrentScreen() {
        return currentScreen;
    }
    
    /**
     * Kiểm tra xem screen có tồn tại không
     * @param screenId ID của screen
     * @return true nếu tồn tại
     */
    public boolean hasScreen(String screenId) {
        return screens.containsKey(screenId);
    }
    
    /**
     * Lấy panel của một screen
     * @param screenId ID của screen
     * @return Panel component hoặc null nếu không tìm thấy
     */
    public JPanel getScreen(String screenId) {
        return screens.get(screenId);
    }
    
    /**
     * Refresh screen hiện tại
     */
    public void refreshCurrentScreen() {
        JPanel currentPanel = screens.get(currentScreen);
        if (currentPanel != null) {
            currentPanel.revalidate();
            currentPanel.repaint();
            logger.debug("Đã refresh screen: {}", currentScreen);
        }
    }
    
    /**
     * Xử lý khi screen thay đổi
     * Override method này để thêm custom logic
     * @param previousScreen Screen trước đó
     * @param newScreen Screen mới
     */
    protected void onScreenChanged(String previousScreen, String newScreen) {
     
        switch (newScreen) {
            case PHONG_BAN:
                handlePhongBanScreenActivated();
                break;
            case NHAN_VIEN:
                handleNhanVienScreenActivated();
                break;
            case THIET_BI:
                handleThietBiScreenActivated();
                break;
            case YEU_CAU:
                handleYeuCauScreenActivated();
                break;
            case ADMIN_YEU_CAU:
                handleAdminYeuCauScreenActivated();
                break;
            case HO_SO_CA_NHAN:
                handleHoSoCaNhanScreenActivated();
                break;
            case BAO_CAO:
                handleBaoCaoScreenActivated();
                break;
            default:
                break;
        }
    }
    
    /**
     * Xử lý khi Phòng ban screen được kích hoạt
     */
    private void handlePhongBanScreenActivated() {
        logger.debug("Phòng ban screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Nhân viên screen được kích hoạt  
     */
    private void handleNhanVienScreenActivated() {
        logger.debug("Nhân viên screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Thiết bị screen được kích hoạt
     */
    private void handleThietBiScreenActivated() {
        logger.debug("Thiết bị screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Yêu cầu screen được kích hoạt
     */
    private void handleYeuCauScreenActivated() {
        logger.debug("Yêu cầu screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Admin Yêu cầu screen được kích hoạt
     */
    private void handleAdminYeuCauScreenActivated() {
        logger.debug("Admin Yêu cầu screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Hồ sơ cá nhân screen được kích hoạt
     */
    private void handleHoSoCaNhanScreenActivated() {
        logger.debug("Hồ sơ cá nhân screen đã được kích hoạt");
       
    }
    
    /**
     * Xử lý khi Báo cáo screen được kích hoạt
     */
    private void handleBaoCaoScreenActivated() {
        logger.debug("Báo cáo screen đã được kích hoạt");
       
    }
    
    /**
     * Đóng ứng dụng
     */
    public void exitApplication() {
        logger.info("Đóng ứng dụng");
        System.exit(0);
    }
    
    /**
     * Logout và quay về login screen
     */
    public void logout() {
        logger.info("Đăng xuất khỏi ứng dụng");
       

        // Close current frame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainContentPanel);
        if (frame != null) {
            frame.dispose();
        }
        
        // Restart login process
        SwingUtilities.invokeLater(() -> {
            try {
                // Dynamically load LoginFrame to avoid circular imports
                Class<?> loginFrameClass = Class.forName("com.mycompany.device.ui.swing.frame.LoginFrame");
                Object loginFrame = loginFrameClass.getDeclaredConstructor().newInstance();
                loginFrameClass.getMethod("setVisible", boolean.class).invoke(loginFrame, true);
                
                logger.info("Đã khởi động lại LoginFrame");
            } catch (Exception e) {
                logger.error("Lỗi khi khởi động lại LoginFrame", e);
                System.exit(0); // Fallback to exit
            }
        });
    }
}
