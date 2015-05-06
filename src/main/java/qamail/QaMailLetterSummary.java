package qamail;

/**
 * Created by Alan on 06/05/2015.
 */
public class QaMailLetterSummary {
    public final String id;
    public final String subject;
    public final String from;
    public final String date;

    public QaMailLetterSummary(String id, String subject, String from, String date) {
        this.id=id;
        this.subject=subject;
        this.from = from;
        this.date = date;
    }
}
