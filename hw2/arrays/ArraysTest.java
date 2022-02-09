package arrays;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

/** Elijah
 *  @author Elijah Jacob
 */

public class ArraysTest {

    @Test
    public void ArrayTest() {
        int[] A = new int[]{2, 3, 4, 5};
        int[] B = new int[]{6, 7, 8, 9};
        int[] C = new int[]{2, 3, 4, 5, 6, 7, 8, 9};
        int [] D= new int [] {2,5,6,7,8,9};
        assertArrayEquals(Arrays.catenate(A,B) , new int[]{2, 3, 4, 5, 6, 7, 8, 9});
        assertArrayEquals(Arrays.remove(C, 1,2),D);
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
