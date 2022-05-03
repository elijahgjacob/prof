package gitlet;

import java.io.*;

public class Head implements Serializable {
    private static final String filename = "HEAD";
    private String newCommitID;
    private String newBranchName;


    /** Constructor takes in the branch that is checked out and the front commit of that branch. **/
    public Head(){
    }

    public String getCommitID(){
        return newCommitID;
    }

    // updateHead updates the HEAD contents and publishes to filesystem.
    public void updateHead(String newCommitID, String newBranchName){
        this.newCommitID = newCommitID;
        this.newBranchName = newBranchName;
//        headID = this.getHash(newCommitID, newBranchName);
    }

    public static Head readHead(){
        Head head;
        File inFile = new File(".gitlet/"+filename);
        head = Utils.readObject(inFile, Head.class);
        return head;
    }


    public static void saveHead(Head head){
        Utils.writeObject(Commands.HEAD, head);
    }
}
