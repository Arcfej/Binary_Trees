package miklos.mayer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private static InputStream backup;

    @BeforeAll
    static void beforeAll() {
        backup = System.in;
    }

    @AfterAll
    static void afterAll() {
        System.setIn(backup);
    }

    @Test
    void addProduct() {
        String input = "1\n1\nTest Product\n10";
    }

    @Test
    void addProducts_printShopCatalogue() {
        String input = "10\n10\nArcfej\n1\n2\n3\n4\n5\n6\nn";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

}