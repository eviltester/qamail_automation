package qamail;

public class QaMailApiSession {
    private final String sessionKey;
    private final String defaultEmailAddress;

    public QaMailApiSession(String sessionKey, String emailAddress) {
        this.sessionKey = sessionKey;
        this.defaultEmailAddress = emailAddress;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
