package d3.soar.MainLists;

import java.util.List;

import d3.soar.UserData.Session;

/* @brief Represents a list of sessions in the system, containing methods to add, remove, and read sessions from a file*/
public class Sessions {
    public List<Session> sessions;

    /* @brief Constructs a new sessions object with the specified list of sessions*/
    public Sessions() {
    }

    /* @brief Constructs a new sessions object with the specified list of sessions*/
    public Sessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    /* @brief Adds a session to the sessions list*/
    public void addSession(Session session) {
        this.sessions.add(session);
    }

    /* @brief Removes a session from the sessions list*/
    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    /* @brief Reads sessions from a file and populates the sessions list*/
    public void readSessionsFromFile(String filePath) {
        // Implement file reading logic to populate the sessions list
    }

    /* @brief Gets the list of sessions for the sessions object*/
    public void setSessions(Sessions sessions) {
        this.sessions = sessions.sessions;
    }
}

