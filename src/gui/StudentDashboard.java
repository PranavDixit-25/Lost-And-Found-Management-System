package gui;

import manager.AppManager;
import model.*;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * StudentDashboard is the main window for logged-in students.
 * Uses JTabbedPane to organize features into tabs.
 *
 * Features:
 * - Add Lost/Found Item
 * - View All Items (with filtering)
 * - Claim an Item
 * - View My Claims
 */
public class StudentDashboard extends JFrame {

    private AppManager appManager;
    private JFrame loginFrame;

    // Tab references for refreshing
    private JTabbedPane tabbedPane;
    private JPanel viewItemsPanel;
    private JPanel myClaimsPanel;

    // Tables
    private JTable itemsTable;
    private DefaultTableModel itemsTableModel;
    private JTable myClaimsTable;
    private DefaultTableModel myClaimsTableModel;

    // Filter for items table
    private JComboBox<String> filterCombo;

    public StudentDashboard(AppManager appManager, JFrame loginFrame) {
        this.appManager = appManager;
        this.loginFrame = loginFrame;
        initUI();
        loadItemsTable("ALL");
        loadMyClaimsTable();
    }

    private void initUI() {
        String userName = appManager.getCurrentUser().getName();
        setTitle("Lost & Found System - Student: " + userName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        // Handle window close - ask confirmation
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleLogout();
            }
        });

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_LIGHT);

        // Top navigation bar
        mainPanel.add(buildNavBar(userName), BorderLayout.NORTH);

        // Tabbed content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_LABEL_BOLD);
        tabbedPane.setBackground(UITheme.BG_LIGHT);

        tabbedPane.addTab("  📦 View Items  ", buildViewItemsTab());
        tabbedPane.addTab("  ➕ Report Item  ", buildAddItemTab());
        tabbedPane.addTab("  📋 My Claims  ", buildMyClaimsTab());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel buildNavBar(String userName) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UITheme.PRIMARY);
        nav.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel appTitle = new JLabel("🔍 Lost & Found System");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appTitle.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("👤 " + userName);
        userLabel.setFont(UITheme.FONT_LABEL);
        userLabel.setForeground(new Color(200, 220, 255));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(UITheme.FONT_SMALL);
        logoutBtn.setForeground(UITheme.ACCENT);
        logoutBtn.setBackground(UITheme.PRIMARY);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());

        userPanel.add(userLabel);
        userPanel.add(new JSeparator(SwingConstants.VERTICAL));
        userPanel.add(logoutBtn);

        nav.add(appTitle, BorderLayout.WEST);
        nav.add(userPanel, BorderLayout.EAST);
        return nav;
    }

    // ==================== TAB 1: VIEW ITEMS ====================

    private JPanel buildViewItemsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top bar: Filter + Refresh + Claim button
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        JLabel filterLabel = UITheme.createBoldLabel("Filter:");
        filterCombo = UITheme.createComboBox(new String[]{"ALL", "LOST", "FOUND", "CLAIMED"});
        filterCombo.setPreferredSize(new Dimension(140, 32));
        filterCombo.addActionListener(e -> {
            String selected = (String) filterCombo.getSelectedItem();
            loadItemsTable(selected);
        });

        JButton refreshBtn = UITheme.createInfoButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> loadItemsTable((String) filterCombo.getSelectedItem()));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        filterPanel.add(refreshBtn);

        JButton claimBtn = UITheme.createSuccessButton("✋ Claim Selected Item");
        claimBtn.addActionListener(e -> handleClaimItem());

        topBar.add(filterPanel, BorderLayout.WEST);
        topBar.add(claimBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Items table
        String[] columns = {"Item ID", "Title", "Type", "Category", "Location", "Date", "Status", "Reported By"};
        itemsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        itemsTable = new JTable(itemsTableModel);
        UITheme.styleTable(itemsTable);

        // Set column widths
        int[] widths = {0, 200, 70, 100, 130, 90, 80, 130};
        for (int i = 0; i < widths.length; i++) {
            if (widths[i] == 0) {
                itemsTable.getColumnModel().getColumn(i).setMinWidth(0);
                itemsTable.getColumnModel().getColumn(i).setMaxWidth(0);
                itemsTable.getColumnModel().getColumn(i).setWidth(0);
            } else {
                itemsTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        }

        JScrollPane scroll = UITheme.createTableScrollPane(itemsTable);
        panel.add(scroll, BorderLayout.CENTER);

        // Detail view on double-click
        itemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showItemDetail();
                }
            }
        });

        // Info label
        JLabel hint = new JLabel("Double-click an item to see full details. Select and click 'Claim' to submit a claim.");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);
        panel.add(hint, BorderLayout.SOUTH);

        viewItemsPanel = panel;
        return panel;
    }

    private void loadItemsTable(String filter) {
        itemsTableModel.setRowCount(0);

        List<Item> items;
        switch (filter) {
            case "LOST":
                items = appManager.getItemsByType(Item.ItemType.LOST);
                break;
            case "FOUND":
                items = appManager.getItemsByType(Item.ItemType.FOUND);
                break;
            case "CLAIMED":
                items = appManager.getClaimedItems();
                break;
            default:
                items = appManager.getActiveItems();
        }

        for (Item item : items) {
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

    private void showItemDetail() {
        int row = itemsTable.getSelectedRow();
        if (row < 0) return;

        String itemId = (String) itemsTableModel.getValueAt(row, 0);
        Item item = appManager.findItemById(itemId);
        if (item == null) return;

        // Build detail message
        String detail = String.format(
            "ITEM DETAILS\n" +
            "═══════════════════════════════\n" +
            "Title:       %s\n" +
            "Type:        %s\n" +
            "Category:    %s\n" +
            "Location:    %s\n" +
            "Date:        %s\n" +
            "Status:      %s\n\n" +
            "Description:\n%s\n\n" +
            "Reported By: %s\n" +
            "Contact:     %s",
            item.getTitle(),
            item.getType().name(),
            item.getCategory(),
            item.getLocation(),
            item.getDateReported(),
            item.getStatus().name(),
            item.getDescription(),
            item.getReportedByName(),
            item.getContactInfo()
        );

        JTextArea textArea = new JTextArea(detail);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(420, 320));

        JOptionPane.showMessageDialog(this, scroll, "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleClaimItem() {
        int row = itemsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an item from the table to claim.",
                "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemId = (String) itemsTableModel.getValueAt(row, 0);
        String status = (String) itemsTableModel.getValueAt(row, 6);
        String title = (String) itemsTableModel.getValueAt(row, 1);

        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This item cannot be claimed. It is already: " + status,
                "Cannot Claim", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Open claim dialog
        ClaimDialog dialog = new ClaimDialog(this, appManager, itemId, title);
        dialog.setVisible(true);

        // Refresh after claim
        loadItemsTable((String) filterCombo.getSelectedItem());
        loadMyClaimsTable();
    }

    // ==================== TAB 2: ADD ITEM ====================

    private JPanel buildAddItemTab() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UITheme.BG_LIGHT);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel formTitle = UITheme.createTitleLabel("📦 Report Lost / Found Item");
        GridBagConstraints gbc = UITheme.createGBC(0, 0, 2, false);
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(formTitle, gbc);

        // Item Type selection
        gbc = UITheme.createGBC(0, 1, 1, false);
        card.add(UITheme.createBoldLabel("Item Type *"), gbc);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typePanel.setOpaque(false);
        JRadioButton lostRadio = new JRadioButton("🔴 LOST Item");
        JRadioButton foundRadio = new JRadioButton("🟢 FOUND Item");
        lostRadio.setFont(UITheme.FONT_LABEL);
        foundRadio.setFont(UITheme.FONT_LABEL);
        lostRadio.setOpaque(false);
        foundRadio.setOpaque(false);
        lostRadio.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(lostRadio);
        typeGroup.add(foundRadio);
        typePanel.add(lostRadio);
        typePanel.add(Box.createHorizontalStrut(20));
        typePanel.add(foundRadio);

        gbc = UITheme.createGBC(1, 1, 1, true);
        card.add(typePanel, gbc);

        // Title
        gbc = UITheme.createGBC(0, 2, 1, false);
        gbc.insets = new Insets(12, 0, 2, 0);
        card.add(UITheme.createBoldLabel("Title *"), gbc);
        JTextField titleField = UITheme.createTextField(25);
        gbc = UITheme.createGBC(1, 2, 1, true);
        gbc.insets = new Insets(12, 5, 2, 0);
        card.add(titleField, gbc);

        // Category
        gbc = UITheme.createGBC(0, 3, 1, false);
        gbc.insets = new Insets(8, 0, 2, 0);
        card.add(UITheme.createBoldLabel("Category"), gbc);
        JComboBox<String> categoryCombo = UITheme.createComboBox(new String[]{
            "Electronics", "Documents/Cards", "Clothing", "Accessories",
            "Books/Notes", "Keys", "Water Bottle/Tiffin", "Money/Wallet", "Sports Equipment", "Other"
        });
        gbc = UITheme.createGBC(1, 3, 1, true);
        gbc.insets = new Insets(8, 5, 2, 0);
        card.add(categoryCombo, gbc);

        // Location
        gbc = UITheme.createGBC(0, 4, 1, false);
        gbc.insets = new Insets(8, 0, 2, 0);
        card.add(UITheme.createBoldLabel("Location *"), gbc);
        JTextField locationField = UITheme.createTextField(25);
        locationField.setToolTipText("e.g., Library 2nd Floor, Canteen, Lab B201");
        gbc = UITheme.createGBC(1, 4, 1, true);
        gbc.insets = new Insets(8, 5, 2, 0);
        card.add(locationField, gbc);

        // Contact Info
        gbc = UITheme.createGBC(0, 5, 1, false);
        gbc.insets = new Insets(8, 0, 2, 0);
        card.add(UITheme.createBoldLabel("Contact Info"), gbc);
        JTextField contactField = UITheme.createTextField(25);
        contactField.setToolTipText("Phone or email to reach you");
        gbc = UITheme.createGBC(1, 5, 1, true);
        gbc.insets = new Insets(8, 5, 2, 0);
        card.add(contactField, gbc);

        // Description
        gbc = UITheme.createGBC(0, 6, 2, false);
        gbc.insets = new Insets(12, 0, 2, 0);
        card.add(UITheme.createBoldLabel("Description * (be specific - color, brand, distinguishing features)"), gbc);

        JTextArea descArea = UITheme.createTextArea(4, 40);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        descScroll.setPreferredSize(new Dimension(500, 100));
        gbc = UITheme.createGBC(0, 7, 2, true);
        card.add(descScroll, gbc);

        // Submit Button
        JButton submitBtn = UITheme.createPrimaryButton("📤 Submit Report");
        submitBtn.setPreferredSize(new Dimension(180, 42));
        gbc = UITheme.createGBC(1, 8, 1, false);
        gbc.insets = new Insets(20, 5, 0, 0);
        gbc.anchor = GridBagConstraints.EAST;
        card.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> {
            String title2 = titleField.getText().trim();
            String desc = descArea.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String location = locationField.getText().trim();
            String contact = contactField.getText().trim();
            Item.ItemType type = lostRadio.isSelected() ? Item.ItemType.LOST : Item.ItemType.FOUND;

            String error = appManager.addItem(title2, desc, category, location, type, contact);
            if (error == null) {
                JOptionPane.showMessageDialog(this,
                    "✅ Your item has been reported successfully!\nOthers can now see it in the items list.",
                    "Item Reported", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields
                titleField.setText(""); descArea.setText(""); locationField.setText("");
                contactField.setText(""); lostRadio.setSelected(true);
                // Refresh view items tab
                loadItemsTable((String) filterCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + error,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        outerPanel.add(card, BorderLayout.CENTER);
        return outerPanel;
    }

    // ==================== TAB 3: MY CLAIMS ====================

    private JPanel buildMyClaimsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JLabel title = UITheme.createSubtitleLabel("My Claim Requests");
        JButton refreshBtn = UITheme.createInfoButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> loadMyClaimsTable());
        topBar.add(title, BorderLayout.WEST);
        topBar.add(refreshBtn, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"Claim ID", "Item Title", "Date Claimed", "Status", "Admin Note"};
        myClaimsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        myClaimsTable = new JTable(myClaimsTableModel);
        UITheme.styleTable(myClaimsTable);

        // Hide Claim ID column
        myClaimsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        myClaimsTable.getColumnModel().getColumn(0).setWidth(0);
        myClaimsTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        myClaimsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        myClaimsTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        myClaimsTable.getColumnModel().getColumn(4).setPreferredWidth(250);

        // Color-code status
        myClaimsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String status = (String) myClaimsTableModel.getValueAt(row, 3);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UITheme.TABLE_STRIPE);
                    if ("APPROVED".equals(status)) c.setForeground(UITheme.SUCCESS);
                    else if ("REJECTED".equals(status)) c.setForeground(UITheme.DANGER);
                    else c.setForeground(UITheme.WARNING);
                    if (col != 3) c.setForeground(UITheme.TEXT_DARK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        JScrollPane scroll = UITheme.createTableScrollPane(myClaimsTable);
        panel.add(scroll, BorderLayout.CENTER);

        JLabel hint = new JLabel("Here you can track the status of all your claim requests.");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);
        panel.add(hint, BorderLayout.SOUTH);

        myClaimsPanel = panel;
        return panel;
    }

    private void loadMyClaimsTable() {
        myClaimsTableModel.setRowCount(0);
        for (ClaimRequest claim : appManager.getMyClaims()) {
            myClaimsTableModel.addRow(new Object[]{
                claim.getClaimId(),
                claim.getItemTitle(),
                claim.getDateClaimed(),
                claim.getStatus().name(),
                claim.getAdminNote()
            });
        }
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
