package miklos.mayer;

import java.util.List;
import java.util.function.Predicate;

public class BinaryTree<E extends Comparable> {

    private TreeNode<E> root;

    public BinaryTree() {
        root = null;
    }

    public boolean contains(E data) {
        return false;
    }

    public void add(E data) {

    }

    public E find(Predicate<E> predicate) {
        return null;
    }

    public boolean delete(E data) {
        return false;
    }

    public boolean delete(Predicate<E> predicate) {
        return false;
    }

    public List<E> traverseInOrder() {
        return null;
    }

    public List<E> traversePreOrder() {
        return null;
    }

    public List<E> traversePostOrder() {
        return null;
    }

    public List<E> traverseOnLevel(int level) {
        return null;
    }
}
