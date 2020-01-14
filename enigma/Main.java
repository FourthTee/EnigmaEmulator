package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Fourth Teerakapibal
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        int count = 0;
        while (_input.hasNextLine()) {
            String command = _input.nextLine();
            String[] comlst = command.split("\\s+");
            if (count == 0 && !command.substring(0, 1).equals("*")) {
                throw error("Input must start with a setting");
            }
            count += 1;
            if (command.equals("")) {
                _output.println();
                continue;
            } else if (command.substring(0, 1).equals("*")) {
                setUp(machine, command);
            } else {
                checkmsgalph(command);
                String out = machine.convert(command);
                printMessageLine(out);
            }
        }
    }
    /** Check if the message contains characters outside of alphabet.
     * @param msg every message line.*/
    private void checkmsgalph(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            if (!_alphabet.contains(msg.charAt(i)) && msg.charAt(i) != ' ') {
                throw error("Message not in alphabet");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine();
            checkalphabet(alphabet);
            _alphabet = new Alphabet(alphabet);
            int numrotors = _config.nextInt();
            int numpawls = _config.nextInt();
            assert numrotors > numpawls && numpawls >= 0;
            allrotors = new ArrayList<Rotor>();
            if (!_config.nextLine().equals("")) {
                throw error("Too many arguments in second line");
            }
            while (_config.hasNextLine()) {
                Rotor rotor = readRotor();
                if (rotor == null) {
                    break;
                }
                allrotors.add(rotor);

            }
            return new Machine(_alphabet, numrotors, numpawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        } catch (AssertionError ecp) {
            throw error("Need Number of rotors > Number of pawls >= 0");
        }
    }

    /** Checks alphabets passed into the config file.
     *  @param alphabets alphabets passed into the machine.*/
    private void checkalphabet(String alphabets) {
        String badalph1 = "*";
        String badalph2 = "(";
        String badalph3 = ")";
        if (alphabets.contains(badalph1) || alphabets.contains(badalph2)
                || alphabets.contains(badalph3)) {
            throw error("Bad character (not in alphabet)");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = "";
            String type = "";
            String perm = "";
            String command = _config.nextLine();
            if (command.equals("")) {
                return null;
            }
            int count = 0;
            while (command.charAt(count) == ' ') {
                count++;
            }
            command = command.substring(count);
            String[] commarr = command.split("\\s+");

            name = commarr[0];
            if (name.contains("(") || name.contains(")")) {
                System.out.println("Name err: " + name);
                throw error("Name cannot contain parenthesis");
            }
            type = commarr[1];
            perm = commarr[2];
            for (int i = 3; i < commarr.length; i++) {
                perm = perm + " " + commarr[i];
            }
            if (_config.hasNext("[(].+[)]")) {
                String extcommand = _config.nextLine();
                String[] extcommarr = extcommand.split(" ");
                for (int i = 0; i < extcommarr.length; i++) {
                    perm = perm + " " + extcommarr[i];
                }
            }
            Permutation nperm = new Permutation(perm, _alphabet);
            if (type.charAt(0) == 'M') {
                if (type.length() == 1) {
                    throw error("Moving rotor needs notch");
                }
                String notches = type.substring(1);
                return new MovingRotor(name, nperm, notches);
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name, nperm);
            } else {
                return new Reflector(name, nperm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment.*/
    private void setUp(Machine M, String settings) {
        String[] setarr = settings.split("\\s+");
        if (setarr.length < M.numRotors() + 2) {
            throw error("Wrong number of rotors");
        }
        String[] rotorarr = new String[M.numRotors()];
        String initsettings = setarr[M.numRotors() + 1];
        checksetting(M, initsettings);
        System.arraycopy(setarr, 1, rotorarr, 0, M.numRotors());
        Permutation plugboard;
        String ringsetting;
        if (setarr.length > M.numRotors() + 2
                && !setarr[M.numRotors() + 2].contains("(")) {
            ringsetting = setarr[M.numRotors() + 2];
            if (setarr.length > M.numRotors() + 3) {
                String plugger = "";
                for (int i = M.numRotors() + 3; i < setarr.length; i++) {
                    plugger = plugger + setarr[i];
                }
                plugboard = new Permutation(plugger, _alphabet);
            } else {
                plugboard = new Permutation("", _alphabet);
            }
        } else {
            ringsetting = "";
            for (int i = 0; i < M.numRotors() - 1; i++) {
                ringsetting += "A";
            }
            if (setarr.length > M.numRotors() + 2) {
                String plugger = "";
                for (int i = M.numRotors() + 2; i < setarr.length; i++) {
                    plugger = plugger + setarr[i];
                }
                plugboard = new Permutation(plugger, _alphabet);
            } else {
                plugboard = new Permutation("", _alphabet);
            }
        }
        checksamerotor(rotorarr);
        M.insertRotors(rotorarr);
        M.checkusedrotor();
        M.setRotors(initsettings, ringsetting);
        M.setPlugboard(plugboard);
    }
    /** Check if the initial settings for the rotors is applicable.
     *  @param M machine used in setup.
     *  @param settings initliazation settings.*/
    private void checksetting(Machine M, String settings) {
        if (settings.length() != M.numRotors() - 1) {
            throw error("Wrong settings");
        }
        for (int idx = 0; idx < settings.length(); idx++) {
            if (!_alphabet.contains(settings.charAt(idx))) {
                throw error("Setting not in alphabet");
            }
        }
    }

    /** Checks to see if the same rotor is being used in the setup.
     *  @param rotors  list of rotor arrays.*/
    private void checksamerotor(String[] rotors) {
        for (int i = 1; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw error("Rotors cannot be the same");
                }
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String nmsg = msg.replaceAll("\\s+", "");
        String op = "";
        for (int idx = 0; idx < nmsg.length(); idx = idx + 1) {
            if ((idx + 1) % 5 == 0) {
                op = op + nmsg.charAt(idx) + " ";
            } else {
                op = op + nmsg.charAt(idx);
            }
        }
        _output.println(op);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Arraylist of all rotors.*/
    private ArrayList<Rotor> allrotors;

}
