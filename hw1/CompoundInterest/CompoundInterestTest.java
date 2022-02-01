import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {
    static final double DELTA = 1e-15;

    @Test
    public void testNumYears() {
        assertEquals(101, CompoundInterest.numYears(2123), DELTA);
        assertEquals(1, CompoundInterest.numYears(2023), DELTA);
        assertEquals(0, CompoundInterest.numYears(2022), DELTA);
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).
        // The variable below can be used when you write your tests.
        double tolerance = 0.01;
        assertEquals(110.41, CompoundInterest.futureValue(100,2,2027), tolerance);
        assertEquals(90.39, CompoundInterest.futureValue(100,-2,2027), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(99.50, CompoundInterest.futureValueReal(100,5,2024, 5), tolerance);
        assertEquals(101.61, CompoundInterest.futureValueReal(100,5,2024, 4), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(23205, CompoundInterest.totalSavings(100, 2025, 10), tolerance);
        assertEquals(16550, CompoundInterest.totalSavings(100, 2024, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(21403, CompoundInterest.totalSavingsReal(100, 2025, 10, 2), tolerance);
        assertEquals(15104, CompoundInterest.totalSavingsReal(100, 2024, 10, 3), tolerance);
    }



    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
