package miklos.mayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    @Test
    void addOneItem() {
        BinaryTree<Integer> tree = new BinaryTree<>();
        try {
            tree.add(1);
            assertEquals(1, tree.getRoot());
        } catch (DuplicateItemException e) {
            fail("There shouldn't be any exception.");
        }
    }
}