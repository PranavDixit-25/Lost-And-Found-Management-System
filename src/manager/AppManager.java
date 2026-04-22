package manager;

import model.*;
import util.IDGenerator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AppManager is the central business logic layer.
 * It manages all in-memory data and coordinates with DataManager
 * to persist data to files.
 *
 * This is the "Service Layer" in real-world architecture.
 * All GUI panels interact with AppManager, never directly with DataManager.
 */
public class AppManager {

    // In-memory data storage using ArrayList
    private List<User> users;
    private List<Item> items;
    private List<ClaimRequest> claims;

    // Currently logged-in user (null if no one is logged in)
    private User currentUser;

    /**
     * Constructor - loads all data from files on startup.
     */
    public AppManager() {
        DataManager.initialize();

        // Load persisted data
        this.users = DataManager.loadUsers();
        this.items = DataManager.loadItems();
        this.claims = DataManager.loadClaims();

        // Create default admin if no admin exists
        createDefaultAdminIfNeeded();

        System.out.println("AppManager loaded: " + users.size() + " users, "
            + items.size() + " items, " + claims.size() + " claims");
    }

    /**
     * Create a default admin account if none exists.
     * This ensures the system always has at least one admin.
     */
    private void createDefaultAdminIfNeeded() {
        boolean hasAdmin = users.stream().anyMatch(u -> u instanceof Admin);
        if (!hasAdmin) {
            Admin defaultAdmin = new Admin(
                IDGenerator.generateUserId(),
                "System Admin",
                "admin@university.edu",
                "admin123",
                "9999999999",
                "ADM001",
                "Administration"
            );
            users.add(defaultAdmin);
            DataManager.saveUsers(users);
            System.out.println("Default admin created: admin@university.edu / admin123");
        }
    }

    // ==================== AUTHENTICATION ====================

