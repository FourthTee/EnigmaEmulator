package enigma;


import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Fourth Teerakapibal
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _offset = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;

    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int frontconvert = permutation().wrap(p + this.setting() - _offset);
        int permutated = permutation().permute(frontconvert);
        int backconvert = permutation().wrap(permutated - this.setting()
                + _offset);
        return backconvert;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int frontconvert = permutation().wrap(e + this.setting() - _offset);
        int permutate = permutation().invert(frontconvert);
        int backconvert = permutation().wrap(permutate - setting() + _offset);
        return backconvert;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    /** Set the offset variable to a.
     * @param a character converted to integer form*/
    void setoffset(int a) {
        _offset = a;
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of the rotor (initialized to 0). */
    private int _setting;

    /** Offset of the rotor from ring setting (initialized to 0).*/
    private int _offset;
}
