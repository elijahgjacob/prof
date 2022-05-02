package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Commands implements Serializable {
    /**Current Working Directory.*/
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Main metadata folder*/
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    static final File BRANCH_DIR = Utils.join(GITLET_DIR, "branches");
    /** * Directory folder that every commit */
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");
    /** * Directory folder that holds every blob */
    static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");

    /** * File that holds the Head commitID */
    static final File HEAD = Utils.join(GITLET_DIR, "HEAD");
    static final File master = Utils.join(BRANCH_DIR, "master");
    static final File STAGING_AREA = Utils.join(GITLET_DIR, "STAGING_AREA");


    private String parentID1 = " ";
    private String timestamp = " ";
    private static String message = "";

    public final String stagingareafn = "STAGING_AREA";

    /**
     * Method for init command
     * @return t/f
     */

    public void init() throws IOException, ClassNotFoundException {
        new File(".gitlet").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/branches").mkdir();
        new File(".gitlet/merge").mkdir();
        new File(".gitlet/blobs").mkdir();
        join(".gitlet", "HEAD");
        join(".gitlet", "STAGING_AREA");
        join(BRANCH_DIR, "master");
        StagingArea stage = new StagingArea();
        StagingArea.updateStage(stage.stagingID, stage.toAdd, stage.toRemove);
        StagingArea.saveStagingArea(stage);
        TreeMap<String, String> commitIDtoFileName = new TreeMap<>();
        Commit initialCommit = new Commit("initial commit", new TreeMap<>(), null);
        String initialCommitID = initialCommit.getCommitID();
        Commit.saveCommit(initialCommit);
        TreeMap<String, String> fileNametoBlobID = new TreeMap<>();
        Branches b = new Branches();
        b.updateBranch("master", initialCommitID);
        Branches.saveBranch(b);
        Head h = new Head();
        h.updateHead(initialCommitID, "master");
        Head.saveHead(h);
    }

    public boolean saveInit(){
        return GITLET_DIR.exists();
    }

    public boolean add(String filename) throws IOException, NullPointerException {
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        File file = new File(filename);
        if (stage == null){
            System.out.println("Stage doesn't exist.");
            System.exit(0);
        }
        try {
            if (stage.toAdd.size() != 0) {
                if (stage.toAdd.containsKey(filename)) {
                    System.out.println("Nothing to add.");
                    return false;
                }
            }
            if (stage.toRemove.size() != 0) {
                if (stage.toRemove.containsKey(filename)) {
                    stage.toRemove.remove(filename);
                    StagingArea.updateStage(stage.stagingID, stage.toAdd, stage.toRemove);
                    stage.saveStagingArea(stage);
                }
            }
            Blobs bl = new Blobs(filename);
            stage.toAdd.put(filename, bl.getBlobID());
            Blobs.saveBlob(bl);
            StagingArea.updateStage(stage.stagingID, stage.toAdd, stage.toRemove);
            stage.saveStagingArea(stage);
            return true;
        } catch (NullPointerException excp) {
            System.out.println("Stage is empty");
            return false;
        }
    }


    public void commit (String message){
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        Head h = Head.getHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        TreeMap<String, String> updatedContents = new TreeMap<>();
        if (stage.toAdd.isEmpty()) {
            if (stage.toRemove.isEmpty()) {
                System.out.println("Nothing to add");
                System.exit(0);
            }
        }
        if (headCommit.fileNameToBlobID().isEmpty()) {
            for (String filename : stage.toAdd.keySet()){
                String blobID = stage.toAdd.get(filename);
                updatedContents.put(filename, blobID);
                Blobs bl = new Blobs(filename);// need to reconstruct the blob from the headCommit with the same filename
                Blobs.saveBlob(bl);
            }
        } else {
            for (String filename : headCommit.fileNameToBlobID().keySet()) { //goes through all files in the commit file
                String blobID = headCommit.getFileNameToBlobID(filename); //gets the blobID of each file
                updatedContents.put(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);// need to reconstruct the blob from the headCommit with the same filename
                Blobs.saveBlob(bl);//puts the files in a new hashmap
            }
            for (String filename : stage.toAdd.keySet()) {
                String blobID = stage.toAdd.get(filename);
                updatedContents.put(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);// need to reconstruct the blob from the headCommit with the same filename
                Blobs.saveBlob(bl);

            }
            for (String filename : stage.toRemove.keySet()) {
                String blobID = stage.toRemove.get(filename);
                updatedContents.remove(filename, blobID);
                Blobs bl = Blobs.getBlob(blobID);// need to reconstruct the blob from the headCommit with the same filename
                Blobs.saveBlob(bl);

            }
        }
        Commit next = new Commit(message, updatedContents, headCommitID);
        String newCommitID = next.getCommitID();
        String newBranchName= Branches.getBranchName(newCommitID);
        Branches b = new Branches(); //fix
        b.updateBranch(newBranchName, newCommitID);
        h.updateHead(newCommitID, newBranchName);
        Head.saveHead(h);
        Commit.saveCommit(next);
        Branches.saveBranch(b);
        stage.toAdd.clear();
        stage.toRemove.clear();
        stage.saveStagingArea(stage);
    }

    public boolean checkout1(String filename) {
        Head h = Head.getHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (headCommit.fileNameToBlobID().containsKey(filename)){
            String blobID = headCommit.getFileNameToBlobID(filename);
            Blobs b = Blobs.getBlob(blobID);// need to reconstruct the blob from the headCommit with the same filename
            Blobs.saveBlob(b);
            writeContents(join(CWD, filename), b.getcontentsstr());
            return true;
        }
        System.out.println("Files does not exist in that commit");
        return false;
    }

    public boolean checkout2 (String commitID, String filename) {
        Head h = Head.getHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (!headCommit.fileNameToBlobID().containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return false;
        } else {
            Commit commitToCheckout = Commit.readCommit(commitID);
            if (commitToCheckout == null) {
                return false;
            }
            if (!commitToCheckout.getFileNameToBlobID(commitID).equals(filename)){
                System.out.println("File does not exist in that commit.");
                return false;
            }
            String blobID = commitToCheckout.getFileNameToBlobID(commitID);
            Blobs b = Blobs.getBlob(blobID);
            Blobs.saveBlob(b);
            return true;
        }
    }

    public boolean checkout3(String branchName) {
        Head h = Head.getHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        Branches BRANCHES = Branches.getBranches(branchName);
        if (!BRANCHES.getBranchNameToCommit().containsKey(branchName)) {
            System.out.println("No such branch exists");
            return false;
        } try {
            // commitID for the commit at the front of current branch.
            String branchHeadCommitID = BRANCHES.getBranchNameToCommit().get(branchName);
            Commit branchHeadCommit = Commit.readCommit(branchHeadCommitID);
            for (String filename : branchHeadCommit.fileNameToBlobID().keySet()) {
                if (!headCommit.fileNameToBlobID().containsKey(filename)) {
                    System.out.println("File not tracked");
                    return false;
                }
            }
        } catch (NullPointerException exception) {
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


    public void log() {
        //for each commit in the branch
        Head h = Head.getHead();
        String headCommitID = h.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        while (headCommit != null){
            System.out.println("===");
            System.out.println("commit " + headCommitID);
            System.out.println("Date: " + headCommit.getTime());
            System.out.println(headCommit.getMessage());
            System.out.println(" ");
            System.out.print("===");
            Commit headCommit = Commit.readCommit(headCommit.getParentID1());
            }
        }


    public boolean globalLog(){
        List<String> commitsList= plainFilenamesIn(COMMIT_DIR);
        for (String f : commitsList){
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


    public boolean branch(String branchName){
        Head h = Head.getHead();
        String headCommitID = Branches.getCommitIDForBranch(branchName);
        Commit headCommit = Commit.readCommit(headCommitID);
        TreeMap<String, String> updatedContents = new TreeMap<>();
        if (Branches.getBranchNameToCommit().containsKey(branchName)) {
            System.out.println("Branch already exists.");
            return false;
        }
        for (String filename : headCommit.fileNameToBlobID().keySet()){ //goes through all files in the commit file
            String blobID = headCommit.getFileNameToBlobID(headCommitID); //gets the blobID of each file
            updatedContents.put(filename, blobID); //puts the files in a new hashmap
        }
        Commit next = new Commit(message, updatedContents, parentID1);
        Branches b = new Branches();
        b.updateBranch(branchName, next.getCommitID());
        h.updateHead(next.getCommitID(), branchName);
        Head.saveHead(h);
        Commit.saveCommit(next);
        Branches.saveBranch(b);
        return true;
    }

    public static void status() {
        StagingArea stage = new StagingArea();
        Head h = Head.getHead();
        System.out.println("=== Branches ===");
        for (String s : Branches.getBranchNameToCommit().keySet()) {
            if (s.equals(h)) {
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
        for (String s : stage.getToRemove().keySet()){
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
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