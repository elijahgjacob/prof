package gitlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commit implements Serializable {
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_DIR = new File(CWD,".gitlet");

    /** Directory folder that contains all of the commit information */
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss YYYY Z");

    public String message;
    public String timestamp;
    public String parentID1;
    public String commitID;
    private TreeMap<String, String> filenameToBlobID; //commitID, Blobs

    public Commit(String message, TreeMap<String, Blobs> blobs){
        this.message = message;
        Date date = new Date();
        this.timestamp = timeFormat.format(date);
        this.commitID = getCommitID();
    }

    TreeMap<String, String> getFilenameToBlobID() {
        return filenameToBlobID;
    }

    String getMessage(){
        return message;
    }

    public String getTime(){
        return timestamp;
    }

    public String getCommitID(){
        String hash = Utils.sha1(Utils.serialize(this));
        return hash;
    }

    public static Commit readCommit(String commitID){
        Commit c;
        File inFile = Utils.join(COMMIT_DIR, commitID);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            c = (Commit) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("No commit with that id exists.");
            c = null;
        }
        return c;
    }
    public static void writeCommit(Commit commit){
        File inFile = Utils.join(COMMIT_DIR, commit.getCommitID());
        Utils.writeContents(inFile, commit);
    }



}