    /**
     * Attempt to login with email and password.
     * Returns the User if successful, null if failed.
     */
    public User login(String email, String password, String expectedRole) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.verifyPassword(password)
                    && user.getRole().equals(expectedRole)) {
                this.currentUser = user;
                return user;
            }
        }
        return null; // Login failed
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() { return currentUser; }

    // ==================== USER MANAGEMENT ====================

    /**
     * Register a new Student account.
     * Returns error message or null if successful.
     */
    public String registerStudent(String name, String email, String password,
                                   String phone, String studentId,
                                   String department, int year) {

        // Input validation
        if (name == null || name.trim().isEmpty()) return "Name is required.";
        if (email == null || !email.contains("@")) return "Valid email is required.";
        if (password == null || password.length() < 6) return "Password must be at least 6 characters.";
        if (studentId == null || studentId.trim().isEmpty()) return "Student ID is required.";

        // Check if email already exists
        if (findUserByEmail(email) != null) return "Email already registered.";

        // Check if student ID already exists
        for (User u : users) {
            if (u instanceof Student && ((Student)u).getStudentId().equals(studentId)) {
                return "Student ID already registered.";
            }
        }

        Student student = new Student(
            IDGenerator.generateUserId(),
            name.trim(), email.trim().toLowerCase(),
            password, phone.trim(),
            studentId.trim(), department.trim(), year
        );

        users.add(student);
        DataManager.saveUsers(users); // Persist immediately
        return null; // null = success
    }

    public User findUserByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst().orElse(null);
    }

    public List<User> getAllUsers() { return Collections.unmodifiableList(users); }

    // ==================== ITEM MANAGEMENT ====================

    /**
     * Add a new Lost or Found item.
     * Returns error message or null if successful.
     */
    public String addItem(String title, String description, String category,
                          String location, Item.ItemType type, String contactInfo) {

        if (currentUser == null) return "Not logged in.";
        if (title == null || title.trim().isEmpty()) return "Title is required.";
        if (description == null || description.trim().isEmpty()) return "Description is required.";
        if (location == null || location.trim().isEmpty()) return "Location is required.";

        Item item = new Item(
            IDGenerator.generateItemId(),
            title.trim(), description.trim(),
            category != null ? category.trim() : "Other",
            location.trim(),
            LocalDate.now().toString(),
            type,
            currentUser.getUserId(),
            currentUser.getName(),
            contactInfo != null ? contactInfo.trim() : currentUser.getPhone()
        );

        items.add(item);
        DataManager.saveItems(items); // Persist immediately
        return null; // success
    }

    /**
     * Remove an item (Admin only).
     */
    public String removeItem(String itemId) {
        if (currentUser == null || !currentUser.hasPermission("REMOVE_ITEM")) {
            return "Permission denied.";
        }

        Item item = findItemById(itemId);
        if (item == null) return "Item not found.";

        item.setStatus(Item.ItemStatus.REMOVED);
        DataManager.saveItems(items);
        return null;
    }

    public Item findItemById(String itemId) {
        return items.stream()
            .filter(i -> i.getItemId().equals(itemId))
            .findFirst().orElse(null);
    }

    /**
     * Get all active (non-removed) items.
     */
    public List<Item> getActiveItems() {
        return items.stream()
            .filter(i -> i.getStatus() != Item.ItemStatus.REMOVED)
            .collect(Collectors.toList());
    }

    /**
     * Get items filtered by type.
     */
    public List<Item> getItemsByType(Item.ItemType type) {
        return items.stream()
            .filter(i -> i.getType() == type && i.getStatus() != Item.ItemStatus.REMOVED)
            .collect(Collectors.toList());
    }

    /**
     * Get claimed items.
     */
    public List<Item> getClaimedItems() {
        return items.stream()
            .filter(i -> i.getStatus() == Item.ItemStatus.CLAIMED)
            .collect(Collectors.toList());
    }

    public List<Item> getAllItems() { return Collections.unmodifiableList(items); }

    // ==================== CLAIM MANAGEMENT ====================

    /**
     * Submit a claim request for an item.
     * Returns error message or null if successful.
     */
    public String submitClaim(String itemId, String claimDescription) {
        if (currentUser == null) return "Not logged in.";
        if (!currentUser.hasPermission("CLAIM_ITEM")) return "Permission denied.";
        if (claimDescription == null || claimDescription.trim().isEmpty()) {
            return "Please describe why you think this is your item.";
        }

        Item item = findItemById(itemId);
        if (item == null) return "Item not found.";
        if (!item.isClaimable()) return "This item is not available for claiming.";

        // Check if user already claimed this item
        boolean alreadyClaimed = claims.stream()
            .anyMatch(c -> c.getItemId().equals(itemId)
                       && c.getClaimantUserId().equals(currentUser.getUserId())
                       && c.getStatus() == ClaimRequest.ClaimStatus.PENDING);
        if (alreadyClaimed) return "You already have a pending claim for this item.";

        ClaimRequest claim = new ClaimRequest(
            IDGenerator.generateClaimId(),
            itemId,
            item.getTitle(),
            currentUser.getUserId(),
            currentUser.getName(),
            currentUser.getEmail(),
            currentUser.getPhone(),
            claimDescription.trim(),
            LocalDate.now().toString()
        );

        claims.add(claim);
        DataManager.saveClaims(claims);
        return null; // success
    }

    /**
     * Approve a claim (Admin only).
     * Marks the item as CLAIMED and rejects all other claims for same item.
     */
    public String approveClaim(String claimId, String adminNote) {
        if (currentUser == null || !currentUser.hasPermission("APPROVE_CLAIM")) {
            return "Permission denied.";
        }

        ClaimRequest claim = findClaimById(claimId);
        if (claim == null) return "Claim not found.";
        if (claim.getStatus() != ClaimRequest.ClaimStatus.PENDING) {
            return "This claim has already been processed.";
        }

        // Approve this claim
        claim.setStatus(ClaimRequest.ClaimStatus.APPROVED);
        claim.setAdminNote(adminNote != null ? adminNote : "Approved by admin.");

        // Mark the item as CLAIMED
        Item item = findItemById(claim.getItemId());
        if (item != null) {
            item.setStatus(Item.ItemStatus.CLAIMED);
            DataManager.saveItems(items);
        }

        // Reject all other pending claims for the same item
        for (ClaimRequest c : claims) {
            if (c.getItemId().equals(claim.getItemId())
                    && !c.getClaimId().equals(claimId)
                    && c.getStatus() == ClaimRequest.ClaimStatus.PENDING) {
                c.setStatus(ClaimRequest.ClaimStatus.REJECTED);
                c.setAdminNote("Item was claimed by another person.");
            }
        }

        DataManager.saveClaims(claims);
        return null; // success
    }

    /**
     * Reject a claim (Admin only).
     */
    public String rejectClaim(String claimId, String adminNote) {
        if (currentUser == null || !currentUser.hasPermission("REJECT_CLAIM")) {
            return "Permission denied.";
        }

        ClaimRequest claim = findClaimById(claimId);
        if (claim == null) return "Claim not found.";
        if (claim.getStatus() != ClaimRequest.ClaimStatus.PENDING) {
            return "This claim has already been processed.";
        }

        claim.setStatus(ClaimRequest.ClaimStatus.REJECTED);
        claim.setAdminNote(adminNote != null ? adminNote : "Rejected by admin.");
        DataManager.saveClaims(claims);
        return null;
    }

    public ClaimRequest findClaimById(String claimId) {
        return claims.stream()
            .filter(c -> c.getClaimId().equals(claimId))
            .findFirst().orElse(null);
    }

    /**
     * Get all claims (Admin view).
     */
    public List<ClaimRequest> getAllClaims() {
        return Collections.unmodifiableList(claims);
    }

    /**
     * Get claims submitted by the current logged-in student.
     */
    public List<ClaimRequest> getMyClaims() {
        if (currentUser == null) return new ArrayList<>();
        return claims.stream()
            .filter(c -> c.getClaimantUserId().equals(currentUser.getUserId()))
            .collect(Collectors.toList());
    }

    /**
     * Get pending claims count (for dashboard stats).
     */
    public long getPendingClaimsCount() {
        return claims.stream()
            .filter(c -> c.getStatus() == ClaimRequest.ClaimStatus.PENDING)
            .count();
    }

    // ==================== DASHBOARD STATS ====================

    public int getTotalItemsCount() { return (int) items.stream()
        .filter(i -> i.getStatus() != Item.ItemStatus.REMOVED).count(); }
    public int getLostItemsCount() { return getItemsByType(Item.ItemType.LOST).size(); }
    public int getFoundItemsCount() { return getItemsByType(Item.ItemType.FOUND).size(); }
    public int getClaimedItemsCount() { return getClaimedItems().size(); }
    public int getTotalUsersCount() { return users.size(); }
    public int getTotalClaimsCount() { return claims.size(); }
}
