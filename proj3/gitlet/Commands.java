package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Commands implements Serializable {
    /**Current Working Directory.*/
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Main metadata folder*/
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /** * Directory folder that every commit */
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");
    /** * Directory folder that holds every blob */
    static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    /** * File that holds the Head commitID */
    static final File HEAD = Utils.join(GITLET_DIR, "HEAD");
    /** * File that holds the StagingArea object */
    static final File STAGING_AREA = Utils.join(GITLET_DIR, "STAGING_AREA");
    /** * File that holds the Branches object */
    static final File BRANCHES = Utils.join(GITLET_DIR, "BRANCHES");


    /** * File that holds the Head commitID */
    private String parentID1 = " ";
    /** * File that holds the Head commitID */
    private String timestamp = " ";
    /** * File that holds the Head commitID */
    private static String message = "";
    /** * File that holds the Head commitID */
    private final String stagingareafn = "STAGING_AREA";

    public String getStagingareafn() {
        return stagingareafn;
    }

    /**
     * Method for init command.
     */

    public void init() throws IOException, ClassNotFoundException {
        new File(".gitlet").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/merge").mkdir();
        new File(".gitlet/blobs").mkdir();
        join(".gitlet", "HEAD");
        join(".gitlet", "BRANCHES");
        join(".gitlet", "STAGING_AREA");
        StagingArea stage = new StagingArea();
        StagingArea.updateStage(stage.toAdd, stage.toRemove);
        StagingArea.saveStagingArea(stage);
        TreeMap<String, String> fileNametoBlobID = new TreeMap<>();
        message = "initial commit";
        Commit initialCommit = new Commit(message, fileNametoBlobID, null);
        String initialCommitID = initialCommit.getCommitID();
        Commit.saveCommit(initialCommit);
        Branches b = new Branches();
        b.updateBranch("master", initialCommitID);
        Branches.saveBranch(b);
        Head h = new Head();
        h.updateHead(initialCommitID, "master");
        Head.saveHead(h);
    }
    /**
     * Method to check if directory exists.
     */
    public boolean saveInit() {
        return GITLET_DIR.exists();
    }

    public boolean add(String fN) throws  NullPointerException {
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        File file = new File(fN);
        if (!file.exists()) {
            return false;
        }
        if (stage == null) {
            System.out.println("Stage doesn't exist.");
            System.exit(0);
        }
        try {
            if (stage.toAdd.containsKey(fN)) {
                System.out.println("Nothing to add.");
                return false;
            }
            if (stage.toRemove.containsKey(fN)) {
                stage.toRemove.remove(fN);
                StagingArea.updateStage(stage.toAdd, stage.toRemove);
                stage.saveStagingArea(stage);
                return true;
            }
            if (headCommit.fileNameToBlobID().containsKey(fN)){
                String s = headCommit.getFileNameToBlobID(fN);
                Blobs b = new Blobs(fN);
                if (s.equals(b.getBlobID())){
                    System.exit(0);
                }
            }
            Blobs bl = new Blobs(fN);
            stage.toAdd.put(fN, bl.getBlobID());
            Blobs.saveBlob(bl);
            StagingArea.updateStage(stage.toAdd, stage.toRemove);
            stage.saveStagingArea(stage);
            return true;
        } catch (NullPointerException excp) {
            System.out.println("Stage is empty");
            return false;
        }
    }

    public void rm(String fN) throws NullPointerException {
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        File f = Utils.join(CWD, fN);
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (stage.toAdd.size() != 0) { //if the file is not in the commit tracker but is staged
            if (stage.toAdd.containsKey(fN)) {
                stage.toAdd.remove(fN);
                StagingArea.updateStage(stage.toAdd, stage.toRemove);
                stage.saveStagingArea(stage);
                System.exit(0);
            }
        } else if (headCommit.fileNameToBlobID().containsKey(fN)) { //file isn't in commit tracker
            String blobId = headCommit.fileNameToBlobID().get(fN);
            stage.toRemove.put(fN, blobId); //file blob is
            StagingArea.updateStage(stage.toAdd, stage.toRemove);
            stage.saveStagingArea(stage);
            Utils.restrictedDelete(f);
            System.exit(0);
        } else {
            System.out.println("No reason to remove the file.");
        }

    }

    /**
     * Method to commit a file.
     */
    public void commit(String message) {
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        TreeMap<String, String> updatedContents = new TreeMap<>();
        if (stage.toAdd.isEmpty()) {
            if (stage.toRemove.isEmpty()) {
                System.out.println("No changes added to the commit.");
                System.exit(0);
            }
        }
        if (headCommit.fileNameToBlobID().isEmpty()) {
            for (String filename : stage.toAdd.keySet()) {
                String blobID = stage.toAdd.get(filename);
                updatedContents.put(filename, blobID);
                Blobs bl = new Blobs(filename);
                Blobs.saveBlob(bl);
            }
        } else {
            for (String filename : headCommit.fileNameToBlobID().keySet()) {
                String blobID = headCommit.getFileNameToBlobID(filename);
                updatedContents.put(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);
                Blobs.saveBlob(bl);
            }
            for (String filename : stage.toAdd.keySet()) {
                String blobID = stage.toAdd.get(filename);
                updatedContents.put(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);
                Blobs.saveBlob(bl);

            }
            for (String filename : stage.toRemove.keySet()) {
                String blobID = stage.toRemove.get(filename);
                updatedContents.remove(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);
                Blobs.saveBlob(bl);

            }
        }
        Commit next = new Commit(message, updatedContents, headCommitID);
        String newCommitID = next.getCommitID();
        Branches b = Branches.readBranches("BRANCHES");
        String branchName = "master";
        b.updateBranch(branchName, newCommitID);
        h.updateHead(newCommitID, branchName);
        Head.saveHead(h);
        Commit.saveCommit(next);
        Branches.saveBranch(b);
        stage.toAdd.clear();
        stage.toRemove.clear();
        stage.saveStagingArea(stage);
    }
    /**
     * Method to checkout.
     * @param fileName
     */
    public boolean checkout1(String fileName) {
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (headCommit.fileNameToBlobID().containsKey(fileName)) {
            String blobID = headCommit.getFileNameToBlobID(fileName);
            Blobs b = Blobs.getBlob(blobID);
            Blobs.saveBlob(b);
            writeContents(join(CWD, fileName), b.getcontentsstr());
            return true;
        }
        System.out.println("Files does not exist in that commit");
        return false;
    }
    /**
     * Method to checkout.
     * @param fN
     * @param commitID
     */
    public void checkout2(String commitID, String fN) {
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (!headCommit.fileNameToBlobID().containsKey(fN)) {
            System.out.println("File does not exist in that commit.1");
            System.exit(0);
        } else {
            Commit commitToCheckout = Commit.readCommit(commitID);
            if (commitToCheckout == null) {
                System.exit(0);
            }
            if (!commitToCheckout.fileNameToBlobID().containsKey(fN)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
            String blobID = commitToCheckout.getFileNameToBlobID(fN);
            Blobs b = Blobs.getBlob(blobID);
            Blobs.saveBlob(b);
            writeContents(join(CWD, fN), b.getcontentsstr());
        }
    }
    /**
     * Method to checkout.
     * @param branchName
     */
    public boolean checkout3(String branchName) {
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        Branches branches = Branches.readBranches(branchName);
        if (!branches.getBranchNameToCommit().containsKey(branchName)) {
            System.out.println("No such branch exists");
            return false;
        }
        try {
            String branchHeadCommitID = branches.getBranchNameToCommit().get(branchName);
            Commit branchHeadCommit = Commit.readCommit(branchHeadCommitID);
            for (String filename : branchHeadCommit.fileNameToBlobID().keySet()) {
                if (!headCommit.fileNameToBlobID().containsKey(filename)) {
                    System.out.println("File not tracked");
                    return false;
                }
            }
        }  catch (NullPointerException exception) {
            for (String filename : headCommit.fileNameToBlobID().keySet()) {
                String blobID = headCommit.fileNameToBlobID().get(filename);
                Blobs b = Blobs.getBlob(blobID);
                join(CWD, blobID).delete();
                Blobs.saveBlob(b);
                Head.saveHead(h);
            }
        }
        return true;
    }

    /**
     * Method to check the log.
     */
    public void log() {
        //for each commit in the branch
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        try {
            while (headCommit != null) {
                System.out.println("===");
                System.out.println("commit " + headCommitID);
                System.out.println("Date: " + headCommit.getTime());
                System.out.println(headCommit.getMessage());
                System.out.println("");
                headCommitID = headCommit.getParentID1();
                headCommit = Commit.readCommit(headCommitID);
            }
        }
        catch (NullPointerException excp) {
           System.exit(0);
        }
    }

    /**
     * Method for globalLog.
     */
    public boolean globalLog() {
        List<String> commitsList = plainFilenamesIn(COMMIT_DIR);
        for (String f : commitsList) {
            try {
                Commit commit = Commit.readCommit(f);
                commitFormat(commit.getMessage(), commit);
            } catch (NullPointerException excp) {
                return false;
            }
        }
        return true;
    }

    public void commitFormat(String headCommitID, Commit headCommit) {
        System.out.println("===");
        System.out.println("commit " + headCommitID);
        System.out.println("Date: " + headCommit.getTime());
        System.out.println(headCommit.getMessage());
        System.out.println(" ");
        System.out.print("===");
    }


    public boolean branch(String branchName) {
        Head h = Head.readHead();
        Branches b = Branches.readBranches("BRANCHES");
        String headCommitID = b.getCommitIDForBranch(branchName);
        Commit headCommit = Commit.readCommit(headCommitID);
        TreeMap<String, String> updatedContents = new TreeMap<>();
        if (b.getBranchNameToCommit().containsKey(branchName)) {
            System.out.println("Branch already exists.");
            return false;
        }
        for (String filename : headCommit.fileNameToBlobID().keySet()){
            String blobID = headCommit.getFileNameToBlobID(headCommitID);
            updatedContents.put(filename, blobID);
        }
        Commit next = new Commit(message, updatedContents, parentID1);
        b.updateBranch(branchName, next.getCommitID());
        h.updateHead(next.getCommitID(), branchName);
        Head.saveHead(h);
        Commit.saveCommit(next);
        Branches.saveBranch(b);
        return true;
    }

    public static void status() {
        StagingArea stage;
        stage = StagingArea.readStagingArea("STAGING_AREA");
        Branches b;
        b = Branches.readBranches("BRANCHES");
        Head h = Head.readHead();
        String headCommitID = h.getCommitID();
        System.out.println("=== Branches ===");
        for (String s : b.getBranchNameToCommit().keySet()) {
            if(b.getCommitIDForBranch(s).equals(headCommitID)){
                System.out.print("*");
            }
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("=== Staged Files ===");
        for (String s :  stage.getToAdd().keySet()) {
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        for (String s : stage.getToRemove().keySet()) {
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        //files in directory but not in toAdd or toRemove and not in STAGING AREA
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        System.out.println("");
    }


//    static void saveCommit(Commit commit, String filename) {
//        File f = new File(COMMIT_DIR + "/" + filename);
//        Utils.writeObject(f, filename);
//    }
//
//    static void saveHead(Head h) {
//        File f = new File(filename);
//        Utils.writeObject(f, filename);
//    }
//
//    static byte[] readCommit(File file) {
//        return readContents(file);
//    }



}
