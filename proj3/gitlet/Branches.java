package gitlet;


import java.io.*;
import java.util.TreeMap;

public class Branches implements Serializable {
    /** <String, String> <branchname, commitID></branchname,></String,> **/
    public static TreeMap<String, String> branchNameToCommit = new TreeMap<>();
    // Filename of the Branch file.
    private static final String filename = "BRANCH";
    public String branchName;
    public String commitID;

    /** Branch constructor**/
    public Branches() {
        this.branchName = branchName;
        this.commitID = commitID;
        this.branchNameToCommit = branchNameToCommit;
    }
    /** Method returns branchName to Commit TreeMap **/
    public static TreeMap<String, String> getBranchNameToCommit() {
        return branchNameToCommit;
    }

    /** Method gets the commit ID of the latest commit on that branch. **/
    public static String getCommitIDForBranch(String branchName){
        return branchNameToCommit.get(branchName);
    }

    /** Method adds the commit by commitID to the branchName **/
    public void updateBranch(String branchName, String commitID) {
        branchNameToCommit.put(branchName, commitID);
    }

    /** Method returns the branchName of the commitID **/
    public static String getBranchName(String commitID){
        for (String s : branchNameToCommit.keySet()){
            if (branchNameToCommit.get(s).equals(commitID)){
                return s;
            }
        }
        return commitID;
    }

    /**Returns the Branch object with that branchName*/
    public static Branches getBranches(String branchName){
        Branches b;
        File inFile = new File(".gitlet/"+branchName);
        b = Utils.readObject(inFile, Branches.class);
        return b;
    }

    public static void saveBranch(Branches b){
        Utils.writeObject(Commands.master, b);
    }
}
