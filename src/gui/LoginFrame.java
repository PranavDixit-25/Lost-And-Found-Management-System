package gui;

import manager.AppManager;
import model.User;
import util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame is the first window users see when launching the application.
 * It provides options to login as Student or Admin, and to register.
 *
 * This is the ENTRY POINT of the GUI.
 */
public class LoginFrame extends JFrame {

    private AppManager appManager;

    // Mode: "STUDENT" or "ADMIN"
    private String loginMode = "STUDENT";

    // GUI Components
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel modeLabel;
    private JButton studentModeBtn;
    private JButton adminModeBtn;
    private JPanel loginPanel;

    public LoginFrame(AppManager appManager) {
        this.appManager = appManager;
        initUI();
    }

    private void initUI() {
        setTitle("Lost & Found Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 600);
        setLocationRelativeTo(null);  // Center on screen
        setResizable(false);

        // Main container with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, UITheme.PRIMARY,
                    0, getHeight(), UITheme.PRIMARY_LIGHT
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Top: Logo and App Title
        JPanel headerPanel = buildHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center: Login form
        loginPanel = buildLoginPanel();
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Bottom: Register link
        JPanel footerPanel = buildFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel buildHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        // App Icon (using emoji as text)
        JLabel iconLabel = new JLabel("🔍", JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Lost & Found System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("University Item Recovery Portal", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel buildLoginPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new util.RoundedBorder(UITheme.BORDER_COLOR, 1, 12),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        // Mode Toggle Buttons
        JPanel modePanel = new JPanel(new GridLayout(1, 2, 0, 0));
        modePanel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        studentModeBtn = new JButton("👤 Student");
        adminModeBtn = new JButton("🛡 Admin");
        styleToggleButton(studentModeBtn, true);
        styleToggleButton(adminModeBtn, false);

        studentModeBtn.addActionListener(e -> switchMode("STUDENT"));
        adminModeBtn.addActionListener(e -> switchMode("ADMIN"));

        modePanel.add(studentModeBtn);
        modePanel.add(adminModeBtn);

        gbc.gridy = 0;
        card.add(modePanel, gbc);

        // Mode Label
        modeLabel = new JLabel("Login as Student", JLabel.CENTER);
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modeLabel.setForeground(UITheme.PRIMARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 5, 0);
        card.add(modeLabel, gbc);

        // Email field
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 2, 0);
        card.add(createFieldLabel("Email Address"), gbc);

        emailField = UITheme.createTextField(20);
        emailField.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(emailField, gbc);

        // Password field
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 0, 2, 0);
        card.add(createFieldLabel("Password"), gbc);

        passwordField = UITheme.createPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(passwordField, gbc);

        // Login button
        JButton loginBtn = UITheme.createPrimaryButton("LOGIN");
        loginBtn.setPreferredSize(new Dimension(300, 42));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 0, 5, 0);
        card.add(loginBtn, gbc);
        loginBtn.addActionListener(e -> handleLogin());

        // Allow Enter key to submit
        getRootPane().setDefaultButton(loginBtn);
        passwordField.addActionListener(e -> handleLogin());

        // Hint text for default admin
        JLabel hintLabel = new JLabel("Default Admin: admin@university.edu / admin123");
        hintLabel.setFont(UITheme.FONT_SMALL);
        hintLabel.setForeground(UITheme.TEXT_MUTED);
        hintLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(hintLabel, gbc);

        container.add(card, BorderLayout.CENTER);
        return container;
    }

    private JPanel buildFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel registerLabel = new JLabel("New Student? ");
        registerLabel.setForeground(new Color(200, 220, 255));
        registerLabel.setFont(UITheme.FONT_LABEL);

        JButton registerBtn = new JButton("Register Here");
        registerBtn.setForeground(UITheme.ACCENT);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> openRegistration());

        panel.add(registerLabel);
        panel.add(registerBtn);
        return panel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.FONT_LABEL_BOLD);
        label.setForeground(UITheme.TEXT_DARK);
        return label;
    }

    private void styleToggleButton(JButton btn, boolean active) {
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        if (active) {
            btn.setBackground(UITheme.PRIMARY);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(UITheme.TEXT_DARK);
        }
    }

    private void switchMode(String mode) {
        this.loginMode = mode;
        if ("STUDENT".equals(mode)) {
            modeLabel.setText("Login as Student");
            styleToggleButton(studentModeBtn, true);
            styleToggleButton(adminModeBtn, false);
        } else {
            modeLabel.setText("Login as Admin");
            styleToggleButton(adminModeBtn, true);
            styleToggleButton(studentModeBtn, false);
        }
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = appManager.login(email, password, loginMode);

        if (user != null) {
            // Login successful - open appropriate dashboard
            if ("ADMIN".equals(loginMode)) {
                AdminDashboard adminDash = new AdminDashboard(appManager, this);
                adminDash.setVisible(true);
            } else {
                StudentDashboard studentDash = new StudentDashboard(appManager, this);
                studentDash.setVisible(true);
            }
            this.setVisible(false); // Hide login window
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password.\nMake sure you selected the correct login type.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openRegistration() {
        RegisterDialog dialog = new RegisterDialog(this, appManager);
        dialog.setVisible(true);
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        switchMode("STUDENT");
    }
}
