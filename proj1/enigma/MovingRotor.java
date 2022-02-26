package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author P.N.Hilfinger
 */
class MovingRotor extends Rotor {

    private String _notches;
    private int _setting;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    void advance() {
        _setting += 1;
        int move = permutation().wrap(_setting);
        super.set(move);
    }

    @Override
    boolean rotates() {
        return true;
    }


    @Override
    boolean atNotch() {
        String c = Character.toString(alphabet().toChar(_setting));
        if (_notches.contains(c)) {
            return true;
        }
        return false;
    }

}
