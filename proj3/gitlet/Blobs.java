package gitlet;

import java.io.*;
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

    /** Returns hash generated. **/
    public String hash() {
        String var = "";
        var+= name;
        var+= contents;
        return Utils.sha1(var);
    }

    public static Blobs getBlob(String blobID) {
        Blobs blob;
        File inFile = new File(blobID);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            blob = (Blobs) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            blob = null;
        }
        return blob;
    }

    public void saveBlob(){
        File inFile = new File(blobID);
        Utils.writeContents(inFile, this);
    }
}
