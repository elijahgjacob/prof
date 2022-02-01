import static org.junit.Assert.*;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

public class MultiArrTest {

    int [][] A = {{4,7,3,8,9,},
            {2,23,53,1,5}};

    @Test
    public void testMaxValue() {
        assertEquals(53, MultiArr.maxValue(A));
    }

    @Test
    public void testAllRowSums() {
        assertEquals(new int[]{31, 84}, MultiArr.allRowSums(A));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
