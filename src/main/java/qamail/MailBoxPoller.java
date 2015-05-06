package qamail;

/**
 * Created by Alan on 06/05/2015.
 */
public class MailBoxPoller {
    private final QaMailApi qaMailApi;
    private final String sessionKey;
    private final String email;
    private int maxPolls;
    private int pollWaitMillis;

    public MailBoxPoller(QaMailApi qaMailApi, String sessionKey, String emailToUse) {
        this.qaMailApi = qaMailApi;
        this.sessionKey = sessionKey;
        this.email = emailToUse;
        this.pollWaitMillis = 100;
        this.maxPolls = 30;
    }


    public MailBoxPoller setPollWaitTo(int millis) {
        this.pollWaitMillis = millis;
        return this;
    }

    public MailBoxPoller maxPolls(int maximumNumberOfPolls) {
        this.maxPolls = maximumNumberOfPolls;
        return this;
    }

    public QaMailBox waitUntilMoreThan(int currentSize) {
        int startEmailCount = currentSize;
        int pollLoop = this.maxPolls;
        QaMailBox mailbox=null;

        while(startEmailCount == currentSize){

            try {
                Thread.sleep(this.pollWaitMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("polling mailbox " + "(" + pollLoop + ")" + this.email);

            mailbox = qaMailApi.showMailBox(this.sessionKey, this.email);

            currentSize = mailbox.size();

            pollLoop--;
            if(pollLoop<=0) break;
        }

        return mailbox;
    }
}
