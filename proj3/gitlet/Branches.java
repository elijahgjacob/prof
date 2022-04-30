package gitlet;


import java.io.*;
import java.util.TreeMap;

public class Branches implements Serializable {
    // branchNameToCommit stores <K,V> pairs of branches to their front commits
    // TODO: See if <K,V> pairs can be put into branchNameToCommit
    private static TreeMap<String, String> branchNameToCommit = new TreeMap<>();
    // Filename of the Branch file.
    private static final String filename = "BRANCH";
//    public static String currbranch = "";

    /** Branch constructor inputs NAME. **/
    public Branches() {
    }

    public TreeMap<String, String> getBranchNameToCommit() {
        return branchNameToCommit;
    }

    public void updateBranch(String branchName, String commitID) {
        branchNameToCommit.put(branchName, commitID);
    }

    public static Branches getBranches(String branchName){
        File f = new File(branchName);
        return Utils.readObject(f, Branches.class);
    }

    public static String getCommitForBranch(String branchName){
        return branchNameToCommit.get(branchName);
    }


    public static void saveBranch(Branches b){
        File inFile = new File(filename);
        Utils.writeObject(inFile, b);
    }
}
