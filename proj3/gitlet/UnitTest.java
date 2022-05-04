package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
        TreeMap<String, String> commitmap1 = new TreeMap<>();
        String message2 = "init";
        TreeMap<String, String> commitmap2 = new TreeMap<>();
        Commit commit1 = new Commit(message1, commitmap1, null);
        Commit commit2 = new Commit(message2, commitmap2,
                commit1.getCommitID());
        assertEquals(commit1.getCommitID(), commit2.getParentID1());
        assertNotEquals(commit1, commit2);
    }
    @Test
    public void singleBranchTest() throws IOException {
        String c = "abc";
        File fnc = new File("filea");
        fnc.createNewFile();
        Utils.writeContents(fnc, c);
        Blobs blob1 = new Blobs(fnc.getName());
        String e = "abcde";
        File fne = new File("fileb");
        fnc.createNewFile();
        Utils.writeContents(fne, e);
        Blobs blob2 = new Blobs(fnc.getName());
        TreeMap<String, String> blob1Map = new TreeMap<>();
        blob1Map.put(c, blob1.getBlobID());
        TreeMap<String, String> blob2Map = new TreeMap<>();
        TreeMap<String, String> branchNameToCommit = new TreeMap<>();
        blob2Map.put("filea", blob1.getBlobID());
        blob2Map.put("fileb", blob2.getBlobID());
        String message = "first";
        String message1 = "second";
        Commit firstcommit = new Commit(message,
                blob1Map, null);
        Branches b = new Branches();
        Branches.saveBranch(b);
        Commit secondcommit = new Commit(message1,
                blob2Map, firstcommit.getCommitID());
        Branches nb = new Branches();
        Branches.saveBranch(b);
        assertEquals("first", firstcommit.getMessage());
        assertEquals("second", secondcommit.getMessage());
        assertEquals(null, b.readBranches("master"));
        assertEquals(secondcommit.getCommitID(),
                b.getBranchNameToCommit().get("master"));
    }

}


