package email;

import org.codemonkey.simplejavamail.Email;
import org.junit.Assert;
import org.junit.Test;
import qamail.QaMailApi;
import qamail.QaMailApiSession;
import qamail.QaMailBox;
import qamail.QaMailLetterSummary;

import javax.mail.Message;
import java.util.List;

public class CanSendEmailTest {

    // This test code is to help me get started and make sure the basic libraries work as required

    @Test
    public void canSendEmail(){

        // https://github.com/bbottema/simple-java-mail/wiki/Manual

        MailSender emailer = MailSender.getInstance();

        final Email email = emailer.getEmail();

        String qamail_host = System.getenv("QAMAIL_HOST");

        email.setSubject("hey");
        email.addRecipient("C. Cane", "nxntzu1@" + qamail_host, Message.RecipientType.TO);
        email.setText("email text");
        email.setTextHTML("<img src='cid:wink1'><b>email text</b><img src='cid:wink2'>");


        emailer.sendMail(email);
    }

    @Test
    public void canWorkWithAPI(){

        // https://bitbucket.org/naushniki/qamail

        QaMailApi qaMailApi = new QaMailApi();

        String qamail_host= qaMailApi.getHost();
        String baseendpoint = qaMailApi.getHostApiUrl();


        QaMailApiSession qaMailSession = qaMailApi.createSession();

        // list mailboxes
        String sessionKeyParam = "?session_key=" + qaMailSession.getSessionKey();

        List<String> mailboxList = qaMailApi.listMailBoxes(qaMailSession.getSessionKey());
        int initialSize = mailboxList.size();
        System.out.println(initialSize);


        // create a new mailbox for the session
        String mailBoxEmail = qaMailApi.createMailbox(qaMailSession.getSessionKey());

        // Check mailbox has a new email in the list
        mailboxList = qaMailApi.listMailBoxes(qaMailSession.getSessionKey());
        int newSize = mailboxList.size();

        Assert.assertTrue("Size should be bigger ", newSize > initialSize);

        String emailToUse = mailboxList.get(0);

        //Show mailbox for an email address
        QaMailBox mailbox = qaMailApi.showMailBox(qaMailSession.getSessionKey(), emailToUse);
        String mailboxAddress = mailbox.getEmailAddress();

        Assert.assertEquals("is wrong email address", emailToUse, mailboxAddress);
        Assert.assertEquals("Should not have any email", 0, mailbox.size());

        // TODO send an email
        MailSender emailer = MailSender.getInstance();
        emailer.sendEmailTo(emailToUse, "Bob", "email title", "body of email");

        // TODO get email and check content
        int startEmailCount = mailbox.size();
        int pollLoop = 10;
        while(startEmailCount == mailbox.size()){

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("polling mailbox " + "(" + pollLoop + ")" + emailToUse);

            mailbox = qaMailApi.showMailBox(qaMailSession.getSessionKey(), emailToUse);

            pollLoop--;
            if(pollLoop<=0) break;
        }

        Assert.assertTrue("Should have received an email", mailbox.size() > startEmailCount);

        List<QaMailLetterSummary> letters = mailbox.getLetters();
        Assert.assertEquals("email title",letters.get(0).subject);
        Assert.assertEquals(emailer.getDefaultFromEmailAddress(), letters.get(0).from);


        //get the body of the email with  /show_letter call
        QaMailLetter letter = qaMailApi.showLetter(qaMailSession.getSessionKey(), emailToUse, letters.get(0).id);
        Assert.assertTrue(letter.content.contains("body of email"));

        // https://bitbucket.org/naushniki/qamail

        // Empty Emailbox
        qaMailApi.emptyMailBox(qaMailSession.getSessionKey(), emailToUse);

        //check mailbox is empty
        mailbox = qaMailApi.showMailBox(qaMailSession.getSessionKey(), emailToUse);
        Assert.assertEquals(0, mailbox.size());

        //TODO create session wrapper around the API, so this test is written in terms of the session, rather than the API
        //TODO find a class library to parse the email content
    }
}
