package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Fourth Teerakapibal
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
        _setting = 0;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(_permutation.wrap(setting() + 1));
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        for (int notch = 0; notch < _notches.length(); notch++) {
            char cursetting = this.alphabet().toChar(this.setting());
            if (_notches.charAt(notch) == cursetting) {
                return true;
            }
        }
        return false;
    }

    /** Setting of rotor. */
    private int _setting;

    /** String of notches. */
    private String _notches;

    /** Permutation. */
    private Permutation _permutation;
}
