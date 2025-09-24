package com.mycompany.device.ui.swing.frame;

import com.mycompany.device.controller.AuthController;
import com.mycompany.device.controller.PhongBanController;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.service.impl.PhongBanServiceImpl;
import com.mycompany.device.ui.swing.panel.PhongBanPanel;
import com.mycompany.device.ui.route.ScreenRouter;
import com.mycompany.device.ui.swing.panel.NhanVienPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.mycompany.device.model.NhanVien;

/**
 * Giao diện chính với Sidebar Menu Layout
 * Sidebar bên trái chứa menu navigation
 * Main content bên phải hiển thị các panels
 * 
 * @author UI Team - MainFrame with Sidebar Layout
 */
public class MainFrame extends JFrame {
    
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    
    // UI Components
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private PhongBanPanel phongBanPanel;
    private NhanVienPanel nhanVienPanel;
    private JPanel dashboardPanel;
    private JLabel statusLabel;
    
    // Screen Router (for navigation between screens)
    private ScreenRouter screenRouter;
    
    // MVC Controllers
    private PhongBanController phongBanController;
    private AuthController authController;
    
    // Menu items
    private JPanel selectedMenuItem = null;
    
    // UI Constants
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font MENU_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font STATUS_FONT = new Font("Arial", Font.PLAIN, 12);
    
    private static final Color SIDEBAR_COLOR = new Color(52, 58, 64);
    private static final Color SIDEBAR_HOVER_COLOR = new Color(73, 80, 87);
    private static final Color SIDEBAR_SELECTED_COLOR = new Color(0, 123, 255);
    private static final Color MAIN_BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color TEXT_PRIMARY_COLOR = Color.WHITE;
    private static final Color TEXT_SECONDARY_COLOR = new Color(173, 181, 189);

    // Thêm constructor mới để nhận AuthController
    public MainFrame(AuthController authController) {
        this.authController = authController;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadInitialData();
        
        // Hiển thị thông tin user đã đăng nhập
        if (authController.getCurrentUser() != null) {
            NhanVien currentUser = authController.getCurrentUser();
            setTitle("Hệ Thống Quản Lý Thiết Bị - " + currentUser.getTenNhanVien() + " (" + currentUser.getRole().getDisplayName() + ")");
        }
        
        logger.info("MainFrame đã được khởi tạo với AuthController");
    }
    
    /**
     * Khoi tao cac UI components
     */
    private void initializeComponents() {
        // Frame configuration
        setTitle("Hệ Thống Quản Lý Thiết Bị CNTT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        
        // Initialize panels
        phongBanPanel = new PhongBanPanel();
        nhanVienPanel = new NhanVienPanel();
        
        // Initialize services and controllers 
        PhongBanService phongBanService = new PhongBanServiceImpl();
        phongBanController = new PhongBanController(phongBanPanel, phongBanService);
        
        // Create card layout for main content
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(MAIN_BACKGROUND_COLOR);
        
        // Initialize ScreenRouter
        screenRouter = new ScreenRouter(cardLayout, mainContentPanel);
        
        // Add panels to card layout via ScreenRouter
        dashboardPanel = createDashboardPanel();
        screenRouter.registerScreen(ScreenRouter.DASHBOARD, dashboardPanel);
        screenRouter.registerScreen(ScreenRouter.PHONG_BAN, phongBanPanel);
        screenRouter.registerScreen(ScreenRouter.NHAN_VIEN, nhanVienPanel);
        screenRouter.registerScreen(ScreenRouter.BAO_CAO, createBaoCaoPanel());
        screenRouter.registerScreen(ScreenRouter.CAI_DAT, createCaiDatPanel());
        
        // Status label
        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setFont(STATUS_FONT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.WHITE);
        
        logger.info("Đã khởi tạo tất cả components");
    }
    
    /**
     * Thiet lap layout chinh
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create sidebar
        sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Add main content
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Add status bar
        add(statusLabel, BorderLayout.SOUTH);
        
        logger.info("Đã thiết lập layout thành công");
    }
    
    /**
     * Thiet lap event handlers
     */
    private void setupEventHandlers() {
        // Window closing event
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });
        
