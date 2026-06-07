package d3.soar.MainLists;

import java.util.List;

import d3.soar.UserData.Session;

public class Sessions {
    public List<Session> sessions;

    // Default constructor
    public Sessions() {
    }

    // Constructor with all fields
    public Sessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    public void readSessionsFromFile(String filePath) {
        // Implement file reading logic to populate the sessions list
    }
}

