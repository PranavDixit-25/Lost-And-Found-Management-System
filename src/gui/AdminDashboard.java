package gui;

import manager.AppManager;
import model.*;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AdminDashboard is the main window for logged-in Admin users.
 * Provides full control over items and claims.
 *
 * Features:
 * - Dashboard statistics overview
 * - View and manage all items
 * - View and process all claim requests
 * - Approve / Reject claims
 * - Remove items
 */
public class AdminDashboard extends JFrame {

    private AppManager appManager;
    private JFrame loginFrame;

    // Items management
    private JTable itemsTable;
    private DefaultTableModel itemsTableModel;

    // Claims management
    private JTable claimsTable;
    private DefaultTableModel claimsTableModel;

    // Dashboard stats labels
    private JLabel statTotal, statLost, statFound, statClaimed, statClaims, statUsers;

    public AdminDashboard(AppManager appManager, JFrame loginFrame) {
        this.appManager = appManager;
        this.loginFrame = loginFrame;
        initUI();
        refreshAllData();
    }

    private void initUI() {
        String adminName = appManager.getCurrentUser().getName();
        setTitle("Lost & Found System - Admin Panel: " + adminName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleLogout();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_LIGHT);

        mainPanel.add(buildNavBar(adminName), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_LABEL_BOLD);

        tabs.addTab("  📊 Dashboard  ", buildDashboardTab());
        tabs.addTab("  📦 Manage Items  ", buildManageItemsTab());
        tabs.addTab("  📋 Manage Claims  ", buildManageClaimsTab());

        mainPanel.add(tabs, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel buildNavBar(String adminName) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(new Color(20, 40, 90));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel appTitle = new JLabel("🛡 Lost & Found - Admin Panel");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appTitle.setForeground(UITheme.ACCENT);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("🛡 Admin: " + adminName);
        userLabel.setFont(UITheme.FONT_LABEL);
        userLabel.setForeground(new Color(200, 220, 255));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(UITheme.FONT_SMALL);
        logoutBtn.setForeground(UITheme.ACCENT);
        logoutBtn.setBackground(new Color(20, 40, 90));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);

        nav.add(appTitle, BorderLayout.WEST);
        nav.add(userPanel, BorderLayout.EAST);
        return nav;
    }

    // ==================== TAB 1: DASHBOARD ====================

    private JPanel buildDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcome = UITheme.createTitleLabel("📊 System Overview");
        panel.add(welcome, BorderLayout.NORTH);

        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        statsGrid.setOpaque(false);

        statTotal   = new JLabel("0", JLabel.CENTER);
        statLost    = new JLabel("0", JLabel.CENTER);
        statFound   = new JLabel("0", JLabel.CENTER);
        statClaimed = new JLabel("0", JLabel.CENTER);
        statClaims  = new JLabel("0", JLabel.CENTER);
        statUsers   = new JLabel("0", JLabel.CENTER);

        statsGrid.add(makeStatCard(statTotal, "Total Items", UITheme.PRIMARY));
        statsGrid.add(makeStatCard(statLost, "Lost Items", UITheme.LOST_COLOR));
        statsGrid.add(makeStatCard(statFound, "Found Items", UITheme.FOUND_COLOR));
        statsGrid.add(makeStatCard(statClaimed, "Claimed Items", UITheme.CLAIMED_COLOR));
        statsGrid.add(makeStatCard(statClaims, "Pending Claims", UITheme.WARNING));
        statsGrid.add(makeStatCard(statUsers, "Registered Users", UITheme.INFO));

        panel.add(statsGrid, BorderLayout.CENTER);

        // Recent activity hint
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JLabel hint = new JLabel("Switch to 'Manage Items' or 'Manage Claims' tabs for detailed management.");
        hint.setFont(UITheme.FONT_LABEL);
        hint.setForeground(UITheme.TEXT_MUTED);
        hint.setHorizontalAlignment(JLabel.CENTER);

