package email;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;

import javax.mail.Message;
import java.util.Map;

public class MailSender {


    private String mail_username;
    private String mail_password;
    private String mail_host;
    private String mail_port;
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

        email.addRecipient(toName, toEmail, Message.RecipientType.TO);
        sendEmailTo(email, title, textOfEmail);
    }

    private  void sendEmailTo(Email email, String title, String textOfEmail){
        email.setSubject(title);
        email.setText(textOfEmail);

        // make the messages visible in std out
        mailer.setDebug(true);
        mailer.sendMail(email);
    }

    public void sendEmailTo(Map<String, String> toEmails, Map<String, String> ccEmails, Map<String, String> bccEmails, String title, String textOfEmail) {

        final Email email = getEmail();

        if(null != toEmails) {
            for (String name : toEmails.keySet()) {
                String address = toEmails.get(name);
                email.addRecipient(name, address, Message.RecipientType.TO);
            }
        }

        if(null != ccEmails) {
            for (String name : ccEmails.keySet()) {
                String address = ccEmails.get(name);
                email.addRecipient(name, address, Message.RecipientType.CC);
            }
        }

        if(null != bccEmails) {
            for (String name : bccEmails.keySet()) {
                String address = bccEmails.get(name);
                email.addRecipient(name, address, Message.RecipientType.BCC);
            }
        }

        sendEmailTo(email, title, textOfEmail);
    }
    // e.g. sendEmailTo(Map<String, String>toEmails, Map<String, String>ccEmails, Map<String, String>bccEmails)
}
