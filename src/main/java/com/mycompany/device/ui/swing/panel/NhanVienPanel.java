package com.mycompany.device.ui.swing.panel;

import javax.swing.*;

/**
 * Panel quản lý nhân viên với Swing (placeholder)
 * @author Kim Ngan
 */
public class NhanVienPanel extends JPanel {
    
    public NhanVienPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new java.awt.BorderLayout());
        
        JLabel label = new JLabel("Quản lý Nhân viên - Đang phát triển", SwingConstants.CENTER);
        label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16));
        add(label, java.awt.BorderLayout.CENTER);
    }
    
    public void refreshData() {
        // TODO: Implement refresh data
    }
}
