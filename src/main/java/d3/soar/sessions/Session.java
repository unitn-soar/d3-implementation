package d3.soar.sessions;

public class Session {
    private static long user_id = -1;

    public static long getUser_id(){
        return user_id;
    }

    public static void setUser_id(long user_id){
        Session.user_id = user_id;
    }

    public static void logout(){
        user_id = -1;
    }
}