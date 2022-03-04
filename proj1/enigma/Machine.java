package enigma;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Elijah G. Jacob
 */
class Machine<msg> {
    /**
     * Alphabet of my rotors.
     */
    private final Alphabet _alphabet;
    /**
     * The number of rotors
     */
    private int _numRotors;
    /**
     * The number of pawls
     */
    private int _pawls;
    /**
     * The rotors in iterable form
     */
    private Rotor [] _allRotors;
    /**
     * The plugboard permutations
     */
    private Permutation _plugboard;
    private String convertedmsg;
    private String msg;
    private Rotor [] _usedRotors;
    static HashMap<String, Rotor> rotorMap = new HashMap<>();


    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray(new Rotor[0]);
        _usedRotors= new Rotor[numRotors];
        for (int x=0; x < numRotors; x++ ){
            rotorMap.put(_allRotors[x].name(), _allRotors[x]);
        }
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }


    Alphabet alphabet() {
        return _alphabet;
    }

    boolean Rotorflag() {
        int reflector=0;
        int rotating=0;
        int still=0;
        boolean flag = true;

        if (_usedRotors.length != _numRotors) {
            throw new EnigmaException("Wrong number of rotors");
        }

        for (int x = 0; x < _usedRotors.length; x++) {
            for (int y = 0; y < _usedRotors.length; y++) {
                if (_usedRotors[x] == _usedRotors[y] && x != y) {
                    throw error("Rotors are duplicate");
                }
            }
        }
        for (int x = 0; x < _usedRotors.length; x++) {
            if (_usedRotors[x].reflecting()) {
                reflector++;
            }
            if (_usedRotors[x].rotates()) {
                rotating++;
            }
        }
//        still = numRotors() - reflector - rotating;
        if (reflector != 1) {
            throw error("Wrong number of reflectors");
        }
        if (rotating != numPawls()) {
            throw error("Wrong number of moving rotors");
        }
//        if (still != (numRotors() - numPawls())) {
//            throw error("Wrong number of fixed rotors");
//        }
        return flag;
}

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        for (int x=0; x< numRotors(); x++) {
            _usedRotors[x] = rotorMap.get(rotors[x]);
        }
        Rotorflag();
        for (int x = 0; x < _usedRotors.length; x++) {
            if (_usedRotors[x] == null) {
                throw new EnigmaException("Error with input. Check the input file");
            }
            for (x = 0; x < rotors.length; x++) {
                for (Object current : _allRotors) {
                    if (rotors[x].equals(((Rotor) current).name().toUpperCase())) {
                        _usedRotors[x] = (Rotor) current;
                    }
                }
            }
        }
    }
    




        /** Set my rotors according to SETTING, which must be a string of
         *  numRotors()-1 characters in my alphabet. The first letter refers
         *  to the leftmost rotor setting (not counting the reflector).  */
        void setRotors (String setting){
            for (int x = 0; x < numRotors()-1; x++) {
                    _usedRotors[x + 1].set(setting.charAt(x));
                }
        }


        /** Return the current plugboard's permutation. */
        Permutation plugboard () {
            return _plugboard;
        }

        /** Set the plugboard to PLUGBOARD. */
        void setPlugboard (Permutation plugboard){
            _plugboard = plugboard;
        }

        /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
         *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
         *  undefined results. */
        Rotor getRotor(int k) {
            return _usedRotors[k];
        }

        /** Returns the result of converting the input character C (as an
         *  index in the range 0..alphabet size - 1), after first advancing
         *  the machine. */
        int convert ( int c){
            advanceRotors();
            if (Main.verbose()) {
                System.err.printf("[");
                for (int r = 1; r < numRotors(); r += 1) {
                    System.err.printf("%c",
                            alphabet().toChar(getRotor(r).setting()));
                }
                System.err.printf("] %c -> ", alphabet().toChar(c));
            }
            c = plugboard().permute(c);
            if (Main.verbose()) {
                System.err.printf("%c -> ", alphabet().toChar(c));
            }
            c = applyRotors(c);
            c = plugboard().permute(c);
            if (Main.verbose()) {
                System.err.printf("%c%n", alphabet().toChar(c));
            }
            return c;
        }


        /** Return the result of applying the rotors to the character C (as an
         *  index in the range 0..alphabet size - 1). */
        private int applyRotors (int c) {
            int x;
            for (x = numRotors()-1; x >= 0; x--) {
                c = _usedRotors[x].convertForward(c);
            }
            for (x = 1; x <= numRotors()-1; x++) {
                c =_usedRotors[x].convertBackward(c);
            }
            return c;
        }

        /** Returns the encoding/decoding of MSG, updating the state of
         *  the rotors accordingly. */
        String convert (String msg){
            for (int x = 0; x < msg.length(); x++) {
                convertedmsg +=_alphabet.toChar(convert(_alphabet.toInt(msg.charAt(x))));
            }
            return convertedmsg;
        }



    private void advanceRotors() {
        boolean dstep = false;
        int fast =_usedRotors[_usedRotors.length-1].setting();
        _usedRotors[_usedRotors.length-1].set(fast + 1);
        for (int x = numRotors()-1; x >= 0; x--) {
            if (_usedRotors[x].atNotch() && _usedRotors[x - 1].rotates()) {
                _usedRotors[x].advance();
                _usedRotors[x - 1].advance();
            }
        }
    }
}


