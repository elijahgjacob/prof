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

    public static void updateStage(String stagingID, TreeMap<String, String> toAdd, TreeMap<String, String> toRemove) {
        toAdd = new TreeMap<>();
        toRemove = new TreeMap<>();
        stagingID = getHash(toAdd, toRemove);
    }


    public TreeMap<String, String> getToAdd(){
        return toAdd;
    }

    public TreeMap<String, String> getToRemove(){
        return toRemove;
    }

    /**
     * Returns hash generated.
     **/
    public static String getHash(TreeMap<String, String> toAdd, TreeMap<String, String> toRemove) {
        String var = "";
        for (Map.Entry<String, String> mapElement :
                toAdd.entrySet()) {
            String value = mapElement.getValue();
            var += value;
        }
        for (Map.Entry<String, String> mapElement :
                toRemove.entrySet()) {
            String value = mapElement.getValue();
            var += value;
        }
        String hash = Utils.sha1(var);
        return hash;
    }

    public static StagingArea readStagingArea(String stagingID) {
        StagingArea stage;
        File inFile = new File(".gitlet/"+stagingID);
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

