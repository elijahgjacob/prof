package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        int[] arr = new int[A.length + B.length];
        int x=0;
        if (A.length==0){
            return B;
        }
        if (B.length==0){
            return A;
        }
        for (; x < A.length; x++) {
            arr[x] = A[x];
        }
        for (x=0 ; x < B.length; x++) {
            arr[A.length + x] = B[x];
        }

        return arr;
    }

    /* C2. */

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START. If the start + len is out of bounds for our array, you
     * can return null.
     * Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     * result should be [0, 3].
     */
    static int[] remove(int[] A, int start, int len) {
        int[] removearr = new int[A.length - len];
        int x;
        for (x = 0; x < start; x++) {
            removearr[x] = A[x];
        }
        System.arraycopy(A, start + len, removearr, start,A.length - len -start);
        return removearr;
                }

}
