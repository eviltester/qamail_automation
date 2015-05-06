package email;

import com.jayway.restassured.http.ContentType;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Test;

import javax.mail.Message;

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

        String endpoint = baseendpoint + "/create_session";

        when().get(endpoint).then().contentType(ContentType.XML);


    }
}
