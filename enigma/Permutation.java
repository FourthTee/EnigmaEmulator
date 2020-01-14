package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Fourth Teerakapibal
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        checkcycles(cycles);
        _cycles = cycles;

    }

    /** Check if the permutation cycle is valid.
     * @param cycle cycle passed in when constructing permutation.*/
    public void checkcycles(String cycle) {
        for (int idx = 0; idx < cycle.length(); idx++) {
            char cyclechar = cycle.charAt(idx);
            if (!_alphabet.contains(cyclechar) && cyclechar != '('
                    && cyclechar != ')' && cyclechar != ' ') {
                throw error("Incorrect permutation sequence");
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = _cycles + cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int pmodsize = wrap(p);
        char palph = alphabet().toChar(pmodsize);
        char newp = permute(palph);
        return alphabet().toInt(newp);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int cmodsize = wrap(c);
        char calph = alphabet().toChar(cmodsize);
        char newc = invert(calph);
        return alphabet().toInt(newc);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles.indexOf(p) == -1) {
            return p;
        }
        ArrayList<String> sublist = new ArrayList<String>();
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                int start = i;
                int end = i;
                while (_cycles.charAt(end) != ')') {
                    end = end + 1;
                }
                sublist.add(_cycles.substring(start + 1, end));
            }
        }
        char permutate = p;
        for (String substring : sublist) {
            int idx = substring.indexOf(p);
            if (substring.length() == 1 && idx == 0) {
                permutate = p;
                break;
            }
            if (idx != -1) {
                if (idx == substring.length() - 1) {
                    permutate = substring.charAt(0);
                } else {
                    permutate = substring.charAt(idx + 1);
                }
                break;
            }
        }
        return permutate;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_cycles.indexOf(c) == -1) {
            return c;
        }
        ArrayList<String> sublist = new ArrayList<String>();
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                int start = i;
                int end = i;
                while (_cycles.charAt(end) != ')') {
                    end = end + 1;
                }
                sublist.add(_cycles.substring(start + 1, end));
            }
        }
        char permutate = c;
        for (String substring : sublist) {
            int idx = substring.indexOf(c);
            if (substring.length() == 1 && idx == 0) {
                permutate = c;
                break;
            }
            if (idx != -1) {
                if (idx == 0) {
                    permutate = substring.charAt(substring.length() - 1);
                } else {
                    permutate = substring.charAt(idx - 1);
                }
                break;
            }
        }
        return permutate;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }
    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String of cycles. */
    private String _cycles;

}
