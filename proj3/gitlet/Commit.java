package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commit implements Serializable {

    /** Var for storing contents **/
    private static byte [] contents;
    /** Var for branch */
    private static int branch;


    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_DIR = new File(CWD,".gitlet");

    /** Directory folder that contains all of the commit information */
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");

    /** Directory folder that contains the Stage folder */
    static final File STAGING_DIR = Utils.join(GITLET_DIR, "staging");

    /** Directory folder that contains all of the staging data to be added */
    static final File STAGADD_SUBDIR = Utils.join(STAGING_DIR, "stagadd");

    /** Directory folder that contains all of the staging data to be removed*/
    static final File STAGRM_SUBDIR = Utils.join(STAGING_DIR, "stagrm");

    /** Directory folder that contains all of the branches of data*/
    static final File BRANCHES_DIR = Utils.join(GITLET_DIR, "branches");

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss YYYY Z");

    public String message;
    public String timestamp;
    public String parentID1;
    public String commitID;
    private TreeMap<String, Blobs> commitmap; //commitID, Blobs

    public Commit(String message, TreeMap<String, Blobs> blobs){
        this.message = message;
        Date date = new Date();
        this.timestamp = timeFormat.format(date);
        this.commitID = getcommitID();
    }

    TreeMap<String, Blobs> getBlobs() {
        return commitmap;
    }

    String getMessage(){
        return message;
    }

    public String getTime(){
        return timestamp;
    }

    Integer getBranch(){
        return branch;
    }

    public String getcommitID(){
        String hash = Utils.sha1(Utils.serialize(this));
        return hash;
    }

    public static Commit readCommit(String commitID){
        File f = new File(commitID);
        return Utils.readObject(f, Commit.class);
    }
    public static void writeCommit(Commit commit){
        File inFile = new File(commit.getcommitID());
        Utils.writeContents(inFile, commit);
    }



}
