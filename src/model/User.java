package model;

import interfaces.Actionable;

/**
 * ABSTRACT CLASS (OOP Concept):
 * User is abstract - you cannot create a "User" directly.
 * You must create either a Student or Admin (subclasses).
 *
 * ENCAPSULATION: All fields are private.
 * Access is only through public getters/setters.
 *
 * INHERITANCE: Student and Admin will extend this class.
 * They inherit all fields and methods defined here.
 *
 * ABSTRACTION: Implements Actionable interface,
 * forcing subclasses to define specific behaviors.
 */
public abstract class User implements Actionable {

    // ENCAPSULATION: private fields - cannot be accessed directly from outside
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phone;

    /**
     * Constructor - initializes all user fields.
     * Called by subclass constructors using super().
     */
    public User(String userId, String name, String email, String password, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // ==================== GETTERS (ENCAPSULATION) ====================

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }

    // ==================== SETTERS (ENCAPSULATION) ====================

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * POLYMORPHISM: toString() is overridden here.
     * Each subclass can further override this.
     */
    @Override
    public String toString() {
        return "User{id='" + userId + "', name='" + name + "', email='" + email + "'}";
    }

    /**
     * POLYMORPHISM: performAction is defined in the interface.
     * Each subclass provides its own implementation.
     */
    @Override
    public void performAction(String action) {
        System.out.println(getName() + " [" + getRole() + "] performed action: " + action);
    }

    /**
     * Verify if the given password matches this user's password.
     * Used during login.
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    /**
     * Convert User to JSON string for file storage.
     * Manual JSON serialization (no external libraries needed).
     */
    public String toJson() {
        return "{" +
            "\"userId\":\"" + userId + "\"," +
            "\"name\":\"" + name + "\"," +
            "\"email\":\"" + email + "\"," +
            "\"password\":\"" + password + "\"," +
            "\"phone\":\"" + phone + "\"," +
            "\"role\":\"" + getRole() + "\"" +
            "}";
    }
}
