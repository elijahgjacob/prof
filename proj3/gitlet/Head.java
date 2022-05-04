package gitlet;


import java.io.Serializable;
import java.io.File;

public class Head implements Serializable {
    private String commitID;
    private String branchName;

    /**
     * Constructor takes in the branch that is checked out and the front commit of that branch.
     **/
    public Head() {
        this.commitID = commitID;
        this.branchName = branchName;
    }

    /** Constructor takes in the branch that is checked out and the front commit of that branch.*/
    public String getCommitID() {
        return this.commitID;
    }

    /** Constructor takes in the branch that is checked out and the front commit of that branch.*/
    public String getBranchName() {
        return branchName;
    }

    /** Method updates the instances in the Head object.
     * @param newCommitID, */
    public void updateHead(String newCommitID, String newBranchName) {
        this.commitID = newCommitID;
        this.branchName = newBranchName;
    }



    /** Method reads the Head object to the file.
     * @returns Head object*/
    public static Head readHead(String HEAD) {
        Head head;
        File inFile = new File(".gitlet/" + HEAD);
        head = Utils.readObject(inFile, Head.class);
        return head;
    }

    /** Method saves the Head object to the file.
     * @param head object */
    public static void saveHead(Head head) {
        Utils.writeObject(Commands.HEAD, head);
    }
}
