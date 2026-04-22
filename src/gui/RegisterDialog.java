package gui;

import manager.AppManager;
import util.UITheme;

import javax.swing.*;
import java.awt.*;

/**
 * RegisterDialog - popup dialog for new student registration.
 * Uses JDialog (modal) so it blocks interaction with parent window.
 */
public class RegisterDialog extends JDialog {

    private AppManager appManager;

    private JTextField nameField, emailField, phoneField;
    private JTextField studentIdField, departmentField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> yearCombo;

    public RegisterDialog(JFrame parent, AppManager appManager) {
        super(parent, "Student Registration", true);  // modal = true
        this.appManager = appManager;
        initUI();
    }

    private void initUI() {
        setSize(480, 580);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG_LIGHT);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("📝 Create Student Account");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(Color.WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = buildForm();
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        main.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UITheme.BG_LIGHT);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton registerBtn = UITheme.createSuccessButton("Register");

        cancelBtn.addActionListener(e -> dispose());
        registerBtn.addActionListener(e -> handleRegister());

        btnPanel.add(cancelBtn);
        btnPanel.add(registerBtn);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        int row = 0;

        // Full Name
        addFormRow(panel, "Full Name *", row++);
        nameField = UITheme.createTextField(20);
        addFieldRow(panel, nameField, row++);

        // Email
        addFormRow(panel, "Email Address *", row++);
        emailField = UITheme.createTextField(20);
        addFieldRow(panel, emailField, row++);

        // Student ID
        addFormRow(panel, "Student ID *", row++);
        studentIdField = UITheme.createTextField(20);
        addFieldRow(panel, studentIdField, row++);

        // Department
        addFormRow(panel, "Department *", row++);
        departmentField = UITheme.createTextField(20);
        addFieldRow(panel, departmentField, row++);

        // Year of Study
        addFormRow(panel, "Year of Study", row++);
        yearCombo = UITheme.createComboBox(new String[]{"1st Year", "2nd Year", "3rd Year", "4th Year", "5th Year"});
        GridBagConstraints gbc = UITheme.createGBC(0, row++, 2, true);
        panel.add(yearCombo, gbc);

        // Phone
        addFormRow(panel, "Phone Number", row++);
        phoneField = UITheme.createTextField(20);
        addFieldRow(panel, phoneField, row++);

        // Password
        addFormRow(panel, "Password * (min 6 chars)", row++);
        passwordField = UITheme.createPasswordField(20);
        addFieldRow(panel, passwordField, row++);

        // Confirm Password
        addFormRow(panel, "Confirm Password *", row++);
        confirmPasswordField = UITheme.createPasswordField(20);
        addFieldRow(panel, confirmPasswordField, row++);

        // Required note
        JLabel reqNote = new JLabel("* Required fields");
        reqNote.setFont(UITheme.FONT_SMALL);
        reqNote.setForeground(UITheme.TEXT_MUTED);
        GridBagConstraints gbcNote = UITheme.createGBC(0, row, 2, false);
        gbcNote.insets = new Insets(10, 0, 0, 0);
        panel.add(reqNote, gbcNote);

        return panel;
    }

    private void addFormRow(JPanel panel, String labelText, int row) {
        JLabel label = UITheme.createBoldLabel(labelText);
        GridBagConstraints gbc = UITheme.createGBC(0, row, 2, false);
        gbc.insets = new Insets(10, 0, 2, 0);
        panel.add(label, gbc);
    }

    private void addFieldRow(JPanel panel, JComponent field, int row) {
        field.setPreferredSize(new Dimension(380, 36));
        GridBagConstraints gbc = UITheme.createGBC(0, row, 2, true);
        panel.add(field, gbc);
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String department = departmentField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        int year = yearCombo.getSelectedIndex() + 1;

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match. Please try again.",
                "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.setText("");
            return;
        }

        if (department.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your department.", "Missing Field", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Attempt registration
        String error = appManager.registerStudent(name, email, password, phone,
                                                   studentId, department, year);

        if (error == null) {
            // Registration successful
            JOptionPane.showMessageDialog(this,
                "✅ Registration successful!\nYou can now log in with your email and password.",
                "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed: " + error,
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
