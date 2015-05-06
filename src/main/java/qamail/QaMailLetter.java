package qamail;

import qamail.QaMailLetterSummary;

/**
 * Created by Alan on 06/05/2015.
 */
public class QaMailLetter {
    public final QaMailLetterSummary summary;
    public final String content;


    public QaMailLetter(String id, String subject, String from, String date, String content) {
        this.summary = new QaMailLetterSummary(
                id,
                subject,
                from,
                date
        );
        this.content = content;
    }
}
