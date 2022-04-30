package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author Elijah G. Jacob
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(UnitTest.class));
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void initTest() {
        String message1 = "init";
        TreeMap<String, Blobs> commitmap1 = new TreeMap<>();
        String message2 = "init";
        TreeMap<String, Blobs> commitmap2 = new TreeMap<>();
        Commit commit1 = new Commit(message1, commitmap1);
        Commit commit2 = new Commit(message2, commitmap2);
        assertEquals(null, commit1.getFilenameToBlobID());
        assertEquals(null, commit2.getFilenameToBlobID());
        assertNotEquals(commit1, commit2);

    }

    public void addTest(){

    }

}


