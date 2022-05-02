package gitlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commit implements Serializable {
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main folder. */
    static final File GITLET_DIR = new File(CWD,".gitlet");

    /** Directory folder that contains each commit hash*/
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss YYYY Z");

    public String message;
    public String timestamp;
    public String parentID1;
    public String commitID;
//    /** TreeMap for storing commits and all the files that they are linked to in their respective state  **/
//    private TreeMap<String, String> commitIDtoFileName; //commitID, filename //, .toString(contents[])

    /** TreeMap for storing fileNames and their according BlobIDs. **/
    private TreeMap<String, String> fileNameToBlobID; //fileName, BlobID //wug.txt, .toString(contents[])

    public Commit(String message, TreeMap<String, String> commitIDtoFileName, String parentID1){
        this.parentID1 = parentID1;
        this.message = message;
        Date date = new Date();
        this.timestamp = timeFormat.format(date);
        this.commitID = getCommitID();
    }

    public TreeMap<String, String> fileNameToBlobID() {
        return fileNameToBlobID;
    }

    public String getFileNameToBlobID(String commitID){
        return fileNameToBlobID.get(commitID);
    }

//    public TreeMap<String, String> commitIDtoFileName() {
//        return commitIDtoFileName;
//    }
//
//    public String getCommitIDtoFileName(String commitID){
//        return commitIDtoFileName.get(commitID);
//    }

    String getMessage(){
        return message;
    }

    public String getTime(){
        return timestamp;
    }

    public String getParentID1(){
        return parentID1;
    }

    public String getCommitID(){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(this);
            objectStream.close();
            return Utils.sha1(stream.toByteArray());
        } catch (IOException excp){
            throw new IllegalArgumentException( ("Error occurred during hash"));
        }

    }

    public static Commit readCommit(String commitID){
        Commit c;
        File inFile = Utils.join(".gitlet/", commitID);
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

    public static void saveCommit(Commit commit){
        Utils.writeContents(COMMIT_DIR, commit.getCommitID());
    }



}
