package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Elijah G. Jacob
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) {
        String first = args[0];
        String filename = " ";
        String message = " ";
        Commands c = new Commands();
        if (args.length == 0) { //error handling for input
            System.out.println("Must have at least one argument");
            System.exit(0);
        }
        switch (first) {
            case "init":
                while (!c.init()) {
                    if (args.length > 1) {
                        System.out.println("invalid operation");
                    }
                }
                break;
            case "add":
                while (!c.add(filename)) {
                    if (args.length != 3) {
                        System.out.println("invalid operation");
                    }
                }
                break;
            case "commit":
                while (!c.commit(message)) {
                    if (args.length < 3) {
                        System.out.println("Please enter a commit message");
                    }
                }
            case "log":
                if (args.length > 2){
                    System.out.println("Please enter git --log only");
                }
            default:
                System.out.println("Invalid command");
        }
        System.exit(0);
    }
}
