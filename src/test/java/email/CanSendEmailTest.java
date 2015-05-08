package email;

import org.codemonkey.simplejavamail.Email;
import org.junit.Assert;
import org.junit.Test;
import qamail.*;

import javax.mail.Message;
import java.util.List;

public class CanSendEmailTest {

    // This test code is to help me get started and make sure the basic libraries work as required

    //TODO find a class library to parse the email content

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
    public void canWorkWithRestAPI(){

        // https://bitbucket.org/naushniki/qamail

        QaMailApi qaMailApi = new QaMailApi();

        // start a qa mail session
        QaMailApiSession qaMailSession = qaMailApi.createSession();

        // list mailboxes

        List<String> mailboxList = qaMailApi.listMailBoxes(qaMailSession.getSessionKey());
        int initialSize = mailboxList.size();
        System.out.println(initialSize);


        // create a new mailbox for the session
        String emailToUse = qaMailApi.createMailbox(qaMailSession.getSessionKey());

        // Check mailbox has a new email in the list
        mailboxList = qaMailApi.listMailBoxes(qaMailSession.getSessionKey());
        int newSize = mailboxList.size();

        Assert.assertTrue("Size should be bigger ", newSize > initialSize);

        //Show mailbox for an email address
        QaMailBox mailbox = qaMailApi.showMailBox(qaMailSession.getSessionKey(), emailToUse);
        String mailboxAddress = mailbox.getEmailAddress();

        Assert.assertEquals("is wrong email address", emailToUse, mailboxAddress);
        Assert.assertEquals("Should not have any email", 0, mailbox.size());

        // send an email
        MailSender emailer = MailSender.getInstance();
        emailer.sendEmailTo(emailToUse, "Bob", "email title", "body of email");


        // wait for email to arrive, then get email and check content
        int startEmailCount = mailbox.size();

        mailbox = new MailBoxPoller(qaMailApi, qaMailSession.getSessionKey(), emailToUse).
                setPollWaitTo(500).
                maxPolls(10).
                waitUntilMoreThan(mailbox.size());

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

    }



    @Test
    public void canWorkWithSessionDomainAPI(){

        QaMailApiSession qaMailSession = QaMailApiSession.start();

        // list mailboxes

        List<String> mailboxList = qaMailSession.listMailBoxes();
        int initialSize = mailboxList.size();
        System.out.println(initialSize);


        // create a new mailbox for the session
        String emailToUse = qaMailSession.createMailbox();

        // Check mailbox has a new email in the list
        mailboxList = qaMailSession.listMailBoxes();
        int newSize = mailboxList.size();

        Assert.assertTrue("Size should be bigger ", newSize > initialSize);

        //Show mailbox for an email address
        QaMailBox mailbox = qaMailSession.showMailBox(emailToUse);
        String mailboxAddress = mailbox.getEmailAddress();

        Assert.assertEquals("is wrong email address", emailToUse, mailboxAddress);
        Assert.assertEquals("Should not have any email", 0, mailbox.size());

        // send an email
        MailSender emailer = MailSender.getInstance();
        emailer.sendEmailTo(emailToUse, "Bob", "email title", "body of email");


        // wait for email to arrive, then get email and check content
        int startEmailCount = mailbox.size();

        mailbox = qaMailSession.mailBoxPollerFor(emailToUse).
                setPollWaitTo(500).
                maxPolls(10).
                waitUntilMoreThan(mailbox.size());

        Assert.assertTrue("Should have received an email", mailbox.size() > startEmailCount);

        List<QaMailLetterSummary> letters = mailbox.getLetters();
        Assert.assertEquals("email title",letters.get(0).subject);
        Assert.assertEquals(emailer.getDefaultFromEmailAddress(), letters.get(0).from);


        //get the body of the email with  /show_letter call
        QaMailLetter letter = qaMailSession.showLetter(emailToUse, letters.get(0).id);
        Assert.assertTrue(letter.content.contains("body of email"));

        // https://bitbucket.org/naushniki/qamail

        // Empty Emailbox
        qaMailSession.emptyMailBox(emailToUse);

        //check mailbox is empty
        mailbox = qaMailSession.showMailBox(emailToUse);
        Assert.assertEquals(0, mailbox.size());
    }


    @Test
    public void canWorkWithSessionAndMailboxDomainAPI(){

        QaMailApiSession qaMailSession = QaMailApiSession.start();

        // list mailboxes

        List<String> mailboxList = qaMailSession.listMailBoxes();
        int initialSize = mailboxList.size();
        System.out.println(initialSize);


        // create a new mailbox for the session
        String emailToUse = qaMailSession.createMailbox();

        // Check mailbox has a new email in the list
        mailboxList = qaMailSession.listMailBoxes();
        int newSize = mailboxList.size();

        Assert.assertTrue("Size should be bigger ", newSize > initialSize);

        //Show mailbox for an email address
        QaMailBox mailbox = qaMailSession.showMailBox(emailToUse);
        String mailboxAddress = mailbox.getEmailAddress();

        Assert.assertEquals("is wrong email address", emailToUse, mailboxAddress);
        Assert.assertEquals("Should not have any email", 0, mailbox.size());

        // send an email
        MailSender emailer = MailSender.getInstance();
        emailer.sendEmailTo(emailToUse, "Bob", "email title", "body of email");


        // wait for email to arrive, then get email and check content
        int startEmailCount = mailbox.size();

        mailbox = mailbox.waitUntilMoreThan(mailbox.size());

        Assert.assertTrue("Should have received an email", mailbox.size() > startEmailCount);

        List<QaMailLetterSummary> letters = mailbox.getLetters();
        Assert.assertEquals("email title",letters.get(0).subject);
        Assert.assertEquals(emailer.getDefaultFromEmailAddress(), letters.get(0).from);


        //get the body of the email with  /show_letter call
        QaMailLetter letter = qaMailSession.showLetter(emailToUse, letters.get(0).id);
        Assert.assertTrue(letter.content.contains("body of email"));

        // https://bitbucket.org/naushniki/qamail

        // Empty Emailbox
        mailbox.empty();

        //check mailbox is empty
        mailbox = mailbox.refresh();

        Assert.assertEquals(0, mailbox.size());
    }

}
