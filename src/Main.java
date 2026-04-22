import gui.LoginFrame;
import manager.AppManager;
import util.UITheme;

import javax.swing.*;

/**
 * Main.java - The entry point of the Lost & Found Management System.
 *
 * This is where the application starts.
 * It initializes the AppManager (loads data from files),
 * then launches the GUI on the Event Dispatch Thread (EDT).
 *
 * OOP CONCEPTS DEMONSTRATED IN THIS PROJECT:
 * ==========================================
 * 1. ENCAPSULATION   → All model fields are private with getters/setters
 *                       (User, Item, ClaimRequest classes)
 *
 * 2. INHERITANCE     → Student and Admin extend User
 *                       Both IS-A User, but with additional fields
 *
 * 3. POLYMORPHISM    → getRole(), hasPermission(), toString() are
 *                       overridden differently in Student vs Admin
 *
 * 4. ABSTRACTION     → Actionable interface defines WHAT must be done
 *                       without specifying HOW (each class implements it)
 *
 * 5. ASSOCIATION     → User ↔ Item ↔ ClaimRequest are linked by IDs
 *                       ClaimRequest references both User and Item
 *
 * ARCHITECTURE:
 * ============
 * GUI Layer       → LoginFrame, StudentDashboard, AdminDashboard, Dialogs
 * Manager Layer   → AppManager (business logic), DataManager (file I/O)
 * Model Layer     → User, Student, Admin, Item, ClaimRequest
 * Utility Layer   → UITheme, IDGenerator, JsonParser, RoundedBorder
 * Interface Layer → Actionable
 */
public class Main {

    public static void main(String[] args) {
        // Set Look and Feel before creating any GUI components
        UITheme.applyLookAndFeel();

        // Run GUI on the Event Dispatch Thread (EDT) - Swing best practice
        SwingUtilities.invokeLater(() -> {
            System.out.println("Starting Lost & Found Management System...");

            // Initialize AppManager - loads all data from JSON files
            AppManager appManager = new AppManager();

            // Create and show the Login window
            LoginFrame loginFrame = new LoginFrame(appManager);
            loginFrame.setVisible(true);

            System.out.println("Application started successfully.");
        });
    }
}
