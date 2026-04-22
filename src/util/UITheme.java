package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * UITheme provides consistent styling constants and helper methods
 * for all GUI components in the application.
 * Centralizing design here makes it easy to change the look globally.
 */
public class UITheme {

    // ==================== COLOR PALETTE ====================
    public static final Color PRIMARY       = new Color(25, 55, 109);    // Deep navy blue
    public static final Color PRIMARY_LIGHT = new Color(41, 90, 168);    // Medium blue
    public static final Color ACCENT        = new Color(255, 165, 0);    // Gold/amber
    public static final Color SUCCESS       = new Color(39, 174, 96);    // Green
    public static final Color DANGER        = new Color(192, 57, 43);    // Red
    public static final Color WARNING       = new Color(243, 156, 18);   // Orange
    public static final Color INFO          = new Color(52, 152, 219);   // Sky blue
    public static final Color LOST_COLOR    = new Color(220, 53, 69);    // Red for Lost
    public static final Color FOUND_COLOR   = new Color(40, 167, 69);    // Green for Found
    public static final Color CLAIMED_COLOR = new Color(108, 117, 125);  // Grey for Claimed

    public static final Color BG_LIGHT      = new Color(245, 247, 250);  // Off-white background
    public static final Color BG_PANEL      = Color.WHITE;
    public static final Color TEXT_DARK     = new Color(33, 37, 41);
    public static final Color TEXT_MUTED    = new Color(108, 117, 125);
    public static final Color BORDER_COLOR  = new Color(222, 226, 230);
    public static final Color TABLE_STRIPE  = new Color(248, 249, 250);
    public static final Color TABLE_HEADER  = new Color(25, 55, 109);

    // ==================== FONTS ====================
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE  = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_LABEL     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL_BOLD= new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BUTTON    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_STAT_NUM  = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_STAT_LBL  = new Font("Segoe UI", Font.PLAIN, 12);

    // ==================== BUTTON FACTORY METHODS ====================

    public static JButton createPrimaryButton(String text) {
        return styleButton(new JButton(text), PRIMARY, Color.WHITE);
    }

    public static JButton createSuccessButton(String text) {
        return styleButton(new JButton(text), SUCCESS, Color.WHITE);
    }

    public static JButton createDangerButton(String text) {
        return styleButton(new JButton(text), DANGER, Color.WHITE);
    }

    public static JButton createWarningButton(String text) {
        return styleButton(new JButton(text), WARNING, Color.WHITE);
    }

    public static JButton createInfoButton(String text) {
        return styleButton(new JButton(text), INFO, Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        return styleButton(new JButton(text), new Color(108, 117, 125), Color.WHITE);
    }

    private static JButton styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BUTTON);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Hover effect
        Color hoverColor = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    // ==================== LABEL FACTORY ====================

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY);
        return label;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL_BOLD);
        label.setForeground(TEXT_DARK);
        return label;
    }

    // ==================== FIELD FACTORY ====================

    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(FONT_LABEL);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return area;
    }

    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(FONT_LABEL);
        combo.setBackground(Color.WHITE);
        return combo;
    }

    // ==================== TABLE STYLING ====================

    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 227, 252));
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(BG_PANEL);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL_BOLD);
        header.setBackground(TABLE_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Center-align renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? BG_PANEL : TABLE_STRIPE);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // ==================== PANEL FACTORY ====================

    public static JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(PRIMARY_LIGHT, 1, 8),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return panel;
    }

    public static JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    public static GridBagConstraints createGBC(int x, int y, int width, boolean fillH) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.fill = fillH ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = fillH ? 1.0 : 0;
        return gbc;
    }

    /**
     * Create a stat card for the dashboard.
     */
    public static JPanel createStatCard(String value, String label, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, color),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));

        JLabel valLabel = new JLabel(value, JLabel.CENTER);
        valLabel.setFont(FONT_STAT_NUM);
        valLabel.setForeground(color);

        JLabel lblLabel = new JLabel(label, JLabel.CENTER);
        lblLabel.setFont(FONT_STAT_LBL);
        lblLabel.setForeground(TEXT_MUTED);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Create a scrollable table pane with consistent styling.
     */
    public static JScrollPane createTableScrollPane(JTable table) {
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(BG_PANEL);
        return scroll;
    }

    /**
     * Create a section header panel.
     */
    public static JPanel createSectionHeader(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(PRIMARY);
        JLabel label = new JLabel(title);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    // ==================== SET LOOK AND FEEL ====================
    public static void applyLookAndFeel() {
        try {
            // Use system look and feel, or Nimbus for a cleaner look
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fall back to default
        }
    }
}
