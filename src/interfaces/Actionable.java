package interfaces;

/**
 * ABSTRACTION (OOP Concept):
 * An interface defines a CONTRACT - what methods MUST be implemented.
 * Any class that "implements Actionable" MUST provide these methods.
 * This ensures consistent behavior across different types of users.
 */
public interface Actionable {

    /**
     * Every actionable entity must be able to perform an action.
     * The actual implementation varies per class (Polymorphism).
     */
    void performAction(String action);

    /**
     * Every actionable entity must have a displayable role.
     */
    String getRole();

    /**
     * Check if this entity has permission for a specific operation.
     */
    boolean hasPermission(String permission);
}
