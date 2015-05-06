package email;

public class MailSender {


    private String mail_username;
    private String mail_password;
    private String mail_host;
    private String mail_port;
    private String qamail_host;

    public static MailSender getInstance() {
        return null;
    }

    public void instantiateFromEnvironmentVars(){

        mail_username= System.getenv("JAVAMAIL_USERNAME");
        mail_password= System.getenv("JAVAMAIL_PASSWORD");
        mail_host= System.getenv("JAVAMAIL_HOST");
        mail_port= System.getenv("JAVAMAIL_PORT");
        qamail_host= System.getenv("QAMAIL_HOST");
    }


}
