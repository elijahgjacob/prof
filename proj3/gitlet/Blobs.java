package gitlet;

import java.io.*;
import java.sql.Blob;
import java.util.TreeMap;

public class Blobs implements Serializable {
    /**Name of file. **/
    private String name;
    /** Hash of file. **/
    private String blobID;
    /** Contents as a byte array. **/
    private byte[] contents;
    /** Contents as a string for testing. **/
    private String contentstr;
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main folder. */
    static final File GITLET_DIR = new File(CWD,".gitlet");

    /** Directory folder that contains each commit hash*/
    static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    /** Blob constructor inputs NAME. **/
    public Blobs(String filename) {
        File f = new File(filename);
        this.name = filename;
        this.contents = Utils.readContents(f);
        this.contentstr = Utils.readContentsAsString(f);
        this.blobID = hash();
    }

    public String getBlobID(){
        return this.blobID;
    }

    public String getcontentsstr(){
        return this.contentstr;
    }

    /** Returns hash generated. **/
    public String hash() {
        String hash= Utils.sha1(Utils.serialize(contents));
        return hash;
    }

    public static Blobs getBlob(String blobID) {
        Blobs b;
        File inFile = Utils.join(".gitlet/blobs/", blobID);
        b = Utils.readObject(inFile, Blobs.class);
        return b;
    }

    public static void saveBlob(Blobs b){
        File inFile = new File(BLOBS_DIR, b.getBlobID());
        Utils.writeObject(inFile, b);
    }
}
