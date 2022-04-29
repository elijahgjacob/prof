package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Commands implements Serializable {
    boolean flag = false;
    /**
     * Var for current branch
     */
    private static String currbranch;
    /**
     * Var for current message
     */
    private static String message = "";
    /**
     * Var for branch
     */
    private static String branchname;
    /**
     * Current Working Directory.
     */
    static final File CWD = new File(".");
    /** Main metadata folder*/
    static final File GITLET_FOLDER = new File(CWD, ".gitlet");
    /** * Directory folder that contains all of the commit information */
    static final File COMMIT_DIR = join(GITLET_FOLDER, "commits");

    private final String head = "HEAD";
    private TreeMap<String, Blobs> blobs = new TreeMap<>(); //commitID, Blobs
    private TreeMap<String, String> branchmap = new TreeMap<>(); //branchname, commitID
    private String currBranch; // need to add it in the headfile
    private String parentID1 = " ";
    private String timestamp = " ";
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss YYYY Z");
    public final String stagingareafn = "StagingArea";
    public StagingArea stagingArea;
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
        Commit commit = new Commit("initial commit", blobs);
        Branches b = new Branches("master", commit.getcommitID());
        saveHead(commit, head);
        Branches.saveBranch(b);
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
            Head head = Head.getHead();
            String headCommitID = Head.getcommitID();
            Commit headcommit = Commit.readCommit(headCommitID);
            if (stage.toAdd.isEmpty()) {
                if (stage.toRemove.isEmpty()) {
                    System.out.println("Nothing to commit");
                    return false;
                }
            }
            TreeMap<String, Blobs> updatedContents = new TreeMap<>();
            for (String filename : headcommit.getBlobs().keySet()){
                updatedContents.put(filename, headcommit.getBlobs().get(filename));
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
            Branches b = Branches.getBranches(branchname);
            b.updateCommit(branchname, next.getcommitID());
            Head.writeHead(next.getcommitID());
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
        String headCommitID = Head.getcommitID();
        Commit headcommit = Commit.readCommit(headCommitID);
        if (headcommit.getBlobs().containsKey(filename)){
            Blobs b = headcommit.getBlobs().get(filename);
            //Utils.join(CWD, b.getBlobID());
            b.writeBlob();
            return true;
        }
        System.out.println("Files does not exist in that commit");
        return false;
    }

    public boolean checkout2 (String commitID, String filename) {
        Head head = Head.getHead();
        String headCommitID = Head.getcommitID();
        Commit headcommit = Commit.readCommit(headCommitID);
        if (headcommit.getBlobs().containsKey(filename)|| headcommit.getBlobs().containsKey(commitID)) {
            Blobs b = headcommit.getBlobs().get(filename);
            b.writeBlob();
            return true;
        }
        System.out.println("Files does not exist in that commit");
        return false;
    }

    public void checkout3(String branchname) {
        Branches.getBranches(branchname);
        if (branchmap.containsKey(branchname)){
            branchmap.get(branchname);
        }
        //Commit.writeCommit(commitc);
    }


    static void saveCommit(Commit commit, String filename) {
        File f = new File(COMMIT_DIR + "/" + filename);
        Utils.writeObject(f, filename);
    }

    static void saveHead(Commit commit, String filename) {
        File f = new File(GITLET_FOLDER + "/" + filename);
        Utils.writeObject(f, filename);
    }

    static byte[] readCommit(File file) {
        return readContents(file);
    }

    public void commitFormat() {
        System.out.println("===");
        //System.out.println("commit" + this.commit(message));
        System.out.println("Date: " + this.timestamp);
        System.out.println(this.message);
        System.out.print("===");
    }
}