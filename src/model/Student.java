package model;

/**
 * INHERITANCE (OOP Concept):
 * Student EXTENDS User - it inherits all fields and methods from User.
 * Student IS-A User (is-a relationship).
 *
 * POLYMORPHISM: Student overrides specific methods from User
 * to provide student-specific behavior.
 */
public class Student extends User {

    // Student-specific fields (in addition to inherited User fields)
    private String studentId;
    private String department;
    private int yearOfStudy;

    /**
     * Constructor calls super() to initialize User fields first,
     * then initializes Student-specific fields.
     */
    public Student(String userId, String name, String email,
                   String password, String phone,
                   String studentId, String department, int yearOfStudy) {

        // Call parent (User) constructor - INHERITANCE in action
        super(userId, name, email, password, phone);

        // Initialize Student-specific fields
        this.studentId = studentId;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
    }

    // ==================== Student-specific Getters ====================

    public String getStudentId() { return studentId; }
    public String getDepartment() { return department; }
    public int getYearOfStudy() { return yearOfStudy; }

    // ==================== Student-specific Setters ====================

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setDepartment(String department) { this.department = department; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    /**
     * POLYMORPHISM: Overrides getRole() from Actionable interface.
     * Student provides its own implementation.
     */
    @Override
    public String getRole() {
        return "STUDENT";
    }

    /**
     * POLYMORPHISM: Overrides hasPermission().
     * Students have limited permissions compared to Admin.
     */
    @Override
    public boolean hasPermission(String permission) {
        switch (permission) {
            case "ADD_ITEM":
            case "VIEW_ITEMS":
            case "CLAIM_ITEM":
            case "VIEW_MY_CLAIMS":
                return true;
            case "APPROVE_CLAIM":
            case "REJECT_CLAIM":
            case "REMOVE_ITEM":
            case "VIEW_ALL_CLAIMS":
            case "VIEW_DASHBOARD":
                return false; // Admin-only permissions
            default:
                return false;
        }
    }

    /**
     * POLYMORPHISM: Overrides toString() for student-specific display.
     */
    @Override
    public String toString() {
        return "Student{name='" + getName() + "', studentId='" + studentId
               + "', dept='" + department + "'}";
    }

    /**
     * Override toJson to include Student-specific fields.
     */
    @Override
    public String toJson() {
        return "{" +
            "\"userId\":\"" + getUserId() + "\"," +
            "\"name\":\"" + getName() + "\"," +
            "\"email\":\"" + getEmail() + "\"," +
            "\"password\":\"" + getPassword() + "\"," +
            "\"phone\":\"" + getPhone() + "\"," +
            "\"role\":\"STUDENT\"," +
            "\"studentId\":\"" + studentId + "\"," +
            "\"department\":\"" + department + "\"," +
            "\"yearOfStudy\":" + yearOfStudy +
            "}";
    }
}
