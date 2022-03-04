package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a nor in the enigma.
 *  @author
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _perm = perm;
        if (perm.derangement()) == true){
            throw error ("xyz");
        }
    }


    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("Reflector has only one position");
        }
    }

    @Override
    boolean reflecting(){

    }

}
