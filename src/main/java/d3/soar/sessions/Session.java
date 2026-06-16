package d3.soar.sessions;

public class Session {
    private static long user_id = -1;
    private static String email;
    private static String salt = "$2a$12$R9h/cIPz0gi.URNNX3kh2O";

    public static long getUser_id(){
        return user_id;
    }

    public static void setUser_id(long user_id){
        Session.user_id = user_id;
    }

    public static void logout(){
        user_id = -1;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static String getSalt() {
        return salt;
    }
}