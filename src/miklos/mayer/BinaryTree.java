package miklos.mayer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * An implementation of a binary search tree data structure.
 *
 * @param <Key> the type of the key which is used to store and organize the data in the tree
 * @param <E> the type of the data which will be stored in the tree
 */
public class BinaryTree<Key extends Comparable<Key>, E> {

    /**
     * The root node in the tree.
     */
    private Node<Key, E> root;

    /**
     * the size of the tree.
     */
    private int size;

    /**
     * Base constructor.
     */
    public BinaryTree() {
        root = null;
        size = 0;
    }

    /**
     * Getter of the root of the tree.
     *
     * @return the root of the tree.
     */
    public E getRoot() {
        return root.getData();
    }

    /**
     * Returns if any data is stored with the provided key or not.
     *
     * @param key The key to look for
     * @return true if any data is stored with the provided key.
     */
    public boolean contains(Key key) {
        return recursiveContains(key, root);
    }

    /**
     * Recursive method to find out if any data is stored with a key in the tree.
     *
     * @param key the key to look for
     * @param node the root of the subtree to search in
     * @return true if the provided node is stored with the provided key
     */
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

    /**
     * Add a new node to the tree.
     *
     * @param key the key to store the data with
     * @param data the data to store in the tree
     * @throws DuplicateItemException if there is already any stored data with the given key
     */
    public void add(Key key, E data) throws DuplicateItemException {
        if (root == null) { // If the tree empty add the new node as the root of the tree
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
            if (direction < 0) { // If the key is smaller than current's key, go left
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

    /**
     * Searches and returns the data stored with the provided key.
     *
     * @param key The key to search the data with.
     * @return the data stored with the provided key or null if nothing is found.
     */
    public E find(Key key) {
        return findRecursive(key, root);
    }

    /**
     * Recursive method to find the data stored with the provided key.
     *
     * @param key the key to search the data with.
     * @param node the root of the subtree to search the data in.
     * @return the found data or null if nothing was stored with the given key.
     */
    private E findRecursive(Key key, Node<Key, E> node) {
        if (node == null) { // If the node null the subtree doesn't contains the data
            return null;
        }
        int direction = key.compareTo(node.getKey());
        if (direction == 0) { // If equal we found the data
            return node.getData();
        } else if (direction < 0) { // If the key smaller than current's key, go left
            return findRecursive(key, node.getLeft());
        } else { // If bigger, go right
            return findRecursive(key, node.getRight());
        }
    }

    /**
     * Delete the node stored with the given key.
     *
     * @param key The key of the node to delete
     * @return true if the deletion was successful or false if nothing has been found with the key.
     */
    public boolean delete(Key key) {
        if (this.find(key) != null) {
            root = deleteRecursive(key, root);
            size--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recursive method to delete any data stored with the provided key.
     *
     * @param key The key the data stored with.
     * @param node The root of the subtree to search for the data to delete.
     * @return  the provided node again, or
     *          (if the node store the data to delete) the node to replace the provided node with.
     *          Could be null.
     */
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
        return node; // Return the same node or the replacement if the node has been deleted.
    }

    /**
     * Deletes a node which has both left and right children.
     *
     * @param toDelete the node to delete from the tree.
     * @return the root of the subtree after the deletion.
     */
    private Node<Key, E> deleteNodeWithTwoChildren(Node<Key, E> toDelete) {
        // Decide which side replace the node from (from the higher subtree)
        int leftDepth = toDelete.getLeft().getHeight();
        int rightDepth = toDelete.getRight().getHeight();
        Node<Key, E> replacement;
        if (leftDepth > rightDepth) { // Replace node with the the rightmost child from the left subtree
            replacement = toDelete.getLeft();
            while (replacement.getRight() != null) { // Get the rightmost child
                replacement = replacement.getRight();
            }
            deleteRecursive(replacement.getKey(), toDelete); // Delete the replacement from tree
            replacement.setLeft(toDelete.getLeft());    // Copy the node's children to replacement
            replacement.setRight(toDelete.getRight());  // Copy the node's children to replacement
        } else { // Replace node with the leftmost child from the right subtree
            replacement = toDelete.getRight();
            while (replacement.getLeft() != null) { // Get the leftmost child
                replacement = replacement.getLeft();
            }
            deleteRecursive(replacement.getKey(), toDelete); // Delete the replacement from tree
            replacement.setLeft(toDelete.getLeft());    // Copy the node's children to replacement
            replacement.setRight(toDelete.getRight());  // Copy the node's children to replacement
        }
        toDelete = replacement; // Replace node with the replacement
        return toDelete;
    }

    /**
     * Provides a list of the stored data in order of the keys.
     *
     * @return the list ordered by the keys.
     */
    public List<E> traverseInOrder() {
        return recursiveInOrder(new ArrayList<>(size), root);
    }

    /**
     * Recursive method to build up the list of the stored data ordered by the keys.
     *
     * @param list to build up
     * @param node the root of the subtree to build the list from
     * @return the list width the added data from the provided subtree.
     */
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

    /**
     * Provides a list of the stored data ordered from top to bottom and left to right, like:
     *
     *      B
     *     / \
     *    A   C
     *
     * B, A, C
     *
     * @return the ordered list
     */
    public List<E> traversePreOrder() {
        return recursivePreOrder(new ArrayList<>(size), root);
    }

    /**
     * Recursive method to build up the list of the stored data ordered from top to bottom and left to right, like:
     *
     *      B
     *     / \
     *    A   C
     *
     * B, A, C
     *
     * @param list to build up
     * @param node the root of the subtree to build the list from
     * @return the list with the added data from the provided subtree
     */
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

    /**
     * Provides a list of the stored data ordered left to right and from bottom to top, like:
     *
     *      B
     *     / \
     *    A   C
     *
     * A, C, B
     *
     * @return the ordered list
     */
    public List<E> traversePostOrder() {
        return recursivePostOrder(new ArrayList<>(size), root);
    }

    /**
     * Recursive method to build up the list of the stored data ordered left to right and from bottom to top, like:
     *
     *      B
     *     / \
     *    A   C
     *
     * A, C, B
     *
     * @param list to build up
     * @param node the root of the subtree to build the list from
     * @return the list with the added  data from the provided subtree
     */
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

    /**
     * Provides a list of the stored data ordered by how high a node is placed and left to right, like:
     *
     *       D
     *     /   \
     *    B     F
     *   / \   / \
     *  A   C E   G
     *
     * D, B, F, A, C, E, G
     *
     * @return the ordered list
     */
    public List<E> traverseOnLevel() {
        List<E> list = new ArrayList<>(size);
        Queue<Node<Key, E>> queue = new ArrayDeque<>((int) Math.ceil((size + 1) / 2f));
        if (root != null) queue.add(root); // the first level is the root, if not null
        while (!queue.isEmpty()) {
            Node<Key, E> node = queue.poll();
            list.add(node.getData()); // build the list with the data of the next node in the queue
            // Add the children of the node to the queue from left to right if they're not null
            if (node.hasLeft()) queue.add(node.getLeft());
            if (node.hasRight()) queue.add(node.getRight());
        }
        return list;
    }

    /**
     * Represents a node in the binary search tree.
     *
     * @param <Key> the type of the key which is used to order the data inside the binary tree
     * @param <T> The type of the data stored in the tree
     */
    public static class Node<Key, T> {

        /**
         * The key which is used to order the data inside the binary tree
         */
        private Key key;

        /**
         * The data to store in the node
         */
        private T data;

        /**
         * The parent of the node
         */
        private Node<Key, T> parent;

        /**
         * The left child of the node.
         */
        private Node<Key, T> left;

        /**
         * the right child of the node.
         */
        private Node<Key, T> right;

        /**
         * Base constructor.
         *
         * @param key the key to store the data with
         * @param data the data to store in the node
         * @param parent the parent of the node
         */
        public Node(Key key, T data, Node<Key, T> parent) {
            this.key = key;
            this.data = data;
            this.parent = parent;
            left = null;
            right = null;
        }

        /**
         * Getter of the key
         *
         * @return the key of the node
         */
        public Key getKey() {
            return key;
        }

        /**
         * Getter of the stored data.
         *
         * @return the stored data.
         */
        public T getData() {
            return data;
        }

        /**
         * Setter of the stored data.
         *
         * @param data the new data to store in the node.
         */
        public void setData(T data) {
            this.data = data;
        }

        /**
         * Getter of the parent of the node.
         *
         * @return the parent of the node.
         */
        public Node<Key, T> getParent() {
            return parent;
        }

        /**
         * Setter of the parent of the node.
         *
         * @param parent the new parent of the node.
         */
        public void setParent(Node<Key, T> parent) {
            this.parent = parent;
        }
        // TODO set parents in deletion

        /**
         * Returns if the node has a left child or not.
         *
         * @return true if the node has a left child.
         */
        public boolean hasLeft() {
            return left != null;
        }

        /**
         * Getter of the left child of the node.
         *
         * @return the left child of the node.
         */
        public Node<Key, T> getLeft() {
            return left;
        }

        /**
         * Setter of the left child of the node.
         *
         * @param left the new left child of the node.
         */
        public void setLeft(Node<Key, T> left) {
            this.left = left;
        }

        /**
         * Returns if the node has a right child or not.
         *
         * @return true if the node has a right child.
         */
        public boolean hasRight() {
            return right != null;
        }

        /**
         * Getter of the right child of the node.
         *
         * @return the right child of the node.
         */
        public Node<Key, T> getRight() {
            return right;
        }

        /**
         * Setter of the right child of the node.
         *
         * @param right the new right child of the node.
         */
        public void setRight(Node<Key, T> right) {
            this.right = right;
        }

        /**
         * Provides the height of the subtree whose root is this node
         *
         * @return the hight of the subtree whose root is this node.
         */
        private int getHeight() {
            int rightDepth = right == null ? 0 : right.getHeight();
            int leftDepth = left == null ? 0 : left.getHeight();

            if (rightDepth > leftDepth) {
                return rightDepth + 1;
            } else {
                return leftDepth + 1;
            }
        }
    }
}
