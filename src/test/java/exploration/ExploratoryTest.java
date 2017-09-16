package exploration;

import email.MailSender;
import org.junit.Assert;
import org.junit.Test;
import qamail.QaMailApi;
import qamail.QaMailApiSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 08/05/2015.
 */
public class ExploratoryTest {

    // a scratchpad for code written to aid exploratory testing

    @Test
    public void exploreExistingMailboxAndSession(){
        // This won't work unless the data listed here is in the environment

        QaMailApiSession session = new QaMailApi().getSession("nTIUamL2QeZYdDSlHdIChkDx");

        List<String> mailboxes = session.listMailBoxes();

        Assert.assertEquals(2,mailboxes.size());
    }

    @Test
    public void exploreSendMultipleEmails(){
        // This won't work unless the data listed here is in the environment

        Map<String,String> toEmails = new HashMap<String,String>();

        MailSender emailer = MailSender.getInstance();

        emailer.sendEmailTo(null, null, toEmails, "Multiple Tos", "Hello this went to many via bcc");

        QaMailApiSession session = new QaMailApi().getSession("nTIUamL2QeZYdDSlHdIChkDx");

        List<String> mailboxes = session.listMailBoxes();

        Assert.assertEquals(2, mailboxes.size());
    }

    @Test
    public void canWeCrashTheLetterImportWithEmailName(){
        // This won't work unless the data listed here is in the environment

        Map<String,String> toEmails = new HashMap<String,String>();

        toEmails.put("bob", "1234--@tpl4.com");

        MailSender emailer = MailSender.getInstance();
        emailer.sendEmailTo(toEmails, null, null, "dodgy email address testing", "this email doesn't exist and is dodgy");

        // monito ./usr/share/qamail/log/import.log
    }

}
