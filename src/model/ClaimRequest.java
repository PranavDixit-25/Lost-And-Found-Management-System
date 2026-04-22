package model;

/**
 * ClaimRequest represents a student's request to claim an item.
 *
 * ASSOCIATION (OOP Concept):
 * ClaimRequest is ASSOCIATED with both User and Item.
 * It stores the IDs of the related User and Item (loose coupling).
 * This creates the relationship: User <-> ClaimRequest <-> Item
 *
 * ENCAPSULATION: All fields private with getters/setters.
 */
public class ClaimRequest {

    // Enum for claim status
    public enum ClaimStatus {
        PENDING,   // Waiting for admin review
        APPROVED,  // Admin approved the claim
        REJECTED   // Admin rejected the claim
    }

    // ENCAPSULATION: All fields private
    private String claimId;
    private String itemId;             // ASSOCIATION: references Item
    private String itemTitle;          // Cached for display
    private String claimantUserId;     // ASSOCIATION: references User
    private String claimantName;
    private String claimantEmail;
    private String claimantPhone;
    private String claimDescription;   // Why they think it's theirs
    private String dateClaimed;
    private ClaimStatus status;
    private String adminNote;          // Note from admin when approving/rejecting

    /**
     * Constructor for creating a new claim request.
     */
    public ClaimRequest(String claimId, String itemId, String itemTitle,
                        String claimantUserId, String claimantName,
                        String claimantEmail, String claimantPhone,
                        String claimDescription, String dateClaimed) {

        this.claimId = claimId;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.claimantUserId = claimantUserId;
        this.claimantName = claimantName;
        this.claimantEmail = claimantEmail;
        this.claimantPhone = claimantPhone;
        this.claimDescription = claimDescription;
        this.dateClaimed = dateClaimed;
        this.status = ClaimStatus.PENDING;  // All new claims start as PENDING
        this.adminNote = "";
    }

    // ==================== GETTERS ====================

    public String getClaimId() { return claimId; }
    public String getItemId() { return itemId; }
    public String getItemTitle() { return itemTitle; }
    public String getClaimantUserId() { return claimantUserId; }
    public String getClaimantName() { return claimantName; }
    public String getClaimantEmail() { return claimantEmail; }
    public String getClaimantPhone() { return claimantPhone; }
    public String getClaimDescription() { return claimDescription; }
    public String getDateClaimed() { return dateClaimed; }
    public ClaimStatus getStatus() { return status; }
    public String getAdminNote() { return adminNote; }

    // ==================== SETTERS ====================

    public void setStatus(ClaimStatus status) { this.status = status; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    /**
     * Convert to JSON string for persistent storage.
     */
    public String toJson() {
        return "{" +
            "\"claimId\":\"" + claimId + "\"," +
            "\"itemId\":\"" + itemId + "\"," +
            "\"itemTitle\":\"" + escapeJson(itemTitle) + "\"," +
            "\"claimantUserId\":\"" + claimantUserId + "\"," +
            "\"claimantName\":\"" + escapeJson(claimantName) + "\"," +
            "\"claimantEmail\":\"" + escapeJson(claimantEmail) + "\"," +
            "\"claimantPhone\":\"" + escapeJson(claimantPhone) + "\"," +
            "\"claimDescription\":\"" + escapeJson(claimDescription) + "\"," +
            "\"dateClaimed\":\"" + dateClaimed + "\"," +
            "\"status\":\"" + status.name() + "\"," +
            "\"adminNote\":\"" + escapeJson(adminNote) + "\"" +
            "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    @Override
    public String toString() {
        return "ClaimRequest{id='" + claimId + "', item='" + itemTitle
               + "', by='" + claimantName + "', status=" + status + "}";
    }
}
