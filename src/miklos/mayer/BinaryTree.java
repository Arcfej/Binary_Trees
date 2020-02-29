package miklos.mayer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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
        return recursiveContains(key, root);
    }

    private boolean recursiveContains(Key key, Node<Key, E> node) {
        if (node == null) { // If the node null the subtree doesn't contains the data
            return false;
        }
        int direction = key.compareTo(node.getKey());
        if (direction == 0) { // If equal we find the data
            return true;
        } else if (direction < 0) { // If the key smaller than current go left
            return recursiveContains(key, node.getLeft());
        } else { // If bigger, go right
            return recursiveContains(key, node.getRight());
        }
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

    public E find(Key key) {
        return findRecursive(key, root);
    }

    private E findRecursive(Key key, Node<Key, E> node) {
        if (node == null) { // If the node null the subtree doesn't contains the data
            return null;
        }
        int direction = key.compareTo(node.getKey());
        if (direction == 0) { // If equal we found the data
            return node.getData();
        } else if (direction < 0) { // If the key smaller than current go left
            return findRecursive(key, node.getLeft());
        } else { // If bigger, go right
            return findRecursive(key, node.getRight());
        }
    }

    public boolean delete(Key key) {
        if (this.find(key) != null) {
            deleteRecursive(key, root);
            return true;
        } else {
            return false;
        }
    }

    private Node<Key, E> deleteRecursive(Key key, Node<Key, E> node) {
        if (node == null) { // If the node null the subtree doesn't contains the data
            return null;
        }
        int direction = key.compareTo(node.getKey());
        if (direction < 0) { // If the key smaller than current go left
            node.setLeft(deleteRecursive(key, node.getLeft()));
        } else if (direction > 0) { // If bigger, go right
            node.setRight(deleteRecursive(key, node.getRight()));
        } else { // If equal we found the data
            if (!node.hasRight()) return node.getLeft(); // If right is null, replace node with left child
            else if (!node.hasLeft()) return node.getRight(); // If only left is null, replace node with right child
            // Node has too child:
            node = deleteNodeWithTwoChildren(node);
        }
        return node; // Return
    }

    private Node<Key, E> deleteNodeWithTwoChildren(Node<Key, E> node) {
        int leftDepth = node.getLeft().getDepth();
        int rightDepth = node.getRight().getDepth();
        Node<Key, E> replacement;
        if (leftDepth > rightDepth) { // Replace node with the the rightmost child from the left subtree
            replacement = node.getLeft();
            while (replacement.getRight() != null) {
                replacement = replacement.getRight();
            }
            node.getLeft().setRight(replacement.getLeft()); // Save the children of replacement
            replacement.setLeft(node.getLeft());    // Copy the node's children to replacement
            replacement.setRight(node.getRight());  // Copy the node's children to replacement
        } else { // Replace node with the leftmost child from the right subtree
            replacement = node.getRight();
            while (replacement.getLeft() != null) {
                replacement = replacement.getLeft();
            }
            node.getRight().setLeft(replacement.getLeft()); // Save the children of replacement
            replacement.setLeft(node.getLeft());    // Copy the node's children to replacement
            replacement.setRight(node.getRight());  // Copy the node's children to replacement
        }
        node = replacement; // Replace node with the replacement
        return node;
    }

    public List<E> traverseInOrder() {
        return recursiveInOrder(new ArrayList<>(size), root);
    }

    private List<E> recursiveInOrder(List<E> list, Node<Key, E> node) {
        if (node == null) {
            return list;
        }
        if (node.hasLeft()) {
            recursiveInOrder(list, node.getLeft());
        }
        list.add(node.getData());
        if (node.hasRight()) {
            recursiveInOrder(list, node.getRight());
        }
        return list;
    }

    public List<E> traversePreOrder() {
        return recursivePreOrder(new ArrayList<>(size), root);
    }

    private List<E> recursivePreOrder(List<E> list, Node<Key, E> node) {
        if (node == null) {
            return list;
        }
        list.add(node.getData());
        if (node.hasLeft()) {
            recursivePreOrder(list, node.getLeft());
        }
        if (node.hasRight()) {
            recursivePreOrder(list, node.getRight());
        }
        return list;
    }

    public List<E> traversePostOrder() {
        return recursivePostOrder(new ArrayList<>(size), root);
    }

    private List<E> recursivePostOrder(List<E> list, Node<Key, E> node) {
        if (node == null) {
            return list;
        }
        if (node.hasLeft()) {
            recursivePostOrder(list, node.getLeft());
        }
        if (node.hasRight()) {
            recursivePostOrder(list, node.getRight());
        }
        list.add(node.getData());
        return list;
    }

    public List<E> traverseOnLevel() {
        List<E> list = new ArrayList<>(size);
        Queue<Node<Key, E>> queue = new ArrayDeque<>((int) Math.ceil((size + 1) / 2f));
        if (root != null) queue.add(root);
        while (!queue.isEmpty()) {
            Node<Key, E> node = queue.poll();
            list.add(node.getData());
            if (node.hasLeft()) queue.add(node.getLeft());
            if (node.hasRight()) queue.add(node.getRight());
        }
        return list;
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
