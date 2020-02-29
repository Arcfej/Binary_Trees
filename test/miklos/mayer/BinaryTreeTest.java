package miklos.mayer;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BinaryTreeTest {

    static BinaryTree<Integer, Integer> tree;

    @BeforeAll
    static void beforeAll() {
        tree = new BinaryTree<>();
    }

    @Test
    @Order(1)
    void traverseEmptyList_inOrder() {
        assertEquals(new ArrayList<>(), tree.traverseInOrder());
    }

    @Test
    @Order(2)
    void addOneItem() {
        try {
            tree.add(6, 6);
        } catch (DuplicateItemException e) {
            fail("There shouldn't be any exception.");
        }
        assertEquals(6, tree.getRoot());
    }

    @Test
    @Order(3)
    void addFiveMoreItem_TraverseInOrder() {
        try {
            tree.add(3, 3);
            tree.add(5, 5);
            tree.add(2, 2);
            tree.add(9, 9);
            tree.add(1, 1);
            tree.add(4, 4);
            tree.add(7, 7);
            tree.add(10, 10);
            tree.add(8, 8);
            tree.add(11, 11);
        } catch (DuplicateItemException e) {
            fail("There shouldn't be any exception");
        }
        assertEquals(new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)), tree.traverseInOrder());
    }

    @Test
    @Order(4)
    void addDuplicateItem() {
        assertThrows(DuplicateItemException.class, () -> tree.add(8, 8));
    }

    @Test
    @Order(5)
    void testContainsMethod() {
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(4));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(6));
        assertTrue(tree.contains(8));
        assertTrue(tree.contains(10));
        assertFalse(tree.contains(0));
        assertFalse(tree.contains(21));
        assertFalse(tree.contains(555));
    }

    @Test
    @Order(6)
    void findData() {
        assertEquals(10, tree.find(10));
    }

    @Test
    @Order(7)
    void findNonexistentData() {
        assertNull(tree.find(555));
    }

    @Test
    @Order(8)
    void testTraversePreOrder() {
        assertEquals(List.of(6, 3, 2, 1, 5, 4, 9, 7, 8, 10, 11), tree.traversePreOrder());
    }

    @Test
    @Order(9)
    void testTraversePostOrder() {
        assertEquals(List.of(1, 2, 4, 5, 3, 8, 7, 11, 10, 9, 6), tree.traversePostOrder());
    }

    @Test
    @Order(10)
    void testTraverseOnLevel() {
        assertEquals(List.of(6, 3, 9, 2, 5, 7, 10, 1, 4, 8, 11), tree.traverseOnLevel());
    }
}