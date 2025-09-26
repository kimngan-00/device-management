package com.mycompany.device.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Utility class để quản lý logo và icon của ứng dụng
 * @author Kim Ngan - Utility Layer
 */
public class LogoUtil {
    
    private static final String LOGO_PATH = "/logo.png";
    private static ImageIcon logoIcon = null;
    
    /**
     * Lấy logo icon của ứng dụng
     * @return ImageIcon của logo, null nếu không tìm thấy
     */
    public static ImageIcon getLogoIcon() {
        if (logoIcon == null) {
            try {
                URL logoUrl = LogoUtil.class.getResource(LOGO_PATH);
                if (logoUrl != null) {
                    logoIcon = new ImageIcon(logoUrl);
                }
            } catch (Exception e) {
                System.err.println("Không thể load logo: " + e.getMessage());
            }
        }
        return logoIcon;
    }
    
    /**
     * Lấy logo icon với kích thước tùy chỉnh
     * @param width chiều rộng mong muốn
     * @param height chiều cao mong muốn
     * @return ImageIcon được scale theo kích thước
     */
    public static ImageIcon getLogoIcon(int width, int height) {
        ImageIcon original = getLogoIcon();
        if (original != null) {
            Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
    
    /**
     * Lấy logo image để set làm icon cho frame
     * @return Image object của logo
     */
    public static Image getLogoImage() {
        ImageIcon icon = getLogoIcon();
        return icon != null ? icon.getImage() : null;
    }
    
    /**
     * Set logo cho JFrame
     * @param frame JFrame cần set logo
     */
    public static void setFrameIcon(JFrame frame) {
        Image logoImage = getLogoImage();
        if (logoImage != null) {
            frame.setIconImage(logoImage);
        }
    }
    
    /**
     * Set logo cho JDialog
     * @param dialog JDialog cần set logo
     */
    public static void setDialogIcon(JDialog dialog) {
        Image logoImage = getLogoImage();
        if (logoImage != null) {
            dialog.setIconImage(logoImage);
        }
    }
    
    /**
     * Hiển thị JOptionPane với logo
     * @param parentComponent component cha
     * @param message nội dung thông báo
     * @param title tiêu đề
     * @param messageType loại message (JOptionPane.INFORMATION_MESSAGE, etc.)
     */
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        ImageIcon logoIcon = getLogoIcon(48, 48); // Icon size 48x48 cho popup
        if (logoIcon != null) {
            JOptionPane.showMessageDialog(parentComponent, message, title, messageType, logoIcon);
        } else {
            JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
        }
    }
    
    /**
     * Hiển thị JOptionPane confirm dialog với logo
     * @param parentComponent component cha
     * @param message nội dung thông báo
     * @param title tiêu đề
     * @param optionType loại option (JOptionPane.YES_NO_OPTION, etc.)
     * @return kết quả lựa chọn của user
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        ImageIcon logoIcon = getLogoIcon(48, 48);
        if (logoIcon != null) {
            return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType, JOptionPane.QUESTION_MESSAGE, logoIcon);
        } else {
            return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
        }
    }
    
    /**
     * Hiển thị JOptionPane input dialog với logo
     * @param parentComponent component cha
     * @param message nội dung thông báo
     * @param title tiêu đề
     * @return input string từ user
     */
    public static String showInputDialog(Component parentComponent, Object message, String title) {
        ImageIcon logoIcon = getLogoIcon(48, 48);
        if (logoIcon != null) {
            return (String) JOptionPane.showInputDialog(parentComponent, message, title, 
                JOptionPane.QUESTION_MESSAGE, logoIcon, null, null);
        } else {
            return JOptionPane.showInputDialog(parentComponent, message);
        }
    }
}
