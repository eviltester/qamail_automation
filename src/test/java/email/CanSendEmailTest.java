package email;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Assert;
import org.junit.Test;

import javax.mail.Message;
import java.util.List;

import static com.jayway.restassured.RestAssured.when;

public class CanSendEmailTest {

    // This test code is to help me get started and make sure the basic libraries work as required

    @Test
    public void canSendEmail(){

        // https://github.com/bbottema/simple-java-mail/wiki/Manual

        MailSender emailer = MailSender.getInstance();

        String mail_username= System.getenv("JAVAMAIL_USERNAME");
        String mail_password= System.getenv("JAVAMAIL_PASSWORD");
        String mail_host= System.getenv("JAVAMAIL_HOST");
        String mail_port= System.getenv("JAVAMAIL_PORT");
        String qamail_host= System.getenv("QAMAIL_HOST");

        final Email email = new Email();

        email.setFromAddress("lollypop", mail_username);
        email.setSubject("hey");
        email.addRecipient("C. Cane", "nxntzu1@" + qamail_host, Message.RecipientType.TO);
        email.setText("email text");
        email.setTextHTML("<img src='cid:wink1'><b>email text</b><img src='cid:wink2'>");


        new Mailer(mail_host, Integer.decode(mail_port), mail_username, mail_password).sendMail(email);
    }

    @Test
    public void canWorkWithAPI(){

        // https://bitbucket.org/naushniki/qamail

        String qamail_host= System.getenv("QAMAIL_HOST");

        String baseendpoint = "http://" + qamail_host + "/api";

        // create a session
        String endpoint = baseendpoint + "/create_session";

        System.out.println(endpoint);
        Response xmlResponse = when().get(endpoint).then().contentType(ContentType.XML).extract().response();

        String xml =  xmlResponse.body().asString();

        XmlPath sessionDetails = new XmlPath(xml);

        String sessionKey = sessionDetails.getString("session.session_key");
        String emailAddress = sessionDetails.getString("session.mailbox.address");

        System.out.println("QA MAIL SESSION KEY: " + sessionKey);

        System.out.println("QA MAIL EMAIL ADDRESS: " + emailAddress);


        // list mailboxes
        String sessionKeyParam = "?session_key=" + sessionKey;

        String listMailboxEndpoint = baseendpoint + "/list_mailboxes" + sessionKeyParam;

        System.out.println(listMailboxEndpoint);
        Response listXmlResponse = when().get(listMailboxEndpoint).then().contentType(ContentType.XML).extract().response();
        String mailboxesXML =  listXmlResponse.body().asString();

        System.out.println(mailboxesXML);

        // parse out mailboxes into a list
        XmlPath mailboxes = new XmlPath(mailboxesXML);
        List<String> mailboxList = mailboxes.getList("session.mailbox.address");
        int initialSize = mailboxList.size();
        System.out.println(initialSize);

        // create a new mailbox for the session
        String createMailBoxEndPoint = baseendpoint + "/create_mailbox" + sessionKeyParam;
        System.out.println(createMailBoxEndPoint);
        Response createMailBoxResponse = when().get(createMailBoxEndPoint).then().contentType(ContentType.XML).extract().response();
        String createMailBoxXML =  createMailBoxResponse.body().asString();

        System.out.println(createMailBoxXML);


        // Check mailbox has a new email in the list
        System.out.println(listMailboxEndpoint);
        listXmlResponse = when().get(listMailboxEndpoint).then().contentType(ContentType.XML).extract().response();
        mailboxesXML =  listXmlResponse.body().asString();

        System.out.println(mailboxesXML);

        mailboxes = new XmlPath(mailboxesXML);
        mailboxList = mailboxes.getList("session.mailbox.address");
        int newSize = mailboxList.size();

        Assert.assertTrue("Size should be bigger ", newSize > initialSize);

        //Show mailbox for an email address
        String showMailBoxEndPoint = baseendpoint + "/show_mailbox_content" + sessionKeyParam + "&address=" + mailboxList.get(0);

        System.out.println(showMailBoxEndPoint);

        Response mailboxXmlResponse = when().get(showMailBoxEndPoint).then().contentType(ContentType.XML).extract().response();
        String mailboxXML = mailboxXmlResponse.body().asString();

        System.out.println(mailboxXML);

        mailboxes = new XmlPath(mailboxXML);
        String mailboxAddress = mailboxes.getString("mailbox.address");

        Assert.assertEquals("is wrong email address", mailboxList.get(0), mailboxAddress);

        List<Object> letterList = mailboxes.getList("mailbox.letter");

        Assert.assertEquals("Should not have any email", 0, letterList.size());

        // TODO send an email
        // TODO get email and check content
        // Empty Emailbox

        String emptyMailBoxEndPoint = baseendpoint + "/empty_mailbox" + sessionKeyParam + "&address=" + mailboxList.get(0);
        System.out.println(emptyMailBoxEndPoint);
        Response emptyMailboxXmlResponse = when().get(emptyMailBoxEndPoint).then().contentType(ContentType.HTML).extract().response();
        String emptyMailboxXmlResponseXML = emptyMailboxXmlResponse.body().asString();

        System.out.println(emptyMailboxXmlResponseXML);
        System.out.println(emptyMailboxXmlResponseXML.length());
        Assert.assertTrue("Should be empty response", 0 == emptyMailboxXmlResponseXML.length());

        //TODO: check mailbox is empty
    }
}
