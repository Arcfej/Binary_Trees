package miklos.mayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BinaryTree<Key extends Comparable<Key>, E> {

    private Node<Key, E> root;

    private int size;

    public BinaryTree() {
        root = null;
        size = 0;
    }

    public E getRoot() {
        return root.getData();
    }

    public boolean contains(Key key) {
        return recursiveContains(root, key);
    }

    private boolean recursiveContains(Node<Key, E> node, Key key) {
        if (node == null) {
            return false; // Node null, node not equals data
        }
        if (node.getKey().compareTo(key) == 0) {
            return true; // Node equals data
        }
        if (recursiveContains(node.getRight(), key)) {
            return true; // Return true if right subtree contains data
        }
        if (recursiveContains(node.getLeft(), key)) {
            return true; // Return true if left subtree contains data
        }
        return false; // return false if neither subtree contains data
    }

    public void add(Key key, E data) throws DuplicateItemException {
        if (root == null) {
            root = new Node<>(key, data, null);
            size++;
            return;
        }
        Node<Key, E> current = root;
        Node<Key, E> parent = null;
        int direction = 0;
        // Find a place for the new data
        while (current != null) {
            direction = key.compareTo(current.getKey());
            parent = current;
            if (direction < 0) { // If the new data smaller than current go left
                current = current.getLeft();
            } else if (direction > 0) { // If bigger, go right
                current = current.getRight();
            } else { // If equal, throw exception
                throw new DuplicateItemException("The data is already in the tree.");
            }
        }
        // Add the new data to the empty space based on direction
        current = new Node<>(key, data, parent);
        if (direction < 0) {
            parent.setLeft(current);
        } else {
            parent.setRight(current);
        }
        size++;
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
        List<E> list = new ArrayList<>(size);
        return recursiveInOrder(list, root);
    }

    private List<E> recursiveInOrder(List<E> list, Node<Key, E> node) {
        if (node == null) {
            return list;
        }
        if (node.getLeft() != null) {
            recursiveInOrder(list, node.getLeft());
        }
        list.add(node.getData());
        if (node.getRight() != null) {
            recursiveInOrder(list, node.getRight());
        }
        return list;
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

    public static class Node<Key, T> {

        private Key key;
        private T data;
        private Node<Key, T> parent;
        private Node<Key, T> left;
        private Node<Key, T> right;

        public Node(Key key, T data, Node<Key, T> parent) {
            this.key = key;
            this.data = data;
            this.parent = parent;
            left = null;
            right = null;
        }

        public Key getKey() {
            return key;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<Key, T> getParent() {
            return parent;
        }

        public void setParent(Node<Key, T> parent) {
            this.parent = parent;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public Node<Key, T> getLeft() {
            return left;
        }

        public void setLeft(Node<Key, T> left) {
            this.left = left;
        }

        public boolean hasRight() {
            return right != null;
        }

        public Node<Key, T> getRight() {
            return right;
        }

        public void setRight(Node<Key, T> right) {
            this.right = right;
        }

        private int getDepth() {
            int rightDepth = right == null ? 0 : right.getDepth();
            int leftDepth = left == null ? 0 : left.getDepth();

            if (rightDepth > leftDepth) {
                return rightDepth + 1;
            } else {
                return leftDepth + 1;
            }
        }
    }
}
