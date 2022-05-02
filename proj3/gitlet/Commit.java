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

    public Commit(String message, TreeMap<String, String> fileNameToBlobID, String parentID1){
        this.parentID1 = parentID1;
        this.message = message;
        Date date = new Date();
        timestamp = timeFormat.format(date);
        this.commitID = getCommitID();
        this.fileNameToBlobID = fileNameToBlobID;
    }
    /** TreeMap for storing fileNames and their according BlobIDs. //fileName, BlobID //wug.txt, BlobID **/
    public TreeMap<String, String> fileNameToBlobID() {
        return fileNameToBlobID;
    }

    public String getFileNameToBlobID(String fileName){
        return fileNameToBlobID.get(fileName);
    }

    /** Method adds the commit by commitID to the branchName **/
    public void updateFileNameToBlobID(String fileName, String blobID) {
        fileNameToBlobID.put(fileName, blobID);
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
        String hash= Utils.sha1(Utils.serialize(this));
        return hash;
    }

    public static Commit readCommit(String commitID){
        Commit c;
        File inFile = Utils.join(".gitlet/commits/", commitID);
        c = Utils.readObject(inFile, Commit.class);
        return c;
    }

    public static void saveCommit(Commit commit){
        File inFile = Utils.join(COMMIT_DIR, commit.getCommitID());
        Utils.writeObject(inFile, commit);
    }

}
