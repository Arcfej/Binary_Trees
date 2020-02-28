package miklos.mayer;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BinaryTreeTest {

    static BinaryTree<Integer> tree;

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
            tree.add(5);
        } catch (DuplicateItemException e) {
            fail("There shouldn't be any exception.");
        }
        assertEquals(5, tree.getRoot());
    }

    @Test
    @Order(3)
    void addFiveMoreItem_TraverseInOrder() {
        try {
            tree.add(1);
            tree.add(4);
            tree.add(6);
            tree.add(10);
            tree.add(8);
        } catch (DuplicateItemException e) {
            fail("There shouldn't be any exception");
        }
        assertEquals(new ArrayList<>(List.of(1, 4, 5, 6, 8, 10)), tree.traverseInOrder());
    }

    @Test
    @Order(4)
    void addDuplicateItem() {
        assertThrows(DuplicateItemException.class, () -> tree.add(8));
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
        assertFalse(tree.contains(11));
        assertFalse(tree.contains(555));
    }
}