package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Elijah G. Jacob
 */
public class Main {

    private static Commands c;
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) {
        String first = args[0];
        if (args.length == 0) { //error handling for input
            System.out.println("Must have at least one argument");
            System.exit(0);
        }
        switch (first) {
            case "init":
                if (args.length > 2){
                    System.out.println("invalid operation");
                } else if (!c.init()) {
                    System.out.println("The repo has already been initialized");
                }
                break;
            case "add":
                if (args.length < 2){
                    System.out.println("invalid operation");
                }
            case "commit":
                if (args.length < 3){
                    System.out.println("Please enter the correct amount of information");
                }
            case "log":
                if (args.length > 2){
                    System.out.println("Please enter git --log only");
                }
            default:
                System.out.println("Invalid command");
        }

    }

}
