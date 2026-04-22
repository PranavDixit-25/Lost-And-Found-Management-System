package gui;

import manager.AppManager;
import util.UITheme;

import javax.swing.*;
import java.awt.*;

/**
 * ClaimDialog - modal dialog for submitting a claim request.
 * Opened when a student selects an item and clicks "Claim".
 */
public class ClaimDialog extends JDialog {

    private AppManager appManager;
    private String itemId;
    private String itemTitle;
    private JTextArea reasonArea;

    public ClaimDialog(JFrame parent, AppManager appManager, String itemId, String itemTitle) {
        super(parent, "Submit Claim Request", true);
        this.appManager = appManager;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        initUI();
    }

    private void initUI() {
        setSize(460, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.SUCCESS);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("✋ Claim Item");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = UITheme.createGBC(0, 0, 1, false);
        content.add(UITheme.createBoldLabel("Item:"), gbc);

        gbc = UITheme.createGBC(1, 0, 1, true);
        JLabel itemLabel = new JLabel(itemTitle);
        itemLabel.setFont(UITheme.FONT_LABEL);
        itemLabel.setForeground(UITheme.PRIMARY);
        content.add(itemLabel, gbc);

        // User info display
        String user = appManager.getCurrentUser().getName();
        String email = appManager.getCurrentUser().getEmail();

        gbc = UITheme.createGBC(0, 1, 1, false);
        gbc.insets = new Insets(10, 0, 2, 0);
        content.add(UITheme.createBoldLabel("Claimant:"), gbc);

        gbc = UITheme.createGBC(1, 1, 1, true);
        gbc.insets = new Insets(10, 5, 2, 0);
        content.add(UITheme.createLabel(user + " (" + email + ")"), gbc);

        // Reason area
        gbc = UITheme.createGBC(0, 2, 2, false);
        gbc.insets = new Insets(15, 0, 5, 0);
        JLabel reasonLabel = UITheme.createBoldLabel("Why do you think this item is yours? *");
        content.add(reasonLabel, gbc);

        JLabel hintLabel = new JLabel("Provide specific details (color, markings, where you lost it, etc.)");
        hintLabel.setFont(UITheme.FONT_SMALL);
        hintLabel.setForeground(UITheme.TEXT_MUTED);
        gbc = UITheme.createGBC(0, 3, 2, false);
        content.add(hintLabel, gbc);

        reasonArea = UITheme.createTextArea(5, 35);
        JScrollPane scroll = new JScrollPane(reasonArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        scroll.setPreferredSize(new Dimension(380, 120));
        gbc = UITheme.createGBC(0, 4, 2, true);
        gbc.insets = new Insets(5, 0, 0, 0);
        content.add(scroll, gbc);

        main.add(content, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnPanel.setBackground(UITheme.BG_LIGHT);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton submitBtn = UITheme.createSuccessButton("Submit Claim");

        cancelBtn.addActionListener(e -> dispose());
        submitBtn.addActionListener(e -> handleSubmit());

        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void handleSubmit() {
        String reason = reasonArea.getText().trim();
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please describe why you think this item is yours.",
                "Required Field", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm before submitting
        int confirm = JOptionPane.showConfirmDialog(this,
            "Submit claim for: \"" + itemTitle + "\"?\n" +
            "An admin will review your claim and notify you.",
            "Confirm Claim", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        String error = appManager.submitClaim(itemId, reason);
        if (error == null) {
            JOptionPane.showMessageDialog(this,
                "✅ Claim submitted successfully!\n" +
                "Check 'My Claims' tab to track its status.",
                "Claim Submitted", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + error,
                "Claim Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
