package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static enigma.TestUtils.UPPER_STRING;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author  Elijah Jacob
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     *
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     *
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     *
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /**
     * Check that PERM has an ALPHABET whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testchpermutation() {
        Permutation perm = getNewPermutation("(DEFGA)", getNewAlphabet("ADEFG"));
        assertEquals('E', perm.permute('D'));
        assertEquals('F', perm.permute('E'));
        assertEquals('G', perm.permute('F'));
        assertEquals('A', perm.permute('G'));
        assertEquals('D', perm.permute('A'));

    }

    @Test
    public void checksimplepermutation() {
        String input = "(ABCDE)";
        Alphabet alpha = getNewAlphabet("ABCDE");
        Permutation perm = getNewPermutation(input,alpha);
        String fromAlpha = "ABCDE";
        String toAlpha = "BCDEA";
        checkPerm("simple", fromAlpha, toAlpha, perm, alpha);
    }

    @Test
    public void checkcomplexpermutation() {
        Permutation perm = getNewPermutation("(DEFG)(ABC)", getNewAlphabet("ABCDEFG"));
        assertEquals('E', perm.permute('D'));
        assertEquals('F', perm.permute('E'));
        assertEquals('G', perm.permute('F'));
        assertEquals('D', perm.permute('G'));
        assertEquals('B', perm.permute('A'));
        assertEquals('C', perm.permute('B'));
        assertEquals('A', perm.permute('C'));

    }


    @Test
    public void testPermuteChar() {
        Permutation e2 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', e2.permute('D'));
        assertEquals('A', e2.permute('B'));
        assertEquals('C', e2.permute('A'));
        Permutation e0 = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', e0.permute('E'));
    }

    @Test
    public void testInvertChar() {
        Permutation pt = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', pt.invert('A'));
        assertEquals('D', pt.invert('B'));
        assertEquals('C', pt.invert('D'));
        Permutation p10 = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', p10.invert('E'));
    }

    @Test
    public void testInvertInt() {
        Permutation pt = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(1, pt.invert(0));
        assertEquals(2, pt.invert(3));
        assertEquals(1, pt.invert(40));
        assertEquals(3, pt.invert(1));
        Permutation pt0 = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals(4, pt0.invert(4));
    }
}


