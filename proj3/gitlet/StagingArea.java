package gitlet;

import org.checkerframework.checker.units.qual.K;

import java.io.*;
import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class StagingArea implements Serializable {

    public HashMap<String, String > toAdd = new HashMap<String, String>(); //filename, blobID
    public HashMap<String, String > toRemove = new HashMap<String, String>();

    public String stagingID = " ";
    /** Blob constructor inputs NAME. **/
    public void StagingArea() {
        stagingID = getHash();
    }

    public void StagingArea(String stagingID) {
    }

    /** Returns hash generated. **/
    public String getHash() {
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
        return Utils.sha1(var);
    }

    public static StagingArea readStagingArea(String stagingID) {
        StagingArea stage;
        File inFile = new File(stagingID);
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

    public void writeStagingArea(){
        File inFile = new File(stagingID);
        Utils.writeObject(inFile, this);
    }

}
