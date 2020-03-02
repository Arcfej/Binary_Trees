package miklos.mayer;

/**
 * Thrown when a new data attempted to add to the binary tree with an already existing key.
 */
public class DuplicateItemException extends Exception {

    public DuplicateItemException(String message) {
        super(message);
    }
}
