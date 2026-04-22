package model;

/**
 * Item class represents a Lost or Found item in the system.
 *
 * ENCAPSULATION: All fields are private.
 * OOP ASSOCIATION: Item is associated with a User (reportedByUserId).
 * A ClaimRequest references an Item.
 */
public class Item {

    // Enum for item type - clean, type-safe way to represent categories
    public enum ItemType {
        LOST, FOUND
    }

    // Enum for item status
    public enum ItemStatus {
        ACTIVE,    // Item is listed, no claim approved yet
        CLAIMED,   // Claim has been approved
        REMOVED    // Admin removed the item
    }

    // ENCAPSULATION: All fields private
    private String itemId;
    private String title;
    private String description;
    private String category;       // e.g., Electronics, Documents, Clothing
    private String location;       // Where item was lost/found
    private String dateReported;   // Date when reported
    private ItemType type;         // LOST or FOUND
    private ItemStatus status;     // ACTIVE, CLAIMED, REMOVED
    private String reportedByUserId;  // ASSOCIATION: links to User who reported
    private String reportedByName;
    private String contactInfo;
    private String imageDescription; // Optional description of item appearance

    /**
     * Full constructor for creating a new Item.
     */
    public Item(String itemId, String title, String description,
                String category, String location, String dateReported,
                ItemType type, String reportedByUserId,
                String reportedByName, String contactInfo) {

        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.dateReported = dateReported;
        this.type = type;
        this.status = ItemStatus.ACTIVE;  // New items start as ACTIVE
        this.reportedByUserId = reportedByUserId;
        this.reportedByName = reportedByName;
        this.contactInfo = contactInfo;
        this.imageDescription = "";
    }

    // ==================== GETTERS (ENCAPSULATION) ====================

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getDateReported() { return dateReported; }
    public ItemType getType() { return type; }
    public ItemStatus getStatus() { return status; }
    public String getReportedByUserId() { return reportedByUserId; }
    public String getReportedByName() { return reportedByName; }
    public String getContactInfo() { return contactInfo; }
    public String getImageDescription() { return imageDescription; }

    // ==================== SETTERS (ENCAPSULATION) ====================

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(ItemStatus status) { this.status = status; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    /**
     * Helper method to check if item is claimable.
     * Only ACTIVE items can be claimed.
     */
    public boolean isClaimable() {
        return status == ItemStatus.ACTIVE;
    }

    /**
     * Convert to JSON string for file persistence.
     */
    public String toJson() {
        return "{" +
            "\"itemId\":\"" + itemId + "\"," +
            "\"title\":\"" + escapeJson(title) + "\"," +
            "\"description\":\"" + escapeJson(description) + "\"," +
            "\"category\":\"" + escapeJson(category) + "\"," +
            "\"location\":\"" + escapeJson(location) + "\"," +
            "\"dateReported\":\"" + dateReported + "\"," +
            "\"type\":\"" + type.name() + "\"," +
            "\"status\":\"" + status.name() + "\"," +
            "\"reportedByUserId\":\"" + reportedByUserId + "\"," +
            "\"reportedByName\":\"" + escapeJson(reportedByName) + "\"," +
            "\"contactInfo\":\"" + escapeJson(contactInfo) + "\"," +
            "\"imageDescription\":\"" + escapeJson(imageDescription) + "\"" +
            "}";
    }

    /**
     * Escape special characters for JSON strings.
     */
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    @Override
    public String toString() {
        return "Item{id='" + itemId + "', title='" + title
               + "', type=" + type + ", status=" + status + "}";
    }
}
