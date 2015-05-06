package email;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Test;

import javax.mail.Message;
import java.util.List;

import static com.jayway.restassured.RestAssured.when;

public class CanSendEmailTest {

    // This test code is to help me get started and make sure the basic libraries work as required

    @Test
    public void canSendEmail(){

        // https://github.com/bbottema/simple-java-mail/wiki/Manual

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

        Response xmlResponse = when().get(endpoint).then().contentType(ContentType.XML).extract().response();

        String xml =  xmlResponse.body().asString();

        XmlPath sessionDetails = new XmlPath(xml);

        String sessionKey = sessionDetails.getString("session.session_key");
        String emailAddress = sessionDetails.getString("session.mailbox.address");

        System.out.println("QA MAIL SESSION KEY: " + sessionKey);

        System.out.println("QA MAIL EMAIL ADDRESS: " + emailAddress);


        // list mailboxes
        endpoint = baseendpoint + "/list_mailboxes" + "?session_key=" + sessionKey;

        Response listXmlResponse = when().get(endpoint).then().contentType(ContentType.XML).extract().response();
        String mailboxesXML =  xmlResponse.body().asString();

        System.out.println(mailboxesXML);

        // parse out mailboxes into a list
        XmlPath mailboxes = new XmlPath(mailboxesXML);
        List<String> mailboxList = mailboxes.getList("session.mailbox.address");
        System.out.println(mailboxList.size());
    }
}
