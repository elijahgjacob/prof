package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Elijah G. Jacob
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) throws IOException, ClassNotFoundException {
        String first = args[0];
        Commands c = new Commands();
        if (args.length == 0) {
            System.out.println("Must have at least one argument");
            System.exit(0);
        }
        switch (first) {
            case "init":
                if (args.length > 1){
                    System.out.println("Please enter the right amount of arguments.");
                    System.exit(0);
                }
                else if (c.saveInit()){
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);
                }
                c.init();
                break;
            case "add":
                String filename = args[1];
                while (!c.add(filename)) {
                    if (!c.saveInit()) {
                        System.out.println("Directory not initialized");
                        System.exit(0);
                    }
                    if (args.length != 2) {
                        System.out.println("Please enter the right amount of arguments");
                        System.exit(0);
                    }
                    if (!c.add(args[1])) {
                        System.out.println("File does not exist.");
                        System.exit(0);
                    }
                    c.add(filename);
                }
                break;
            case "commit":
                String message = args[1];
                if (!c.saveInit()) {
                    System.out.println("Directory not initialized");
                    System.exit(0);
                }
                if (args.length == 1) {
                    System.out.println("Please enter a commit message");
                    System.exit(0);
                }
                if (args.length > 2) {
                    System.out.println("Please enter the right amount of arguments");
                    System.exit(0);
                }
                c.commit(message);
                break;
            case "checkout":
                if (args.length == 3) {
                    String checkoutFN = args[2];
                    if (!c.saveInit()) {
                        System.out.println("Directory not initialized");
                        System.exit(0);
                    }
                    c.checkout1(checkoutFN);
                }
                if (args.length == 4){
                    String commitID  = args[1];
                    String checkoutFN1 = args[3];
                    if (!c.saveInit()) {
                        System.out.println("Directory not initialized");
                        System.exit(0);
                    }
                    c.checkout2(commitID, checkoutFN1);
                }
                break;
            case "rm":
                String fN = args[1];
                if (!c.saveInit()) {
                    System.out.println("Directory not initialized");
                    System.exit(0);
                }
                if (args.length != 2) {
                    System.out.println("Please enter the right amount of arguments");
                    System.exit(0);
                }
                c.rm(fN);
                break;

            case "log":
                if (args.length > 2){
                    System.out.println("Please enter git --log only");
                    System.exit(0);
                }
                c.log();
                break;
            case "status":
                if (!c.saveInit()) {
                    System.out.println("Directory not initialized");
                    System.exit(0);
                }
                c.status();
                break;
//                if (args.length > 2) {
//                    System.out.println("Please enter 'git status' only");
//                    System.exit(0);
//                }
//                c.status();
//                break;
//            case "branch":
//                if (!c.saveInit()) {
//                    System.out.println("Directory not initialized");
//                    System.exit(0);
//                }
//                if (args.length > 3) {
//                    System.out.println("Please enter the right amount of arguments");
//                    System.exit(0);
//                }
//            case "rmbranch":
//                if (!c.saveInit()) {
//                    System.out.println("Directory not initialized");
//                    System.exit(0);
//                }
//                if (args.length > 3) {
//                    System.out.println("Please enter the right amount of arguments");
//                }
//                if (args.length < 3) {
//                    System.out.println("Please enter the branch name");
//                }
//
//            case "reset":
//                if (!c.saveInit()) {
//                    System.out.println("Directory not initialized");
//                    System.exit(0);
//                }
//                if (args.length > 2) {
//                    System.out.println("Please enter 'git reset' only");
//                    System.exit(0);
//                }
//            case "merge":
//                if (!c.saveInit()) {
//                    System.out.println("Directory not initialized");
//                    System.exit(0);
//                }
            default:
                System.out.println("Invalid command");
        }
        System.exit(0);
    }
}
