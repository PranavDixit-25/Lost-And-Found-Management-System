package manager;

import model.*;
import util.JsonParser;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * DataManager handles ALL data persistence.
 * This is the BACKEND of our application.
 *
 * Responsibilities:
 * - Save users, items, claims to JSON files
 * - Load data from files on startup
 * - Ensure data survives application restarts
 *
 * Pattern used: Singleton-like (one instance shared across app)
 * This class uses static methods for simplicity in a beginner project.
 */
public class DataManager {

    // File paths for data storage
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.json";
    private static final String ITEMS_FILE = DATA_DIR + "/items.json";
    private static final String CLAIMS_FILE = DATA_DIR + "/claims.json";

    /**
     * Initialize the data directory and files if they don't exist.
     * Called once when the application starts.
     */
    public static void initialize() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(DATA_DIR));

            // Create empty JSON files if they don't exist
            createFileIfNotExists(USERS_FILE, "[]");
            createFileIfNotExists(ITEMS_FILE, "[]");
            createFileIfNotExists(CLAIMS_FILE, "[]");

            System.out.println("DataManager initialized. Data directory: " + Paths.get(DATA_DIR).toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error initializing DataManager: " + e.getMessage());
        }
    }

    private static void createFileIfNotExists(String path, String defaultContent) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(defaultContent);
            }
        }
    }

    // ==================== SAVE METHODS ====================

    /**
     * Save all users to users.json
     * Called after every user-related operation.
     */
    public static void saveUsers(List<User> users) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < users.size(); i++) {
            json.append("  ").append(users.get(i).toJson());
            if (i < users.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        writeFile(USERS_FILE, json.toString());
    }

    /**
     * Save all items to items.json
     * Called after every item-related operation.
     */
    public static void saveItems(List<Item> items) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < items.size(); i++) {
            json.append("  ").append(items.get(i).toJson());
            if (i < items.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        writeFile(ITEMS_FILE, json.toString());
    }

    /**
     * Save all claims to claims.json
     * Called after every claim-related operation.
     */
    public static void saveClaims(List<ClaimRequest> claims) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < claims.size(); i++) {
            json.append("  ").append(claims.get(i).toJson());
            if (i < claims.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        writeFile(CLAIMS_FILE, json.toString());
    }

    // ==================== LOAD METHODS ====================

    /**
     * Load all users from users.json
     * Called once on application startup.
     * Returns a list of User objects (Student or Admin).
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String content = readFile(USERS_FILE);
        if (content == null || content.trim().equals("[]")) return users;

        List<String> objects = JsonParser.parseArray(content);
        for (String obj : objects) {
            try {
                Map<String, String> map = JsonParser.parseObject(obj);
                String role = JsonParser.getString(map, "role", "STUDENT");

                if ("ADMIN".equals(role)) {
                    Admin admin = new Admin(
                        JsonParser.getString(map, "userId", ""),
                        JsonParser.getString(map, "name", ""),
                        JsonParser.getString(map, "email", ""),
                        JsonParser.getString(map, "password", ""),
                        JsonParser.getString(map, "phone", ""),
                        JsonParser.getString(map, "adminCode", ""),
                        JsonParser.getString(map, "department", "")
                    );
                    users.add(admin);
                } else {
                    Student student = new Student(
                        JsonParser.getString(map, "userId", ""),
                        JsonParser.getString(map, "name", ""),
                        JsonParser.getString(map, "email", ""),
                        JsonParser.getString(map, "password", ""),
                        JsonParser.getString(map, "phone", ""),
                        JsonParser.getString(map, "studentId", ""),
                        JsonParser.getString(map, "department", ""),
                        JsonParser.getInt(map, "yearOfStudy", 1)
                    );
                    users.add(student);
                }
            } catch (Exception e) {
                System.err.println("Error parsing user: " + e.getMessage());
            }
        }
        return users;
    }

    /**
     * Load all items from items.json
     */
    public static List<Item> loadItems() {
        List<Item> items = new ArrayList<>();
        String content = readFile(ITEMS_FILE);
        if (content == null || content.trim().equals("[]")) return items;

        List<String> objects = JsonParser.parseArray(content);
        for (String obj : objects) {
            try {
                Map<String, String> map = JsonParser.parseObject(obj);
                Item item = new Item(
                    JsonParser.getString(map, "itemId", ""),
                    JsonParser.getString(map, "title", ""),
                    JsonParser.getString(map, "description", ""),
                    JsonParser.getString(map, "category", ""),
                    JsonParser.getString(map, "location", ""),
                    JsonParser.getString(map, "dateReported", ""),
                    Item.ItemType.valueOf(JsonParser.getString(map, "type", "LOST")),
                    JsonParser.getString(map, "reportedByUserId", ""),
                    JsonParser.getString(map, "reportedByName", ""),
                    JsonParser.getString(map, "contactInfo", "")
                );
                String statusStr = JsonParser.getString(map, "status", "ACTIVE");
                item.setStatus(Item.ItemStatus.valueOf(statusStr));
                item.setImageDescription(JsonParser.getString(map, "imageDescription", ""));
                items.add(item);
            } catch (Exception e) {
                System.err.println("Error parsing item: " + e.getMessage());
            }
        }
        return items;
    }

    /**
     * Load all claims from claims.json
     */
    public static List<ClaimRequest> loadClaims() {
        List<ClaimRequest> claims = new ArrayList<>();
        String content = readFile(CLAIMS_FILE);
        if (content == null || content.trim().equals("[]")) return claims;

        List<String> objects = JsonParser.parseArray(content);
        for (String obj : objects) {
            try {
                Map<String, String> map = JsonParser.parseObject(obj);
                ClaimRequest claim = new ClaimRequest(
                    JsonParser.getString(map, "claimId", ""),
                    JsonParser.getString(map, "itemId", ""),
                    JsonParser.getString(map, "itemTitle", ""),
                    JsonParser.getString(map, "claimantUserId", ""),
                    JsonParser.getString(map, "claimantName", ""),
                    JsonParser.getString(map, "claimantEmail", ""),
                    JsonParser.getString(map, "claimantPhone", ""),
                    JsonParser.getString(map, "claimDescription", ""),
                    JsonParser.getString(map, "dateClaimed", "")
                );
                String statusStr = JsonParser.getString(map, "status", "PENDING");
                claim.setStatus(ClaimRequest.ClaimStatus.valueOf(statusStr));
                claim.setAdminNote(JsonParser.getString(map, "adminNote", ""));
                claims.add(claim);
            } catch (Exception e) {
                System.err.println("Error parsing claim: " + e.getMessage());
            }
        }
        return claims;
    }

    // ==================== FILE I/O HELPERS ====================

    private static void writeFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to " + path + ": " + e.getMessage());
        }
    }

    private static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.err.println("Error reading " + path + ": " + e.getMessage());
            return null;
        }
    }
}
