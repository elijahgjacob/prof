package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Commands implements Serializable {
    /** Var for current message*/
    private static String message = "";
    /**Current Working Directory.*/
    static final File CWD = new File(".");
    /** Main metadata folder*/
    static final File GITLET_FOLDER = new File(CWD, ".gitlet");
    /** * Directory folder that contains all of the commit information */
    static final File COMMIT_DIR = join(GITLET_FOLDER, "commits");
    private static final String head = "HEAD";
    static final File HEAD = join(GITLET_FOLDER, head);

    private TreeMap<String, Blobs> blobs = new TreeMap<>(); //commitID, Blobs
    private TreeMap<String, String> branchmap = new TreeMap<>(); //branchname, commitID
    private String currBranch; // need to add it in the headfile
    private String parentID1 = " ";
    private String timestamp = " ";
    public final String stagingareafn = "StagingArea";
    /**
     * Method for init command
     *
     * @return t/f
     */

    public boolean init() {
        if (GITLET_FOLDER.exists()) {
            System.out.println("Directory already exists");
            return false;
        }
        new File(".gitlet").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/branches").mkdir();
        new File(".gitlet/merge").mkdir();
        StagingArea stage = new StagingArea();
        stage.writeStagingArea();
        Commit initialCommit = new Commit("initial commit", blobs);
        currBranch = "master";
        Branches b = new Branches();
        b.updateBranch(currBranch, initialCommit.getCommitID());
        Branches.saveBranch(b);
        Head h = new Head();
        h.updateHead(initialCommit.getCommitID(), currBranch);
        Head.saveHead(h);
        return true;
    }

    public boolean add(String filename) {
        StagingArea stage = StagingArea.readStagingArea(stagingareafn);
        File file = new File(filename);
        if (file.exists()) {
            if (stage.toAdd.containsKey(filename)) {
                System.out.println("Nothing to add.");
                return false;
            }
            if (stage.toRemove.containsKey(filename)) {
                stage.toRemove.remove(filename);
                stage.writeStagingArea();
                return true;
            }
            Blobs b = new Blobs(filename);
            stage.toAdd.put(filename, b.getBlobID());
            stage.writeStagingArea();
            return true;
        } else {
            System.out.println("File does not exist");
        }
        return false;
    }

        public boolean commit (String message){
            StagingArea stage = StagingArea.readStagingArea(stagingareafn);
            Head h = Head.getHead();
            String headCommitID = Head.getCommitID();
            Commit headcommit = Commit.readCommit(headCommitID);
            if (stage.toAdd.isEmpty()) {
                if (stage.toRemove.isEmpty()) {
                    System.out.println("Nothing to commit");
                    return false;
                }
            }
            TreeMap<String, Blobs> updatedContents = new TreeMap<>();
            for (String filename : headcommit.getFilenameToBlobID().keySet()){
                String blobID = headcommit.getFilenameToBlobID().get(filename);
                updatedContents.put(filename, Blobs.readBlob(blobID));
            }
            for (String filename : stage.toAdd.keySet()){
                String blobID = stage.toAdd.get(filename);
                Blobs b = Blobs.readBlob(blobID);
                updatedContents.put(filename, b);
            }
            for (String filename : stage.toRemove.keySet()){
                String blobID = stage.toRemove.get(filename);
                Blobs b = Blobs.readBlob(blobID);
                updatedContents.remove(filename, b);
            }
            Commit next = new Commit(message, updatedContents);
            Branches b = Branches.getBranches(branchname); //fix
            b.updateBranch(branchname, next.getCommitID());
            h.updateHead(next.getCommitID(), branchname);
            Head.saveHead(h);
            Commit.writeCommit(next);
            Branches.saveBranch(b);
            stage.toAdd.clear();
            stage.toRemove.clear();
            stage.writeStagingArea();
            return true;
        }


    public boolean log() {
        commitFormat();
        return true;
    }

    public boolean checkout1(String filename) {
        Head head = Head.getHead();
        String headCommitID = Head.getCommitID();
        Commit headcommit = Commit.readCommit(headCommitID);
        if (headcommit.getFilenameToBlobID().containsKey(filename)){
            String blobID = headcommit.getFilenameToBlobID().get(filename);
            Blobs b = Blobs.readBlob(blobID);
            b.writeBlob();
            return true;
        }
        System.out.println("Files does not exist in that commit");
        return false;
    }

    public boolean checkout2 (String commitID, String filename) {
        Head head = Head.getHead();
        String headCommitID = Head.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        if (!headCommit.getFilenameToBlobID().containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return false;
        } else {
            Commit commitToCheckout = Commit.readCommit(commitID);
            if (commitToCheckout == null) {
                return false;
            }
            if (!commitToCheckout.getFilenameToBlobID().containsKey(filename)){
                System.out.println("File does not exist in that commit.");
                return false;
            }
            String blobID = commitToCheckout.getFilenameToBlobID().get(filename);
            Blobs b = Blobs.readBlob(blobID);
            b.writeBlob();
            return true;
        }
    }

    public boolean checkout3(String branchName) {
        Head head = Head.getHead();
        String headCommitID = Head.getCommitID();
        Commit headCommit = Commit.readCommit(headCommitID);
        Branches BRANCHES = Branches.getBranches(branchName);
        if (!BRANCHES.getBranchNameToCommit().containsKey(branchName)) {
            System.out.println("No such branch exists");
            return false;
        }
        try {
            // commitID for the commit at the front of current branch.
            String branchHeadCommitID = BRANCHES.getBranchNameToCommit().get(branchName);
            Commit branchHeadCommit = Commit.readCommit(branchHeadCommitID);
            for (String filename : branchHeadCommit.getFilenameToBlobID().keySet()) {
                if (!headCommit.getFilenameToBlobID().containsKey(filename)) {
                    System.out.println("File not tracked");
                    return false;
                }
            }
        } catch (NullPointerException exception) {
            for (String filename : headCommit.getFilenameToBlobID().keySet()) {
                String blobID = headCommit.getFilenameToBlobID().get(filename);
                Blobs b = Blobs.readBlob(blobID);
                join(CWD, blobID).delete();
                // head
                b.writeBlob();
                //Commit.writeCommit(commit);
            }
        }
        return true;
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

    public void commitFormat() {
        System.out.println("===");
        //System.out.println("commit" + this.commit(message));
        System.out.println("Date: " + this.timestamp);
        System.out.println(this.message);
        System.out.print("===");
    }
}