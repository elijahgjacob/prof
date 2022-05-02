package gitlet;

import java.io.*;

public class Head implements Serializable {
    private static final String filename = "HEAD";
    private String newCommitID;
    private String newBranchName;


    /** Constructor takes in the branch that is checked out and the front commit of that branch. **/
    public Head(){
    }

//    public static String getCommitID(){
//        return newCommitID;
//    }

//    public String getHash(String newCommitID, String newBranchName){
//        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
//            objectStream.writeObject(this);
//            objectStream.close();
//            return Utils.sha1(stream.toByteArray());
//        } catch (IOException excp){
//            throw new IllegalArgumentException( ("Error occurred during hash"));
//        }
//    }

    // updateHead updates the HEAD contents and publishes to filesystem.
    public void updateHead(String newCommitID, String newBranchName){
        this.newCommitID = newCommitID;
        this.newBranchName = newBranchName;
//        headID = this.getHash(newCommitID, newBranchName);
    }

    public static Head getHead(){
        Head head;
        File inFile = new File(".gitlet/"+filename);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            head = (Head) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            head = null;
        }
        return head;
    }


    public static void saveHead(Head head){
        Utils.writeObject(Commands.HEAD, head);
    }
}
