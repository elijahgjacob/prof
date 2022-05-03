package gitlet;

import org.checkerframework.checker.units.qual.K;

import java.io.*;
import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


public class StagingArea implements Serializable {

    public TreeMap<String, String> toAdd = new TreeMap<String, String>(); //filename, blobID
    public TreeMap<String, String> toRemove = new TreeMap<String, String>();
    public String stagingID = "STAGING_AREA";

    /**
     * StagingArea CONSTRUCTOR
     **/
    public StagingArea(){
    }

    /**
     * Method adds the commit by commitID to the branchName.
     * @param toAdd
     * @param toRemove
     **/
    public static void updateStage(TreeMap<String, String> toAdd, TreeMap<String, String> toRemove) {
        toAdd = new TreeMap<>();
        toRemove = new TreeMap<>();
    }


    public TreeMap<String, String> getToAdd(){
        return toAdd;
    }

    public TreeMap<String, String> getToRemove(){
        return toRemove;
    }


    public static StagingArea readStagingArea(String stagingID) {
        StagingArea stage;
        File inFile = new File(".gitlet/"+ stagingID);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            stage = (StagingArea) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            stage = null;
        }
        return stage;
    }

    public static void saveStagingArea(StagingArea stage) {
        Utils.writeObject(Commands.STAGING_AREA, stage);
    }
}

