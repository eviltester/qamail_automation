package exploration;

import org.junit.Assert;
import org.junit.Test;
import qamail.QaMailApi;
import qamail.QaMailApiSession;

import java.util.List;

/**
 * Created by Alan on 08/05/2015.
 */
public class ExploratoryTest {

    // a scratchpad for code written to aid exploratory testing

    @Test
    public void exploreExistingMailboxAndSession(){
        // This won't work unless the data listed here is in the environment

        QaMailApiSession session = new QaMailApi().getSession("nTIUamL2QeZYdDSlHdIChkDx");

        List<String> mailboxes = session.listMailBoxes();

        Assert.assertEquals(2,mailboxes.size());
    }
}
