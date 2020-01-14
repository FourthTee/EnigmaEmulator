package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Fourth Teerakpibal
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotorsused = new ArrayList<Rotor>();
    }
    /** Alphabet order for offset in ringsetting.**/
    private Alphabet alphabetoffset = new Alphabet(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        assert rotors.length == numRotors();
        if (_rotorsused.size() == _numRotors) {
            int counter = 0;
            for (String rotor: rotors) {
                boolean checkrotor = false;
                for (Rotor allrotor : _allRotors) {
                    if (allrotor.name().equals(rotor)) {
                        _rotorsused.set(counter, allrotor);
                        counter += 1;
                        checkrotor = true;
                    }
                }
                if (!checkrotor) {
                    throw error("Misnamed rotor");
                }
            }
        } else {
            for (String rotor: rotors) {
                for (Rotor allrotor : _allRotors) {
                    if (allrotor.name().equals(rotor)) {
                        _rotorsused.add(allrotor);
                    }
                }
            }
            if (_rotorsused.size() != numRotors()) {
                throw error("Misnamed rotor");
            }
        }

        if (!_rotorsused.get(0).reflecting()) {
            throw error("First rotor must be a reflector");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).
     *  @param offset offset of the rotors
     *  @param setting initial setting of rotors*/
    void setRotors(String setting, String offset) {
        for (int idx = 1; idx < numRotors(); idx++) {
            _rotorsused.get(idx).set(setting.charAt(idx - 1));
            char offsetchar = offset.charAt(idx - 1);
            int offsetint = alphabetoffset.toInt(offsetchar);
            _rotorsused.get(idx).setoffset(offsetint);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean [] checkmove = new boolean[numRotors()];
        Rotor farright = _rotorsused.get(numRotors() - 1);
        checkmove[numRotors() - 1] = true;
        if (farright.atNotch()) {
            for (int idx = numRotors() - 2; idx >= 0; idx--) {
                Rotor currotor = _rotorsused.get(idx);
                Rotor predrotor = _rotorsused.get(idx + 1);
                if (predrotor.atNotch()) {
                    if (currotor.rotates()) {
                        checkmove[idx] = true;
                        checkmove[idx + 1] = true;
                    } else if (idx == numRotors() - numPawls() - 1) {
                        continue;
                    } else {
                        checkmove[idx + 1] = true;
                    }
                }
            }
        } else {
            for (int idx = numRotors() - 3; idx >= 0; idx--) {
                Rotor currotor = _rotorsused.get(idx);
                Rotor predrotor = _rotorsused.get(idx + 1);
                if (predrotor.atNotch()) {
                    if (currotor.rotates()) {
                        checkmove[idx] = true;
                        checkmove[idx + 1] = true;
                    } else if (idx == numRotors() - numPawls() - 1) {
                        continue;
                    } else {
                        checkmove[idx + 1] = true;
                    }
                }
            }
        }
        for (int idx = 0; idx < numRotors(); idx++) {
            if (checkmove[idx]) {
                _rotorsused.get(idx).advance();
            }
        }
        int plugin = _plugboard.permute(c);
        int curstatus = plugin;
        for (int idx = numRotors() - 1; idx >= 0; idx--) {
            Rotor currotor = _rotorsused.get(idx);
            curstatus = currotor.convertForward(curstatus);
        }
        for (int idx = 1; idx < numRotors(); idx++) {
            Rotor currotor = _rotorsused.get(idx);
            curstatus = currotor.convertBackward(curstatus);
        }
        int plugout = _plugboard.permute(curstatus);
        return plugout;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String nmsg = "";
        for (int i = 0; i <  msg.length(); i++) {
            char cmsg = msg.charAt(i);
            if (cmsg == ' ') {
                nmsg = nmsg + " ";
                continue;
            }
            int cmsgnum = _alphabet.toInt(cmsg);
            int newcmsgnum = convert(cmsgnum);
            char newcmsg = _alphabet.toChar(newcmsgnum);
            nmsg = nmsg + newcmsg;
        }
        return nmsg;
    }
    /** Check if the moving and nonmoving rotors are in the right position.*/
    void checkusedrotor() {
        for (int i = 0; i < _rotorsused.size(); i++) {
            if (i >= 1 && i < numRotors() - numPawls()) {
                if (_rotorsused.get(i).rotates()) {
                    throw error("Moving rotor in wrong position");
                }
            }
            if (i >= numRotors() - numPawls()) {
                if (!_rotorsused.get(i).rotates()) {
                    throw error("Nonmoving rotor in wrong position");
                }
            }
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** number of pawls.*/
    private final int _pawls;

    /**number of rotors.*/
    private final int _numRotors;

    /** arraylist of all rotors.*/
    private final Collection<Rotor> _allRotors;

    /**arraylist of the used rotors.*/
    private ArrayList<Rotor> _rotorsused;

    /** plugboard permutatations.*/
    private Permutation _plugboard;

}
