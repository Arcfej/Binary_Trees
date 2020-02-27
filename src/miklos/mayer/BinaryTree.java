package miklos.mayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BinaryTree<E extends Comparable<E>> {

    private Node<E> root;

    private int size;

    public BinaryTree() {
        root = null;
        size = 0;
    }

    public E getRoot() {
        return root.getData();
    }

    public boolean contains(E data) {
        return false;
    }

    public void add(E data) throws DuplicateItemException {
        if (root == null) {
            root = new Node<>(data, null);
            return;
        }
        Node<E> current = root;
        Node<E> parent = null;
        int direction = 0;
        // Find a place for the new data
        while (current != null) {
            direction = data.compareTo(current.getData());
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
        current = new Node<>(data, parent);
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

    private List<E> recursiveInOrder(List<E> list, Node<E> node) {
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

    public static class Node<T extends Comparable<T>> {

        private T data;
        private Node<T> parent;
        private Node<T> left;
        private Node<T> right;

        public Node(T data, Node<T> parent) {
            this.data = data;
            this.parent = parent;
            left = null;
            right = null;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public Node<T> getLeft() {
            return left;
        }

        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public boolean hasRight() {
            return right != null;
        }

        public Node<T> getRight() {
            return right;
        }

        public void setRight(Node<T> right) {
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
