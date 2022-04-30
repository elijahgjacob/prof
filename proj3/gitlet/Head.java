package gitlet;

import java.io.File;
import java.io.Serializable;

public class Head implements Serializable {
    private static final String filename = "HEAD";
    private static String commitID;
    private static String branchName;

    /** Constructor takes in the branch that is checked out and the front commit of that branch. **/
    public Head() {
    }

    public static String getCommitID(){
        return commitID;
    }

    public static Head getHead(){
        File f = new File(filename);
        return Utils.readObject(f, Head.class);
    }

    // updateHead updates the HEAD contents and publishes to filesystem.
    public void updateHead(String newCommitID, String newBranchName){
        commitID = newCommitID;
        branchName = newBranchName;
    }

    public static void saveHead(Head h){
        File inFile = new File(filename);
        Utils.writeObject(inFile, h);
    }
}
