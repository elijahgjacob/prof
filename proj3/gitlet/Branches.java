package gitlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.TreeMap;
import java.io.File;


public class Branches implements Serializable {
    /**
     * <String, String> <branchname, commitID>.
     **/
    private TreeMap<String, String> branchNameToCommit;
    private final String BRANCHES = "BRANCHES";
    /**
     * Branch constructor.
     **/
    public Branches() {
        this.branchNameToCommit = new TreeMap<>();
    }

    /**
     * Method returns branchName to Commit TreeMap.
     **/
    public TreeMap<String, String> getBranchNameToCommit() {
        return this.branchNameToCommit;
    }

    /**
     * Method gets the commit ID of the latest commit on that branch.
     *
     * @param branchName
     **/
    public String getCommitIDForBranch(String branchName) {
        return this.branchNameToCommit.get(branchName);
    }

    /**
     * Method adds the commit by commitID to the branchName.
     *
     * @param branchName
     * @param commitID
     **/
    public void updateBranch(String branchName, String commitID) {
        branchNameToCommit.put(branchName, commitID);
    }

    /**
     * Method adds the commit by commitID to the branchName.
     *
     * @param commitID
     **/
    public String getBranchName(String commitID) {
        try {
            for (String s : branchNameToCommit.keySet()) {
                if (branchNameToCommit.get(s).equals(commitID)) {
                    return s;
                }
            }
        } catch (NullPointerException excp) {
            System.out.println("Did not find branch.");
        }
        return " ";
    }

    /**
     * Returns the Branch object with that branchName
     */
    public static Branches readBranches(String BRANCHES) {
        Branches b;
        File inFile = new File(".gitlet/"+ BRANCHES);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            b = (Branches) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            b = null;
        }
        return b;
    }

    public static void saveBranch(Branches b) {
        Utils.writeObject(Commands.BRANCHES, b);
    }
}
