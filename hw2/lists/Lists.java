package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
*/
class Lists {


    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */




    /** Same as above, but a recursive version.
     *
     *  If you choose to go with the recursive skeleton, make sure to change the
     *  name from naturalRunsRecursive to naturalRuns, and delete the iterative
     *  skeleton. Otherwise, our autograder will grade the iterative version above.
     *
     * @return
     */
    static int naturalRuns(int [] L) {
        IntListList result = new IntListList(L, null);
        IntListList temp= result;

        temp.head = temp;
        result.tail = temp;


        while (L.tail != null){
            if (L.head < L.tail.head){
                temp = L.head;
            }
            else{

            }
        }
        return result;
    }

    /** Recursive helper method, if you'd like.
     *
     *  Assuming L is not null, returns the last element of L in which
     *  the value of L.head increases from the previous element (the
     *  end of the list if L is entirely in strictly ascending order).
     * @return
     */
    static boolean endOfRun(IntList L) {
        while (L.tail != null && L.tail.head > L.head) {
            L = L.tail;
        }
        return false;
    }
}
