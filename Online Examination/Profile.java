public class Profile {
    public static void updateProfile(User user, String newPassword, String newProfile) {
        user.setPassword(newPassword);
        user.setProfile(newProfile);
    }
}
