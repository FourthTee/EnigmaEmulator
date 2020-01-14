package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Fourth Teerakapibal
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPerm1() {
        perm = new Permutation("(ABC)", new Alphabet());
        assertEquals('B', perm.permute('A'));
        assertEquals('C', perm.permute('B'));
        assertEquals('A', perm.permute('C'));
        assertEquals('D', perm.permute('D'));
        assertEquals(1, perm.permute(0));
        assertEquals(2, perm.permute(1));
        assertEquals(0, perm.permute(2));
        assertEquals(3, perm.permute(3));
    }

    @Test
    public void checkperm2() {
        perm = new Permutation("(AGC) (BKF) (ELZ)", UPPER);
        assertEquals('A', perm.permute('C'));
        assertEquals('B', perm.permute('F'));
        assertEquals('E', perm.permute('Z'));
        assertEquals('Z', perm.permute('L'));
        assertEquals(4, perm.permute(25));
        assertEquals(6, perm.permute(26));
        assertEquals(0, perm.permute(2));
    }

    @Test
    public void checkperm3() {
        perm = new Permutation("(AGC)    (BKF)(ELZ) (JX)", new Alphabet());
        assertEquals('A', perm.permute('C'));
        assertEquals('B', perm.permute('F'));
        assertEquals('E', perm.permute('Z'));
        assertEquals('Z', perm.permute('L'));
        assertEquals('J', perm.permute('X'));
        assertEquals(4, perm.permute(25));
        assertEquals(6, perm.permute(26));
        assertEquals(0, perm.permute(2));
    }

    @Test
    public void checkinv1() {
        perm = new Permutation("(ABC)", new Alphabet());
        assertEquals('C', perm.invert('A'));
        assertEquals('A', perm.invert('B'));
        assertEquals('B', perm.invert('C'));
        assertEquals('D', perm.invert('D'));
        assertEquals(2, perm.invert(0));
        assertEquals(0, perm.invert(1));
        assertEquals(1, perm.invert(2));
        assertEquals(3, perm.invert(3));
    }
    @Test
    public void checkinv2() {
        perm = new Permutation("(AGC) (BKF) (ELZ)", UPPER);
        assertEquals('G', perm.invert('C'));
        assertEquals('K', perm.invert('F'));
        assertEquals('L', perm.invert('Z'));
        assertEquals('E', perm.invert('L'));
        assertEquals(11, perm.invert(25));
        assertEquals(2, perm.invert(26));
        assertEquals(6, perm.invert(2));
    }
    @Test
    public void checkinv3() {
        perm = new Permutation("(AGC)    (BKF)(ELZ) (JX)", new Alphabet());
        assertEquals('G', perm.invert('C'));
        assertEquals('K', perm.invert('F'));
        assertEquals('L', perm.invert('Z'));
        assertEquals('E', perm.invert('L'));
        assertEquals('J', perm.invert('X'));
        assertEquals(11, perm.invert(25));
        assertEquals(2, perm.invert(26));
        assertEquals(6, perm.invert(2));
    }

    @Test
    public void checkDerangement() {
        Permutation permnd1 = new Permutation("(AGC)    (BKF)(ELZ) (JX)",
                new Alphabet());
        Permutation permnd2 = new Permutation("(AGC) (BKF) (ELZ)",
                new Alphabet());
        Permutation permnd3 = new Permutation("(ABC)", new Alphabet());
        Permutation permd1 = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)",
                new Alphabet());
        Permutation permd2 = new Permutation("(ABCD) (EFGHIJ) (KLMNOPQRS) "
                + "(TUVWX) (YZ)", new Alphabet());
        Permutation permd3 = new Permutation("(AGJW) (FBHIC) (KMTZNEOL) "
                + "(PS) (QURDX) (VY)", new Alphabet());
        assertFalse(permnd1.derangement());
        assertFalse(permnd2.derangement());
        assertFalse(permnd3.derangement());
        assertTrue(permd1.derangement());
        assertTrue(permd2.derangement());
        assertTrue(permd3.derangement());
    }


}
