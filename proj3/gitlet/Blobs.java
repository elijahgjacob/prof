package gitlet;

import java.io.*;
import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    public Blobs(String name) {
        File file = new File(name);
        this.name = name;
        this.contents = Utils.readContents(file);
        this.contentstr = Utils.readContentsAsString(file);
        this.blobID = hash();
    }

    public String getBlobID(){
        return this.blobID;
    }

    public void setBlobID(String id){
        this.blobID = id;
    }

    /** Returns hash generated. **/
    public String hash() {
        String var = "";
        var+= name;
        var+= contents;
        var+= contentstr;
        return Utils.sha1(var);
    }

    public static Blobs readBlob(String blobID) {
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

    public void writeBlob(){
        File inFile = new File(blobID);
        Utils.writeContents(inFile, this);
    }
}
