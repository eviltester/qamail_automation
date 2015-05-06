package qamail;

import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.element.Node;
import com.jayway.restassured.path.xml.element.NodeChildren;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 06/05/2015.
 */
public class QaMailBox {

    private final XmlPath mailbox;

    public QaMailBox(String mailboxXML) {
        mailbox = new XmlPath(mailboxXML);
    }

    public String getEmailAddress(){
        return mailbox.getString("mailbox.address");
    }

    public int size() {
        List<Object> letterList = mailbox.getList("mailbox.letter");
        return letterList.size();
    }

    public List<QaMailLetterSummary> getLetters() {

        //NOTE: I always forget how to return a list of nodes from restassured - this is the easy way
        NodeChildren letterList = mailbox.get("mailbox.letter");

        List<QaMailLetterSummary> summaries = new ArrayList<QaMailLetterSummary>();


        for(Node letter : letterList.list()){
            QaMailLetterSummary summary = new QaMailLetterSummary(
                                                letter.children().getNode("id").value(),
                                                letter.children().getNode("subject").value(),
                                                letter.children().getNode("from").value(),
                                                letter.children().getNode("date").value()
                                                );

            summaries.add(summary);
            //for( Node field : letter.children().list()){
            //    System.out.println(field.name() + ":" + field.value());
            //}
        }

        return summaries;
    }
}