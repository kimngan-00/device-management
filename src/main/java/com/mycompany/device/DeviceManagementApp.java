package com.mycompany.device;

import com.mycompany.device.ui.swing.frame.LoginFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Ứng dụng chính quản lý phòng ban và nhân viên
 * Main class cho ứng dụng Swing
 * @author Kim Ngan
 */
public class DeviceManagementApp {
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceManagementApp.class);
    
    public static void main(String[] args) {
        logger.info("Khởi động ứng dụng Swing Device Management");
        
        // Thiết lập Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            logger.info("Đã thiết lập System Look and Feel");
        } catch (Exception e) {
            logger.warn("Không thể thiết lập System Look and Feel: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                logger.info("Đã thiết lập Cross Platform Look and Feel");
            } catch (Exception ex) {
                logger.error("Không thể thiết lập Look and Feel: " + ex.getMessage());
            }
        }
        
        // Thiết lập font mặc định cho các component
        setupDefaultFonts();
        
        // Khởi động ứng dụng trong Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo và hiển thị Login Frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                
                logger.info("Ứng dụng Swing đã khởi động thành công");
                
            } catch (Exception e) {
                logger.error("Lỗi khi khởi động ứng dụng Swing: " + e.getMessage(), e);
                
                // Hiển thị error dialog
                JOptionPane.showMessageDialog(
                    null,
                    "Lỗi khi khởi động ứng dụng:\n" + e.getMessage(),
                    "Lỗi khởi động",
                    JOptionPane.ERROR_MESSAGE
                );
                
                System.exit(1);
            }
        });
    }
    
    /**
     * Thiết lập font mặc định cho các component Swing
     */
    private static void setupDefaultFonts() {
        try {
            // Font mặc định
            java.awt.Font defaultFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12);
            
            // Áp dụng cho các component
            UIManager.put("Label.font", defaultFont);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);
            UIManager.put("Table.font", defaultFont);
            UIManager.put("TableHeader.font", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
            UIManager.put("Menu.font", defaultFont);
            UIManager.put("MenuItem.font", defaultFont);
            UIManager.put("TitledBorder.font", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
            
            logger.info("Đã thiết lập font mặc định cho UI components");
            
        } catch (Exception e) {
            logger.warn("Không thể thiết lập font mặc định: " + e.getMessage());
        }
    }
}
