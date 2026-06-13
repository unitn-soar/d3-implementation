package d3.soar.sessions;

public class session {
    private static long user_id;




    public static long getUser_id(){
        return user_id;
    }

    public static void setUser_id(long user_id) {
        session.user_id = user_id;
    }
}
