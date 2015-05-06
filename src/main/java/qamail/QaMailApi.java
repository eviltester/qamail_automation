package qamail;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;

import java.util.List;

import static com.jayway.restassured.RestAssured.when;

public class QaMailApi {

    // TODO: Store last response

    private final String qamail_host;
    private final String baseEndPoint;
    private Response lastResponse;

    public QaMailApi(){
        qamail_host= System.getenv("QAMAIL_HOST");
        baseEndPoint = "http://" + qamail_host + "/api";
    }

    public String getHostApiUrl() {
        return baseEndPoint;
    }

    public String getHost() {
        return qamail_host;
    }

    public QaMailApiSession createSession() {


        // create a session
        String endpoint = baseEndPoint + "/create_session";

        System.out.println(endpoint);
        Response xmlResponse = when().get(endpoint).then().contentType(ContentType.XML).extract().response();

        storeLastResponse(xmlResponse);


        String xml =  xmlResponse.body().asString();

        XmlPath sessionDetails = new XmlPath(xml);

        String sessionKey = sessionDetails.getString("session.session_key");
        String emailAddress = sessionDetails.getString("session.mailbox.address");

        System.out.println("QA MAIL SESSION KEY: " + sessionKey);

        System.out.println("QA MAIL EMAIL ADDRESS: " + emailAddress);

        QaMailApiSession session = new QaMailApiSession(sessionKey, emailAddress);

        return session;

        //TODO: store sessions in a list in QaMailApi
        //TODO: allow createSession("logical_name"); to access sessions later using logical_name as a key
    }

    private void storeLastResponse(Response xmlResponse) {
        lastResponse = xmlResponse;
    }

    public List<String> listMailBoxes(String sessionKey) {

        String listMailboxEndpoint = baseEndPoint + "/list_mailboxes" + sessionKeyParam(sessionKey);

        System.out.println(listMailboxEndpoint);
        Response listXmlResponse = when().get(listMailboxEndpoint).then().contentType(ContentType.XML).extract().response();
        String mailboxesXML =  listXmlResponse.body().asString();

        storeLastResponse(listXmlResponse);

        System.out.println(mailboxesXML);

        // parse out mailboxes into a list
        XmlPath xmlResponse = new XmlPath(mailboxesXML);
        List<String> mailboxList = xmlResponse.getList("session.mailbox.address");

        return mailboxList;
    }

    private String sessionKeyParam(String sessionKey) {
        return "?session_key=" + sessionKey;
    }

    public String createMailbox(String sessionKey) {
        String createMailBoxEndPoint = baseEndPoint + "/create_mailbox" + sessionKeyParam(sessionKey);
        System.out.println(createMailBoxEndPoint);
        Response createMailBoxResponse = when().get(createMailBoxEndPoint).then().contentType(ContentType.XML).extract().response();
        String createMailBoxXML =  createMailBoxResponse.body().asString();

        storeLastResponse(createMailBoxResponse);

        System.out.println(createMailBoxXML);

        XmlPath xmlResponse = new XmlPath(createMailBoxXML);
        return xmlResponse.getString("mailbox.address");
    }

    public QaMailBox showMailBox(String sessionKey, String email) {

        String showMailBoxEndPoint = baseEndPoint + "/show_mailbox_content" + sessionKeyParam(sessionKey) + addressParam(email);

        System.out.println(showMailBoxEndPoint);

        Response mailboxXmlResponse = when().get(showMailBoxEndPoint).then().contentType(ContentType.XML).extract().response();
        String mailboxXML = mailboxXmlResponse.body().asString();

        storeLastResponse(mailboxXmlResponse);

        System.out.println(mailboxXML);

        return new QaMailBox(mailboxXML);
    }

    private String addressParam(String email) {
        return "&address=" + email;
    }

    public void emptyMailBox(String sessionKey, String email) {
        String emptyMailBoxEndPoint = baseEndPoint + "/empty_mailbox" + sessionKeyParam(sessionKey) + addressParam(email);
        System.out.println(emptyMailBoxEndPoint);
        Response emptyMailboxXmlResponse = when().get(emptyMailBoxEndPoint).then().contentType(ContentType.HTML).extract().response();
        String emptyMailboxXmlResponseXML = emptyMailboxXmlResponse.body().asString();

        storeLastResponse(emptyMailboxXmlResponse);

        System.out.println(emptyMailboxXmlResponseXML);
        System.out.println(emptyMailboxXmlResponseXML.length());
    }

}
