package model;

/**
 * INHERITANCE (OOP Concept):
 * Admin EXTENDS User - inherits all User fields and methods.
 * Admin IS-A User but with more permissions.
 *
 * POLYMORPHISM: Admin overrides methods to provide admin-specific behavior.
 */
public class Admin extends User {

    // Admin-specific fields
    private String adminCode;    // Special admin authorization code
    private String department;

    /**
     * Constructor - uses super() to call User constructor.
     */
    public Admin(String userId, String name, String email,
                 String password, String phone,
                 String adminCode, String department) {

        // Call parent (User) constructor
        super(userId, name, email, password, phone);

        this.adminCode = adminCode;
        this.department = department;
    }

    // ==================== Getters ====================

    public String getAdminCode() { return adminCode; }
    public String getDepartment() { return department; }

    // ==================== Setters ====================

    public void setAdminCode(String adminCode) { this.adminCode = adminCode; }
    public void setDepartment(String department) { this.department = department; }

    /**
     * POLYMORPHISM: Admin's getRole() returns "ADMIN".
     * Different from Student's getRole() which returns "STUDENT".
     */
    @Override
    public String getRole() {
        return "ADMIN";
    }

    /**
     * POLYMORPHISM: Admin has ALL permissions.
     * Contrast with Student who has limited permissions.
     */
    @Override
    public boolean hasPermission(String permission) {
        // Admin can do everything
        return true;
    }

    /**
     * POLYMORPHISM: Admin-specific toString().
     */
    @Override
    public String toString() {
        return "Admin{name='" + getName() + "', dept='" + department + "'}";
    }

    /**
     * Override toJson to include Admin-specific fields.
     */
    @Override
    public String toJson() {
        return "{" +
            "\"userId\":\"" + getUserId() + "\"," +
            "\"name\":\"" + getName() + "\"," +
            "\"email\":\"" + getEmail() + "\"," +
            "\"password\":\"" + getPassword() + "\"," +
            "\"phone\":\"" + getPhone() + "\"," +
            "\"role\":\"ADMIN\"," +
            "\"adminCode\":\"" + adminCode + "\"," +
            "\"department\":\"" + department + "\"" +
            "}";
    }
}