        JButton refreshBtn = UITheme.createPrimaryButton("🔄 Refresh Statistics");
        refreshBtn.addActionListener(e -> refreshStats());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);

        bottomPanel.add(hint, BorderLayout.NORTH);
        bottomPanel.add(btnPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel makeStatCard(JLabel valueLabel, String labelText, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, color),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
            )
        ));

        valueLabel.setFont(UITheme.FONT_STAT_NUM);
        valueLabel.setForeground(color);

        JLabel label = new JLabel(labelText, JLabel.CENTER);
        label.setFont(UITheme.FONT_STAT_LBL);
        label.setForeground(UITheme.TEXT_MUTED);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(label, BorderLayout.SOUTH);
        return card;
    }

    private void refreshStats() {
        statTotal.setText(String.valueOf(appManager.getTotalItemsCount()));
        statLost.setText(String.valueOf(appManager.getLostItemsCount()));
        statFound.setText(String.valueOf(appManager.getFoundItemsCount()));
        statClaimed.setText(String.valueOf(appManager.getClaimedItemsCount()));
        statClaims.setText(String.valueOf(appManager.getPendingClaimsCount()));
        statUsers.setText(String.valueOf(appManager.getTotalUsersCount()));
    }

    // ==================== TAB 2: MANAGE ITEMS ====================

    private JPanel buildManageItemsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        JLabel title = UITheme.createSubtitleLabel("All Items");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn = UITheme.createInfoButton("🔄 Refresh");
        JButton removeBtn  = UITheme.createDangerButton("🗑 Remove Item");
        JButton detailBtn  = UITheme.createPrimaryButton("📄 View Detail");

        refreshBtn.addActionListener(e -> loadItemsTable());
        removeBtn.addActionListener(e -> handleRemoveItem());
        detailBtn.addActionListener(e -> showItemDetail());

        btnPanel.add(detailBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(refreshBtn);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Item ID", "Title", "Type", "Category", "Location", "Date", "Status", "Reported By"};
        itemsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        itemsTable = new JTable(itemsTableModel);
        UITheme.styleTable(itemsTable);

        // Column widths
        itemsTable.getColumnModel().getColumn(0).setMinWidth(0);
        itemsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        itemsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        itemsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        itemsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        itemsTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        itemsTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        itemsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        itemsTable.getColumnModel().getColumn(7).setPreferredWidth(120);

        panel.add(UITheme.createTableScrollPane(itemsTable), BorderLayout.CENTER);

        JLabel hint = new JLabel("Select an item and click 'Remove Item' to mark it as removed. Double-click to view details.");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);
        panel.add(hint, BorderLayout.SOUTH);

        return panel;
    }

    private void loadItemsTable() {
        itemsTableModel.setRowCount(0);
        for (Item item : appManager.getAllItems()) {
            if (item.getStatus() != Item.ItemStatus.REMOVED) {
                itemsTableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getTitle(),
                    item.getType().name(),
                    item.getCategory(),
                    item.getLocation(),
                    item.getDateReported(),
                    item.getStatus().name(),
                    item.getReportedByName()
                });
            }
        }
    }

    private void handleRemoveItem() {
        int row = itemsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemId = (String) itemsTableModel.getValueAt(row, 0);
        String title = (String) itemsTableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Remove item: \"" + title + "\"?\nThis action cannot be undone.",
            "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String error = appManager.removeItem(itemId);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "✅ Item removed successfully.",
                    "Item Removed", JOptionPane.INFORMATION_MESSAGE);
                loadItemsTable();
                refreshStats();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + error,
                    "Remove Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showItemDetail() {
        int row = itemsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String itemId = (String) itemsTableModel.getValueAt(row, 0);
        Item item = appManager.findItemById(itemId);
        if (item == null) return;

        String detail = String.format(
            "Title:       %s\nType:        %s\nCategory:    %s\n" +
            "Location:    %s\nDate:        %s\nStatus:      %s\n\n" +
            "Description:\n%s\n\nReported By: %s\nContact:     %s",
            item.getTitle(), item.getType().name(), item.getCategory(),
            item.getLocation(), item.getDateReported(), item.getStatus().name(),
            item.getDescription(), item.getReportedByName(), item.getContactInfo()
        );

        JTextArea ta = new JTextArea(detail);
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(420, 300));
        JOptionPane.showMessageDialog(this, sp, "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== TAB 3: MANAGE CLAIMS ====================

    private JPanel buildManageClaimsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        JLabel title = UITheme.createSubtitleLabel("All Claim Requests");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn  = UITheme.createInfoButton("🔄 Refresh");
        JButton approveBtn  = UITheme.createSuccessButton("✅ Approve");
        JButton rejectBtn   = UITheme.createDangerButton("❌ Reject");

        refreshBtn.addActionListener(e -> loadClaimsTable());
        approveBtn.addActionListener(e -> handleApproveClaim());
        rejectBtn.addActionListener(e -> handleRejectClaim());

        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        btnPanel.add(refreshBtn);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Claims table
        String[] cols = {"Claim ID", "Item Title", "Claimant", "Email", "Phone", "Date", "Status", "Admin Note"};
        claimsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        claimsTable = new JTable(claimsTableModel);
        UITheme.styleTable(claimsTable);

        // Column widths
        claimsTable.getColumnModel().getColumn(0).setMinWidth(0);
        claimsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        claimsTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        claimsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        claimsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        claimsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        claimsTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        claimsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        claimsTable.getColumnModel().getColumn(7).setPreferredWidth(200);

        // Color-code status
        claimsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String status = claimsTableModel.getRowCount() > row
                    ? (String) claimsTableModel.getValueAt(row, 6) : "";
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UITheme.TABLE_STRIPE);
                    c.setForeground(UITheme.TEXT_DARK);
                    if (col == 6) {
                        if ("APPROVED".equals(status)) c.setForeground(UITheme.SUCCESS);
                        else if ("REJECTED".equals(status)) c.setForeground(UITheme.DANGER);
                        else c.setForeground(UITheme.WARNING);
                    }
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        // Double-click to see full claim description
        claimsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) showClaimDetail();
            }
        });

        panel.add(UITheme.createTableScrollPane(claimsTable), BorderLayout.CENTER);

        JLabel hint = new JLabel(
            "Select a PENDING claim and click 'Approve' or 'Reject'. Double-click to see full claim reason.");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);
        panel.add(hint, BorderLayout.SOUTH);

        return panel;
    }

    private void loadClaimsTable() {
        claimsTableModel.setRowCount(0);
        for (ClaimRequest claim : appManager.getAllClaims()) {
            claimsTableModel.addRow(new Object[]{
                claim.getClaimId(),
                claim.getItemTitle(),
                claim.getClaimantName(),
                claim.getClaimantEmail(),
                claim.getClaimantPhone(),
                claim.getDateClaimed(),
                claim.getStatus().name(),
                claim.getAdminNote()
            });
        }
    }

    private void showClaimDetail() {
        int row = claimsTable.getSelectedRow();
        if (row < 0) return;

        String claimId = (String) claimsTableModel.getValueAt(row, 0);
        ClaimRequest claim = appManager.findClaimById(claimId);
        if (claim == null) return;

        String detail = String.format(
            "CLAIM DETAILS\n" +
            "═══════════════════════════════════\n" +
            "Claim ID:    %s\n" +
            "Item:        %s\n" +
            "Claimant:    %s\n" +
            "Email:       %s\n" +
            "Phone:       %s\n" +
            "Date:        %s\n" +
            "Status:      %s\n\n" +
            "Claim Reason:\n%s\n\n" +
            "Admin Note:\n%s",
            claim.getClaimId(),
            claim.getItemTitle(),
            claim.getClaimantName(),
            claim.getClaimantEmail(),
            claim.getClaimantPhone(),
            claim.getDateClaimed(),
            claim.getStatus().name(),
            claim.getClaimDescription(),
            claim.getAdminNote().isEmpty() ? "(none)" : claim.getAdminNote()
        );

        JTextArea ta = new JTextArea(detail);
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(450, 340));
        JOptionPane.showMessageDialog(this, sp, "Claim Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleApproveClaim() {
        int row = claimsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a claim to approve.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) claimsTableModel.getValueAt(row, 6);
        if (!"PENDING".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only PENDING claims can be approved.",
                "Cannot Approve", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String claimId = (String) claimsTableModel.getValueAt(row, 0);
        String claimant = (String) claimsTableModel.getValueAt(row, 2);
        String item = (String) claimsTableModel.getValueAt(row, 1);

        String note = JOptionPane.showInputDialog(this,
            "Approve claim by \"" + claimant + "\" for \"" + item + "\"?\n" +
            "Enter optional note for the student:",
            "Approve Claim", JOptionPane.QUESTION_MESSAGE);

        if (note == null) return; // User cancelled

        String error = appManager.approveClaim(claimId, note.trim());
        if (error == null) {
            JOptionPane.showMessageDialog(this,
                "✅ Claim APPROVED!\nThe item is now marked as claimed.\n" +
                "All other pending claims for this item have been rejected.",
                "Claim Approved", JOptionPane.INFORMATION_MESSAGE);
            refreshAllData();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + error,
                "Approve Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRejectClaim() {
        int row = claimsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a claim to reject.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) claimsTableModel.getValueAt(row, 6);
        if (!"PENDING".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only PENDING claims can be rejected.",
                "Cannot Reject", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String claimId = (String) claimsTableModel.getValueAt(row, 0);
        String claimant = (String) claimsTableModel.getValueAt(row, 2);
        String item = (String) claimsTableModel.getValueAt(row, 1);

        String note = JOptionPane.showInputDialog(this,
            "Reject claim by \"" + claimant + "\" for \"" + item + "\"?\n" +
            "Enter reason for rejection (required):",
            "Reject Claim", JOptionPane.QUESTION_MESSAGE);

        if (note == null || note.trim().isEmpty()) {
            if (note != null) {
                JOptionPane.showMessageDialog(this, "Please provide a reason for rejection.",
                    "Reason Required", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }

        String error = appManager.rejectClaim(claimId, note.trim());
        if (error == null) {
            JOptionPane.showMessageDialog(this,
                "❌ Claim REJECTED.\nThe student will be notified.",
                "Claim Rejected", JOptionPane.INFORMATION_MESSAGE);
            refreshAllData();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + error,
                "Reject Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllData() {
        refreshStats();
        loadItemsTable();
        loadClaimsTable();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            appManager.logout();
            loginFrame.setVisible(true);
            ((LoginFrame) loginFrame).clearFields();
            dispose();
        }
    }
}
