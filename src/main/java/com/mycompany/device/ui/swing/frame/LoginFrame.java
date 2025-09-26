package com.mycompany.device.ui.swing.frame;

import com.mycompany.device.controller.AuthController;
import com.mycompany.device.controller.AuthController.AuthResult;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.util.LogoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Giao diện đăng nhập cho hệ thống quản lý thiết bị
 * Sử dụng AuthController thực tế thay vì Mock Authentication
 * 
 * @author UI Team - Login Interface with Real Auth
 */
public class LoginFrame extends JFrame {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);
    
    // UI Components
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    // Auth Controller
    private AuthController authController;
    
    // UI Constants - Colors và Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.ITALIC, 14);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);

    public LoginFrame() {
        initializeAuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // Set logo cho frame
        LogoUtil.setFrameIcon(this);
        
        logger.info("LoginFrame được khởi tạo thành công với AuthController");
    }

    /**
     * Khởi tạo AuthController thực tế
     */
    private void initializeAuthController() {
        this.authController = new AuthController();
        logger.info("Đã khởi tạo AuthController với NhanVienService thực tế");
    }

    /**
     * Khởi tạo các components của giao diện
     */
    private void initializeComponents() {
        // Email field
        emailField = new JTextField(20);
        emailField.setFont(FIELD_FONT);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(FIELD_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Login button
        loginButton = new JButton("Đăng nhập");
        loginButton.setFont(BUTTON_FONT);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(true);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        loginButton.setPreferredSize(new Dimension(120, 40));
        
        // Hover effect cho login button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        // Cancel button
        cancelButton = new JButton("Huỷ");
        cancelButton.setFont(BUTTON_FONT);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(true);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        
        // Hover effect cho cancel button
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        logger.info("Đã khởi tạo tất cả components");
    }
    
    /**
     * Thiết lập layout cho giao diện
     */
    private void setupLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Đăng Nhập - Hệ Thống Quản Lý Thiết Bị");
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Tạo main panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        
        logger.info("Đã thiết lập layout thành công");
    }
    
    /**
     * Thiết lập các event handlers
     */
    private void setupEventHandlers() {
        // Enter key cho login
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        };
        
        // Bind Enter key
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
        getRootPane().getActionMap().put("login", loginAction);
        
        // Button listeners
        loginButton.addActionListener(event -> performLogin());
        cancelButton.addActionListener(event -> System.exit(0));
        
        logger.info("Đã thiết lập tất cả event handlers");
    }

    /**
     * Tạo main panel chứa tất cả components
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Demo accounts panel
        JPanel demoPanel = createDemoAccountsPanel();
        mainPanel.add(demoPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Tạo header panel với title và subtitle
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Title
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ THIẾT BỊ");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Đăng nhập vào hệ thống");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    /**
     * Tạo form panel với email/password fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(LABEL_FONT);
        formPanel.add(emailLabel, gbc);

        // Email field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Password label
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(LABEL_FONT);
        formPanel.add(passwordLabel, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Button panel
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 10, 0);
        JPanel buttonPanel = createButtonPanel();
        formPanel.add(buttonPanel, gbc);

        // Status label
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(statusLabel, gbc);

        return formPanel;
    }

    /**
     * Tạo button panel với login và cancel buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(PANEL_COLOR);

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    /**
     * Tạo demo accounts panel
     */
    private JPanel createDemoAccountsPanel() {
        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(BACKGROUND_COLOR);
        demoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel demoLabel = new JLabel("Tài khoản demo (cần có trong database):");
        demoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        demoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel account1 = new JLabel("admin@company.com / admin123 (Admin)");
        account1.setFont(new Font("Arial", Font.PLAIN, 11));
        account1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel account2 = new JLabel("nva@company.com / 123456 (User)");
        account2.setFont(new Font("Arial", Font.PLAIN, 11));
        account2.setAlignmentX(Component.CENTER_ALIGNMENT);

        demoPanel.add(demoLabel);
        demoPanel.add(Box.createVerticalStrut(5));
        demoPanel.add(account1);
        demoPanel.add(account2);

        return demoPanel;
    }

    /**
     * Xử lý đăng nhập với AuthController thực tế
     * Sử dụng SwingWorker để tránh block UI thread
     */
    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Vui lòng nhập đầy đủ email và mật khẩu!", ERROR_COLOR);
            return;
        }

        // Disable button during login
        loginButton.setEnabled(false);
        loginButton.setText("Đang đăng nhập...");
        showStatus("Đang xác thực...", PRIMARY_COLOR);

        // Thực hiện đăng nhập trong background thread
        SwingWorker<AuthResult, Void> loginWorker = new SwingWorker<AuthResult, Void>() {
            @Override
            protected AuthResult doInBackground() throws Exception {
                return authController.login(email, password);
            }

            @Override
            protected void done() {
                try {
                    AuthResult result = get();

                    if (result.isSuccess()) {
                        showStatus("Đăng nhập thành công!", SUCCESS_COLOR);
                        logger.info("Đăng nhập thành công cho user: {}", result.getNhanVien().getTenNhanVien());

                        // Delay một chút để user thấy message thành công
                        Timer timer = new Timer(1000, event -> openMainFrame(result.getNhanVien()));
                        timer.setRepeats(false);
                        timer.start();

                    } else {
                        showStatus(result.getMessage(), ERROR_COLOR);
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception e) {
                    showStatus("Lỗi hệ thống: " + e.getMessage(), ERROR_COLOR);
                    logger.error("Lỗi trong quá trình đăng nhập: " + e.getMessage(), e);
                } finally {
                    // Re-enable button
                    loginButton.setEnabled(true);
                    loginButton.setText("Đăng Nhập");
                }
            }
        };

        loginWorker.execute();
    }

    /**
     * Hiển thị status message
     */
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    /**
     * Mở MainFrame sau khi đăng nhập thành công
     */
    private void openMainFrame(NhanVien nhanVien) {
        // Đóng login frame
        this.dispose();

        // Mở main frame với thông tin user
        SwingUtilities.invokeLater(() -> {
            try {
                // Truyền AuthController cho MainFrame để quản lý session
                MainFrame mainFrame = new MainFrame(authController);
                // Cập nhật sidebar dựa trên quyền của user
                mainFrame.refreshSidebar();
                mainFrame.setVisible(true);
                logger.info("Đã mở MainFrame cho user: {}", nhanVien.getTenNhanVien());
            } catch (Exception e) {
                logger.error("Lỗi khi mở MainFrame: " + e.getMessage(), e);
                LogoUtil.showMessageDialog(
                    null, 
                    "Lỗi khi mở giao diện chính: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    /**
     * Thiết lập dữ liệu mẫu cho demo
     */
    public void setDemoData() {
        emailField.setText("nva@company.com");
        passwordField.setText("123456");
    }

    /**
     * Lấy AuthController để sử dụng ở nơi khác
     */
    public AuthController getAuthController() {
        return authController;
    }

    /**
     * Main method để test LoginFrame độc lập
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Cannot set Look and Feel: " + e.getMessage());
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
