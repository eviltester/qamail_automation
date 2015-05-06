package email;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;

import javax.mail.Message;

public class MailSender {


    private String mail_username;
    private String mail_password;
    private String mail_host;
    private String mail_port;
    private String qamail_host;
    private Mailer mailer;

    public static MailSender getInstance() {

        MailSender sender = new MailSender();
        sender.instantiateFromEnvironmentVars();

        return sender;
    }

    public void instantiateFromEnvironmentVars(){

        mail_username= System.getenv("JAVAMAIL_USERNAME");
        mail_password= System.getenv("JAVAMAIL_PASSWORD");
        mail_host= System.getenv("JAVAMAIL_HOST");
        mail_port= System.getenv("JAVAMAIL_PORT");

        mailer = new Mailer(mail_host, Integer.decode(mail_port), mail_username, mail_password);
    }

    public String getDefaultFromEmailAddress(){
        return mail_username;
    }

    public Email getEmail() {
        Email email = new Email();

        email.setFromAddress("default", mail_username);
        email.setSubject("default");
        email.setText("default text");
        return email;
    }

    public void sendMail(Email email) {
        mailer.sendMail(email);
    }

    public void sendEmailTo(String toEmail, String toName, String title, String textOfEmail) {
        final Email email = getEmail();

        email.setSubject(title);
        email.addRecipient(toName, toEmail, Message.RecipientType.TO);
        email.setText(textOfEmail);

        mailer.sendMail(email);
    }
}
