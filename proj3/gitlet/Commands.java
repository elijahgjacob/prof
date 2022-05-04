package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Commands implements Serializable {
    /**
     * Current Working Directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * Main metadata folder
     */
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /**
     * Directory folder that every commit
     */
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");
    /**
     * Directory folder that holds every blob
     */
    static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    /**
     * File that holds the Head commitID
     */
    static final File HEAD = Utils.join(GITLET_DIR, "HEAD");
    /**
     * File that holds the StagingArea object
     */
    static final File STAGING_AREA = Utils.join(GITLET_DIR, "STAGING_AREA");
    /**
     * File that holds the Branches object
     */
    static final File BRANCHES = Utils.join(GITLET_DIR, "BRANCHES");


    /**
     * File that holds the Head commitID
     */
    private String parentID1 = " ";
    /**
     * File that holds the Head commitID
     */
    private String timestamp = " ";
    /**
     * File that holds the Head commitID
     */
    private static String message = "";
    /**
     * File that holds the Head commitID
     */
    private final String STAGINGAREAFN = "STAGING_AREA";

    private final String HEADFN = "HEAD";

    private final String BRANCHESFN = "BRANCHES";

    public String getStagingAreaFn() {
        return STAGINGAREAFN;
    }

    /**
     * Method for init command.
     */

    public void init() throws IOException, ClassNotFoundException {
        new File(".gitlet").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/merge").mkdir();
        new File(".gitlet/blobs").mkdir();
        join(".gitlet", HEADFN);
        join(".gitlet", BRANCHESFN);
        join(".gitlet", STAGINGAREAFN);
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

    public boolean add(String fN) throws NullPointerException {
        StagingArea stage = StagingArea.readStagingArea(STAGINGAREAFN);
        Head h = Head.readHead(HEADFN);
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
            if (headCommit.fileNameToBlobID().containsKey(fN)) {
                String s = headCommit.getFileNameToBlobID(fN);
                Blobs b = new Blobs(fN);
                if (s.equals(b.getBlobID())) {
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
        StagingArea stage = StagingArea.readStagingArea(STAGINGAREAFN);
        File f = Utils.join(CWD, fN);
        Head h = Head.readHead(HEADFN);
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
        StagingArea stage = StagingArea.readStagingArea(STAGINGAREAFN);
        Head h = Head.readHead(HEADFN);
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
        Branches b = Branches.readBranches(BRANCHESFN);
        String branchName = h.getBranchName();
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
     *
     * @param fileName
     */
    public boolean checkout1(String fileName) {
        Head h = Head.readHead(HEADFN);
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (headCommit.fileNameToBlobID().containsKey(fileName)) {
            String blobID = headCommit.getFileNameToBlobID(fileName);
            Blobs b = Blobs.getBlob(blobID);
            Blobs.saveBlob(b);
            writeContents(join(CWD, fileName), b.getcontentsstr());
            return true;
        }
        System.out.println("File does not exist in that commit");
        return false;
    }

    /**
     * Method to checkout.
     *
     * @param fN
     * @param commitID
     */
    public void checkout2(String commitID, String fN) {
        List<String> commitsList = plainFilenamesIn(COMMIT_DIR);
        Head h = Head.readHead(HEADFN);
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (!commitsList.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit c = Commit.readCommit(commitID);
        if (!c.fileNameToBlobID().containsKey(fN)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            for (String headFName : headCommit.fileNameToBlobID().keySet()) {
                if (headCommit.fileNameToBlobID().containsKey(headFName)) {
                    join(CWD, headFName).delete();
                }
                String blobID = c.getFileNameToBlobID(fN);
                Blobs b = Blobs.getBlob(blobID);
                writeContents(join(CWD, fN), b.getcontentsstr());
            }

        }
    }

    /**
     * Method to checkout.
     *
     * @param branchName
     */
    public void checkout3(String branchName) {
        boolean untrackedFileIsPresent = false;
        Head h = Head.readHead(HEADFN);
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        Branches branches = Branches.readBranches(BRANCHESFN);
        if (branches.getCommitIDForBranch(branchName) == null) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (h.getBranchName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        String branchHeadCommitID = branches.getBranchNameToCommit().get(branchName);
        Commit branchHeadCommit = Commit.readCommit(branchHeadCommitID);
        for (String filename : branchHeadCommit.fileNameToBlobID().keySet()) {
            File f = new File(filename);
            if (!headCommit.fileNameToBlobID().containsKey(filename) && f.exists()) {
                untrackedFileIsPresent = true;
                break;
            }
        }
        if (untrackedFileIsPresent) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
        for (String fN : headCommit.fileNameToBlobID().keySet()) {
            if (!branchHeadCommit.fileNameToBlobID().containsKey(fN)) {
                join(CWD, fN).delete();
            }
        }
        for (String fN : branchHeadCommit.fileNameToBlobID().keySet()) {
            String blobID = branchHeadCommit.fileNameToBlobID().get(fN);
            Blobs b = Blobs.getBlob(blobID);
            writeContents(join(CWD, fN), b.getcontentsstr());
            Blobs.saveBlob(b);
        }
        h.updateHead(branchHeadCommitID, branchName);
        branches.updateBranch(branchName, branchHeadCommitID);
        Head.saveHead(h);
        System.exit(0);
    }

    /**
     * Method to check the log.
     */
    public void log() {
        //for each commit in the branch
        Head h = Head.readHead(HEADFN);
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
        } catch (NullPointerException excp) {
            System.exit(0);
        }
    }

    /**
     * Method for globalLog.
     */
    public void globalLog() {
        List<String> commitsList = plainFilenamesIn(COMMIT_DIR);
        for (String s : commitsList) {
            Commit c = Commit.readCommit(s);
            System.out.println("===");
            System.out.println("commit " + s);
            System.out.println("Date: " + c.getTime());
            System.out.println(c.getMessage());
            System.out.println("");
        }
    }

    public void find(String message) {
        List<String> commitsList = plainFilenamesIn(COMMIT_DIR);
        boolean exists = false;
        for (String s : commitsList) {
            if (Commit.readCommit(s).getMessage().equals(message)) {
                System.out.println(s);
                exists = true;
            }
        }
        if (!exists) {
            System.out.println("Found no commit with that message.");
        }
    }


    public void reset(String commitID) { //incomplete
        List<String> commitsList = plainFilenamesIn(COMMIT_DIR);
        boolean exists = false;
        for (String f : commitsList) {
            if (f.equals(commitID)) {
                exists = true;
                break;
            }
        }
        Commit c = Commit.readCommit(commitID);
        Head h = new Head();
        Commit headCommit = Commit.readCommit(h.getCommitID());
        String currBranchName = h.getBranchName();
        Branches branches = Branches.readBranches(BRANCHESFN);
        StagingArea stage = StagingArea.readStagingArea(STAGINGAREAFN);
        for (String fN : c.fileNameToBlobID().keySet()) {
            File f = new File(fN);
            if (!headCommit.fileNameToBlobID().containsKey(fN) && f.exists()) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                Utils.join(CWD, fN).delete();
            } else {
                String blobID = c.getFileNameToBlobID(fN);
                Blobs b = Blobs.getBlob(blobID);
                h.updateHead(commitID, currBranchName);
                branches.updateBranch(currBranchName, commitID);
                writeContents(join(CWD, fN), b.getcontentsstr());
            }
        }
        if (exists) {
            stage.toAdd.clear();
            stage.toRemove.clear();
            StagingArea.saveStagingArea(stage);
            Branches.saveBranch(branches);
        } else {
            System.out.println("Found no commit with that message.");
        }
    }


    public void branch(String branchNameB) {
        Head h = Head.readHead(HEADFN);
        String headCommitID = h.getCommitID();
        Branches b = Branches.readBranches(BRANCHESFN);
        if (b.getCommitIDForBranch(branchNameB) != null) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            b.updateBranch(branchNameB, headCommitID);
            Branches.saveBranch(b);
            System.exit(0);
        }
    }

    public void rmBranch(String branchNameB) {
        Head h = Head.readHead(HEADFN);
        Branches b = Branches.readBranches(BRANCHESFN);
        if (b.getCommitIDForBranch(branchNameB) == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (h.getBranchName().equals(branchNameB)){
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        else {
            b.removeBranch(branchNameB);
            Branches.saveBranch(b);
            System.exit(0);
        }
    }

    public void status() {
        StagingArea stage;
        stage = StagingArea.readStagingArea("STAGING_AREA");
        Branches b;
        b = Branches.readBranches(BRANCHESFN);
        Head h = Head.readHead(HEADFN);
        String headCommitID = h.getCommitID();
        System.out.println("=== Branches ===");
        for (String s : b.getBranchNameToCommit().keySet()) {
            if (b.getCommitIDForBranch(s).equals(headCommitID)) {
                System.out.print("*");
            }
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("=== Staged Files ===");
        for (String s : stage.getToAdd().keySet()) {
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
