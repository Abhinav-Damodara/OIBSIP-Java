
public class Login {
    public static User loginUser(String username, String password) {
        // Logic to validate username and password
        // For simplicity, I'll just create a hardcoded user
        if (username.equals("admin") && password.equals("admin123")) {
            return new User(username, password, "Admin Profile");
        } else {
            return null; // Invalid credentials
        }
    }
}
