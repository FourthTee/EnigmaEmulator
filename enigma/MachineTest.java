package enigma;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/** Test the machine class by initializing and permutating using the machine.
 * @author Fourth Teerakapibal
 */

public class MachineTest {
    public ArrayList<Rotor> initRotor() {
        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        MovingRotor i = new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                        + "(IV) (JZ) (S)",
                        new Alphabet()), "Q");
        MovingRotor ii = new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                        + "(GR) (NT) (A) (Q)",
                        new Alphabet()), "E");
        MovingRotor iii = new MovingRotor("III",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
                        new Alphabet()), "V");
        MovingRotor iv = new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                        new Alphabet()), "J");
        MovingRotor v = new MovingRotor("V",
                new Permutation("(AVOLDRWFIUQ) (BZKSMNHYC) (EGTJPX)",
                        new Alphabet()), "Z");
        MovingRotor vi = new MovingRotor("VI",
                new Permutation("(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)",
                        new Alphabet()), "ZM");
        MovingRotor vii = new MovingRotor("VII",
                new Permutation("(ANOUPFRIMBZTLWKSVEGCJYDHXQ)",
                        new Alphabet()), "ZM");
        MovingRotor viii = new MovingRotor("VIII",
                new Permutation("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)",
                        new Alphabet()), "ZM");
        FixedRotor beta = new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                        new Alphabet()));
        FixedRotor gamma = new FixedRotor("Gamma",
                new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)",
                        new Alphabet()));
        Reflector b = new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) "
                        + "(LO) (MP) (RX) (SZ) (TV)",
                        new Alphabet()));
        Reflector c = new Reflector("C",
                new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) "
                        + "(LM) (PW) (QZ) (SX) (UY)",
                        new Alphabet()));
        rotors.add(i); rotors.add(ii); rotors.add(iii);
        rotors.add(iv); rotors.add(v);
        rotors.add(vi); rotors.add(vii); rotors.add(viii);
        rotors.add(beta); rotors.add(gamma);
        rotors.add(b); rotors.add(c);
        return rotors;
    }

    public ArrayList<Rotor> initRotor2() {
        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        MovingRotor A = new MovingRotor("I",
                new Permutation("(ABC)",
                        new Alphabet()), "C");
        MovingRotor B = new MovingRotor("II",
                new Permutation("(ABC)",
                        new Alphabet()), "C");
        MovingRotor C = new MovingRotor("III",
                new Permutation("(ABC)",
                        new Alphabet()), "C");
        FixedRotor D = new FixedRotor("D",
                new Permutation("(ABC)", new Alphabet()));

        rotors.add(A); rotors.add(B); rotors.add(C); rotors.add(D);
        return rotors;
    }


    @Test
    public void machineconvert() {
        ArrayList<Rotor> allrotors = initRotor();
        Machine m = new Machine(new Alphabet(), 5, 3, allrotors);
        String[] rotors = new String[]{"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotors);
        m.setRotors("AXLE", "AAAA");
        m.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                new Alphabet()));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                m.convert("FROMHISSHOULDERHIAWATHA"));
        assertEquals("BHCNSCXNUOAATZXSRCFYDGU",
                m.convert("TOOKTHECAMERAOFROSEWOOD"));
        assertEquals("FLPNXGXIXTYJUJRCAUGEUNCFMKUF",
                m.convert("MADEOFSLIDINGFOLDINGROSEWOOD"));
        assertEquals("WJFGKCIIRGXODJGVCGPQOH",
                m.convert("NEATLYPUTITALLTOGETHER"));
        assertEquals("ALWEBUHTZMOXIIVXUEFPRPR",
                m.convert("INITSCASEITLAYCOMPACTLY"));
        assertEquals("KCGVPFPYKIKITLBURVGTSFU",
                m.convert("FOLDEDINTONEARLYNOTHING"));
    }

    @Test
    public void machineconvert2() {
        ArrayList<Rotor> allrotors = initRotor();
        Machine m = new Machine(new Alphabet(), 5, 3, allrotors);
        String[] rotors = new String[]{"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotors);
        m.setRotors("AXLE", "AAAA");
        m.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                new Alphabet()));
        assertEquals("FROMHISSHOULDERHIAWATHA",
                m.convert("QVPQSOKOILPUBKJZPISFXDW"));
        assertEquals("TOOKTHECAMERAOFROSEWOOD",
                m.convert("BHCNSCXNUOAATZXSRCFYDGU"));
        assertEquals("MADEOFSLIDINGFOLDINGROSEWOOD",
                m.convert("FLPNXGXIXTYJUJRCAUGEUNCFMKUF"));
        assertEquals("NEATLYPUTITALLTOGETHER",
                m.convert("WJFGKCIIRGXODJGVCGPQOH"));
        assertEquals("INITSCASEITLAYCOMPACTLY",
                m.convert("ALWEBUHTZMOXIIVXUEFPRPR"));
        assertEquals("FOLDEDINTONEARLYNOTHING",
                m.convert("KCGVPFPYKIKITLBURVGTSFU"));
    }

    @Test
    public void machineconvert3() {
        ArrayList<Rotor> allrotors = initRotor();
        Machine m = new Machine(new Alphabet(), 5, 3, allrotors);
        String[] rotors = new String[]{"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotors);
        m.setRotors("AXLE", "AAAA");
        m.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                new Alphabet()));
        assertEquals("WAAOXUZNLZJPOAYFPTERYXZT",
                m.convert("COMPUTERSCIENCESIXTYONEB"));
        assertEquals("CUTTUZFPKAWAXUWMJX",
                m.convert("EECSONETWENTYSEVEN"));
        assertEquals("JBLEICYNHVCHBI",
                m.convert("EESIXTEENAANDB"));
        assertEquals("GAHAHJROQOLSBAVGDVUOBSPQ",
                m.convert("COMPUTERSCIENCESIXTYONEA"));
        assertEquals("PYPUJIJ",
                m.convert("ILOVECS"));

    }

}
