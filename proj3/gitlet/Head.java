package gitlet;


import java.io.Serializable;
import java.io.File;

public class Head implements Serializable {
    private static final String filename = "HEAD";
    private String newCommitID;
    private String newBranchName;


    /**
     * Constructor takes in the branch that is checked out and the front commit of that branch.
     **/
    public Head() {
    }

    /** Constructor takes in the branch that is checked out and the front commit of that branch.*/
    public String getCommitID() {
        return newCommitID;
    }

    /** Method updates the instances in the Head object.
     * @param newCommitID, */
    public void updateHead(String newCommitID, String newBranchName) {
        this.newCommitID = newCommitID;
        this.newBranchName = newBranchName;
    }

    /** Method reads the Head object to the file.
     * @returns Head object*/
    public static Head readHead() {
        Head head;
        File inFile = new File(".gitlet/" + filename);
        head = Utils.readObject(inFile, Head.class);
        return head;
    }

    /** Method saves the Head object to the file.
     * @param head object */
    public static void saveHead(Head head) {
        Utils.writeObject(Commands.HEAD, head);
    }
}
