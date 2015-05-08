package qamail;

import java.util.List;

public class QaMailApiSession {
    private QaMailApi qaMailApi;
    private final String sessionKey;
    private final String defaultEmailAddress;

    public QaMailApiSession(QaMailApi qaMailApi, String sessionKey, String emailAddress) {
        this.sessionKey = sessionKey;
        this.defaultEmailAddress = emailAddress;
        this.qaMailApi = qaMailApi;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public static QaMailApiSession start() {
        return new QaMailApi().createSession();
    }

    public List<String> listMailBoxes() {
        return qaMailApi.listMailBoxes(sessionKey);
    }

    public String createMailbox() {
        return qaMailApi.createMailbox(sessionKey);
    }

    public QaMailBox showMailBox(String email) {
        return qaMailApi.showMailBox(sessionKey, email);
    }

    public MailBoxPoller mailBoxPollerFor(String email) {
        return new MailBoxPoller(qaMailApi, sessionKey, email);
    }

    public QaMailLetter showLetter(String emailToUse, String letterId) {
        return qaMailApi.showLetter(sessionKey, emailToUse, letterId);
    }

    public void emptyMailBox(String emailToUse) {
        qaMailApi.emptyMailBox(sessionKey, emailToUse);
    }
}
