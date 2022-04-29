package gitlet;


import java.io.*;
import java.util.TreeMap;

public class Branches implements Serializable {
    private TreeMap<String, String> branchmap = new TreeMap<>();
    private static String filename = "BRANCH";

    /** Branch constructor inputs NAME. **/
    public Branches(String branchname, String commitID) {
        this.branchmap.put(branchname, commitID);
    }

    public void updateCommit(String branchname, String commitID) {
        this.branchmap.put(branchname, commitID);
    }

    public static Branches getBranches(String branchname){
        File f = new File(branchname);
        return Utils.readObject(f, Branches.class);
    }

    public static void saveBranch(Branches branchmap){
        File inFile = new File(filename);
        Utils.writeObject(inFile, branchmap);
    }
}