        logger.info("Đã thiết lập event handlers");
    }
    
    /**
     * Tao sidebar panel voi menu navigation
     */
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        
        // Header
        JPanel headerPanel = createSidebarHeader();
        sidebar.add(headerPanel);
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu items
        sidebar.add(createMenuItem("🏠", "Dashboard", "DASHBOARD"));
        sidebar.add(createMenuItem("🏢", "Quản lý Phòng ban", "PHONGBAN"));
        sidebar.add(createMenuItem("👥", "Quản lý Nhân viên", "NHANVIEN"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuItem("📊", "Báo cáo", "REPORTS"));
        sidebar.add(createMenuItem("⚙️", "Cài đặt", "SETTINGS"));
        
        // Spacer
        sidebar.add(Box.createVerticalGlue());
        
        // Logout button
        sidebar.add(createMenuItem("🚪", "Đăng xuất", "LOGOUT"));
        sidebar.add(Box.createVerticalStrut(20));
        
        return sidebar;
    }
    
    /**
     * Tao header cho sidebar
     */
    private JPanel createSidebarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SIDEBAR_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel titleLabel = new JLabel("Hệ Thống Quản Lý");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Thiết bị CNTT");
        subtitleLabel.setFont(MENU_FONT);
        subtitleLabel.setForeground(TEXT_SECONDARY_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        header.add(titleLabel, BorderLayout.NORTH);
        header.add(subtitleLabel, BorderLayout.SOUTH);
        
        return header;
    }
    
    /**
     * Tao menu item cho sidebar
     */
    private JPanel createMenuItem(String icon, String text, String action) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(SIDEBAR_COLOR);
        menuItem.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setForeground(TEXT_SECONDARY_COLOR);
        iconLabel.setPreferredSize(new Dimension(30, 24));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(MENU_FONT);
        textLabel.setForeground(TEXT_SECONDARY_COLOR);
        
        menuItem.add(iconLabel, BorderLayout.WEST);
        menuItem.add(textLabel, BorderLayout.CENTER);
        
        // Mouse events for hover effect
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (menuItem != selectedMenuItem) {
                    menuItem.setBackground(SIDEBAR_HOVER_COLOR);
                    iconLabel.setForeground(TEXT_PRIMARY_COLOR);
                    textLabel.setForeground(TEXT_PRIMARY_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (menuItem != selectedMenuItem) {
                    menuItem.setBackground(SIDEBAR_COLOR);
                    iconLabel.setForeground(TEXT_SECONDARY_COLOR);
                    textLabel.setForeground(TEXT_SECONDARY_COLOR);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(menuItem, iconLabel, textLabel);
                handleMenuAction(action, text);
            }
        });
        
        return menuItem;
    }
    
    /**
     * Xu ly selection cua menu item
     */
    private void selectMenuItem(JPanel menuItem, JLabel iconLabel, JLabel textLabel) {
        // Reset previous selection
        if (selectedMenuItem != null) {
            selectedMenuItem.setBackground(SIDEBAR_COLOR);
            Component[] components = selectedMenuItem.getComponents();
            if (components.length >= 2) {
                ((JLabel) components[0]).setForeground(TEXT_SECONDARY_COLOR);
                ((JLabel) components[1]).setForeground(TEXT_SECONDARY_COLOR);
            }
        }
        
        // Set new selection
        selectedMenuItem = menuItem;
        menuItem.setBackground(SIDEBAR_SELECTED_COLOR);
        iconLabel.setForeground(TEXT_PRIMARY_COLOR);
        textLabel.setForeground(TEXT_PRIMARY_COLOR);
    }
    
    /**
     * Xu ly action khi click menu
     */
    private void handleMenuAction(String action, String menuText) {
        updateStatus("Đang xem: " + menuText);
        
        switch (action) {
            case "DASHBOARD":
                showDashboard();
                break;
            case "PHONGBAN":
                showPhongBanPanel();
                break;
            case "NHANVIEN":
                showNhanVienPanel();
                break;
            case "REPORTS":
                showReports();
                break;
            case "SETTINGS":
                showSettings();
                break;
            case "LOGOUT":
                handleLogout();
                break;
            default:
                logger.warn("Unknown menu action: " + action);
        }
        
        logger.info("Menu clicked: " + menuText);
    }
    
    /**
     * Tao dashboard panel
     */
    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(MAIN_BACKGROUND_COLOR);
        dashboard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ Thống Quản Lý Thiết Bị CNTT");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(52, 58, 64));
        
        // Stats panel
        JPanel statsPanel = createStatsPanel();
        
        dashboard.add(welcomeLabel, BorderLayout.NORTH);
        dashboard.add(statsPanel, BorderLayout.CENTER);
        
        return dashboard;
    }
    
    /**
     * Tao bao cao panel (placeholder)
     */
    private JPanel createBaoCaoPanel() {
        JPanel baoCaoPanel = new JPanel(new BorderLayout());
        baoCaoPanel.setBackground(MAIN_BACKGROUND_COLOR);
        baoCaoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("📊 Báo Cáo & Thống Kê");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(52, 58, 64));
        
        // Content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(MAIN_BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        contentPanel.add(createFeatureCard("📈", "Báo cáo phòng ban", "Thống kê chi tiết về các phòng ban"));
        contentPanel.add(createFeatureCard("👥", "Báo cáo nhân viên", "Thống kê nhân viên theo phòng ban"));
        contentPanel.add(createFeatureCard("💻", "Báo cáo thiết bị", "Tình trạng và phân bổ thiết bị"));
        contentPanel.add(createFeatureCard("📊", "Dashboard Analytics", "Tổng quan hệ thống"));
        
        baoCaoPanel.add(titleLabel, BorderLayout.NORTH);
        baoCaoPanel.add(contentPanel, BorderLayout.CENTER);
        
        return baoCaoPanel;
    }
    
    /**
     * Tao cai dat panel (placeholder)
     */
    private JPanel createCaiDatPanel() {
        JPanel caiDatPanel = new JPanel(new BorderLayout());
        caiDatPanel.setBackground(MAIN_BACKGROUND_COLOR);
        caiDatPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("⚙️ Cài Đặt Hệ Thống");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(52, 58, 64));
        
        // Content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(MAIN_BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        contentPanel.add(createFeatureCard("🔐", "Quản lý người dùng", "Cài đặt tài khoản và quyền hạn"));
        contentPanel.add(createFeatureCard("🔄", "Sao lưu dữ liệu", "Backup và restore database"));
        contentPanel.add(createFeatureCard("🎨", "Giao diện", "Tùy chỉnh theme và layout"));
        contentPanel.add(createFeatureCard("📧", "Thông báo", "Cài đặt email và notifications"));
        
        caiDatPanel.add(titleLabel, BorderLayout.NORTH);
        caiDatPanel.add(contentPanel, BorderLayout.CENTER);
        
        return caiDatPanel;
    }
    
    /**
     * Tao feature card
     */
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(108, 117, 125));
        
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 5, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(iconLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(descLabel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Tao panel thong ke
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(MAIN_BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Stat cards
        statsPanel.add(createStatCard("🏢", "Phòng ban", "6", "Tổng số phòng ban"));
        statsPanel.add(createStatCard("👥", "Nhân viên", "25", "Tổng số nhân viên"));
        statsPanel.add(createStatCard("💻", "Thiết bị", "120", "Tổng số thiết bị"));
        statsPanel.add(createStatCard("📊", "Báo cáo", "8", "Báo cáo trong tháng"));
        
        return statsPanel;
    }
    
    /**
     * Tao stat card
     */
    private JPanel createStatCard(String icon, String title, String value, String subtitle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(0, 123, 255));
        
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(iconLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);
        contentPanel.add(subtitleLabel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Hien thi dashboard
     */
    private void showDashboard() {
        screenRouter.navigateToScreen(ScreenRouter.DASHBOARD);
    }
    
    /**
     * Hien thi phong ban panel
     */
    private void showPhongBanPanel() {
        screenRouter.navigateToScreen(ScreenRouter.PHONG_BAN);
    }
    
    /**
     * Hien thi nhan vien panel
     */
    private void showNhanVienPanel() {
        screenRouter.navigateToScreen(ScreenRouter.NHAN_VIEN);
    }
    
    /**
     * Hien thi bao cao (placeholder)
     */
    private void showReports() {
        screenRouter.navigateToScreen(ScreenRouter.BAO_CAO);
    }
    
    /**
     * Hien thi cai dat (placeholder)
     */
    private void showSettings() {
        screenRouter.navigateToScreen(ScreenRouter.CAI_DAT);
    }
    
    /**
     * Xu ly dang xuat
     */
    private void handleLogout() {
        if (authController != null) {
            authController.logout();
        }
        this.dispose();
        new LoginFrame().setVisible(true);
    }
    
    /**
     * Xac nhan thoat ung dung
     */
    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn thoát ứng dụng?",
            "Xác nhận thoát",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            logger.info("Đóng ứng dụng");
            System.exit(0);
        }
    }
    
    /**
     * Cap nhat status message
     */
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    /**
     * Load initial data (if needed)
     */
    private void loadInitialData() {
        // Example: If you need to load data for the main frame,
        // you would call methods from phongBanController or nhanVienController
        // For now, it's empty as per the original code.
    }
}
