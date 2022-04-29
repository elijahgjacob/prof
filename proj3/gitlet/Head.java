package gitlet;

import java.io.File;
import java.io.Serializable;

public class Head implements Serializable {
    private static String filename = "HEAD";
    private static String commitID;
    private static String branchname;

    /** Head constructor inputs NAME. **/
    public Head(String commitID) {
        this.commitID = commitID;
    }

    public static String getcommitID(){
        return commitID;
    }

    public static Head getHead(){
        File f = new File(filename);
        return Utils.readObject(f, Head.class);
    }

    public static void writeHead(String commitID){
        File inFile = new File(filename);
        Utils.writeObject(inFile, commitID);
    }
}
