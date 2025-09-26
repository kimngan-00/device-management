package com.mycompany.device;

import com.mycompany.device.ui.swing.frame.LoginFrame;
import com.mycompany.device.util.LogoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Ứng dụng quản lý thiết bị
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
        
        // Thiết lập application icon cho desktop
        setupApplicationIcon();
        
        // Thiết lập System Tray nếu được hỗ trợ
        setupSystemTray();
        
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
    
    /**
     * Thiết lập application icon cho desktop và taskbar
     */
    private static void setupApplicationIcon() {
        try {
            // Lấy logo icon từ LogoUtil
            ImageIcon logoIcon = LogoUtil.getLogoIcon();
            if (logoIcon != null) {
                Image logoImage = logoIcon.getImage();
                
                // Kiểm tra xem có hỗ trợ Taskbar API không (Java 9+)
                if (Taskbar.isTaskbarSupported()) {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    
                    // Set icon cho taskbar
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(logoImage);
                        logger.info("Đã thiết lập icon cho taskbar");
                    }
                } else {
                    logger.info("Taskbar API không được hỗ trợ trên hệ thống này");
                }
                
                // Set default icon cho tất cả JFrame
                // Sẽ được áp dụng cho các frame không có icon riêng
                java.util.List<Image> iconImages = new java.util.ArrayList<>();
                iconImages.add(logoImage);
                iconImages.add(LogoUtil.getLogoIcon(32, 32).getImage());
                iconImages.add(LogoUtil.getLogoIcon(16, 16).getImage());
                
                // Set default icon cho application
                if (Desktop.isDesktopSupported()) {
                    // Có thể thêm các tương tác desktop khác ở đây
                    logger.info("Desktop API được hỗ trợ");
                }
                
                logger.info("Đã thiết lập application icon thành công");
                
            } else {
                logger.warn("Không thể tải logo icon");
            }
            
        } catch (Exception e) {
            logger.warn("Không thể thiết lập application icon: " + e.getMessage());
        }
    }
    
    /**
     * Thiết lập System Tray với logo của ứng dụng
     */
    private static void setupSystemTray() {
        try {
            // Kiểm tra xem system tray có được hỗ trợ không
            if (!SystemTray.isSupported()) {
                logger.info("System Tray không được hỗ trợ trên hệ thống này");
                return;
            }
            
            SystemTray tray = SystemTray.getSystemTray();
            ImageIcon logoIcon = LogoUtil.getLogoIcon(16, 16); // Size nhỏ cho tray
            
            if (logoIcon != null) {
                // Tạo popup menu cho tray icon
                PopupMenu popup = new PopupMenu();
                
                MenuItem openItem = new MenuItem("Mở ứng dụng");
                MenuItem exitItem = new MenuItem("Thoát");
                
                popup.add(openItem);
                popup.addSeparator();
                popup.add(exitItem);
                
                // Tạo tray icon
                TrayIcon trayIcon = new TrayIcon(logoIcon.getImage(), "Device Management System", popup);
                trayIcon.setImageAutoSize(true);
                
                // Thêm action listeners
                openItem.addActionListener(e -> {
                    // Logic để mở lại ứng dụng (sẽ implement sau)
                    logger.info("Mở ứng dụng từ system tray");
                });
                
                exitItem.addActionListener(e -> {
                    logger.info("Thoát ứng dụng từ system tray");
                    System.exit(0);
                });
                
                // Double click để mở ứng dụng
                trayIcon.addActionListener(e -> {
                    logger.info("Double click trên tray icon");
                });
                
                // Thêm tray icon vào system tray
                tray.add(trayIcon);
                logger.info("Đã thiết lập System Tray icon thành công");
                
            } else {
                logger.warn("Không thể tải logo icon cho System Tray");
            }
            
        } catch (Exception e) {
            logger.warn("Không thể thiết lập System Tray: " + e.getMessage());
        }
    }
}
