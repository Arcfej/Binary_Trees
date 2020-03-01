package miklos.mayer;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopMenuTest {

    private static Menu menu;

    private static Shop shop;

    private static InputStream backupInput;
    private static PrintStream backupOutput;

    private static ByteArrayOutputStream out;

    @BeforeAll
    static void beforeAll() {
        backupInput = System.in;
        backupOutput = System.out;

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        menu = new Menu();
        menu.resetShop();
        shop = menu.getShop();
    }

    @AfterAll
    static void afterAll() {
        System.setIn(backupInput);
        System.setOut(backupOutput);
    }

    @Test
    @Order(1)
    void testInvalidInputForMenu() {
        String input = "10\ngg\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(2)
    void printEmptyCatalogue() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "|        ID|                Name|      Cost|     Stock|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(3)
    void addProduct() {
        String input = "1\n3\nTest Product 3\n12.5\n10\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals(
                List.of(new Product(3, "Test Product 3", 12.5f, 10)),
                shop.getOrderedCatalogue());
    }

    @Test
    @Order(4)
    void addMultipleProduct() {
        String input = "";
        input += "1\n4\nTest Product 4\n13.5\n11\n";
        input += "1\n2\nTest Product 2\n11.5\n9\n";
        input += "1\n5\nTest Product 5\n14.5\n12\n";
        input += "1\n1\nTest Product 1\n10.5\n8\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals(
                List.of(
                        new Product(1, "Test Product 1", 10.5f, 8),
                        new Product(2, "Test Product 2", 11.5f, 9),
                        new Product(3, "Test Product 3", 12.5f, 10),
                        new Product(4, "Test Product 4", 13.5f, 11),
                        new Product(5, "Test Product 5", 14.5f, 12)),
                shop.getOrderedCatalogue());
    }

    @Test
    @Order(5)
    void printShopCatalogue() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "|        ID|                Name|      Cost|     Stock|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "|         1|      Test Product 1|     £10.5|         8|\n" +
                        "|         2|      Test Product 2|     £11.5|         9|\n" +
                        "|         3|      Test Product 3|     £12.5|        10|\n" +
                        "|         4|      Test Product 4|     £13.5|        11|\n" +
                        "|         5|      Test Product 5|     £14.5|        12|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(6)
    void testInvalidInputForAddNewProduct() {
        String input = "10\ngg\n\n1\ngg\n-1\n8.5\n111\n \n\ntest\n-5.8\nhhh\n5.8\n-5\njjj\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "Not a valid command!\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "Please enter an ID for the new product:\n" +
                        "You have not entered a whole number for the new ID or the shop already has a product with the ID.\n" +
                        "Please enter an ID for the new product:\n" +
                        "You have not entered a whole number for the new ID or the shop already has a product with the ID.\n" +
                        "Please enter an ID for the new product:\n" +
                        "You have not entered a whole number for the new ID or the shop already has a product with the ID.\n" +
                        "Please enter an ID for the new product:\n" +
                        "Please enter the name of the product:\n" +
                        "\n" +
                        "Please enter the name of the product:\n" +
                        "\n" +
                        "Please enter the name of the product:\n" +
                        "Please enter the cost of the product:\n" +
                        "Please enter a valid amount\n" +
                        "Please enter the cost of the product:\n" +
                        "Please enter a valid amount\n" +
                        "Please enter the cost of the product:\n" +
                        "Please enter the initial quantity of the product:\n" +
                        "Please enter a not negative, whole number!\n" +
                        "Please enter the initial quantity of the product:\n" +
                        "Please enter a not negative, whole number!\n" +
                        "Please enter the initial quantity of the product:\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(7)
    void testDuplicateProduct() {
        String input = "1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter an ID for the new product:\n" +
                        "You have not entered a whole number for the new ID or the shop already has a product with the ID.\n" +
                        "Please enter an ID for the new product:\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(8)
    void searchProduct() {
        String input = "3\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "|        ID|                Name|      Cost|     Stock|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "|         1|      Test Product 1|     £10.5|         8|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(9)
    void searchNonexistentProduct() {
        String input = "3\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "The required product is not in the shop\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(10)
    void printTotalCostOfProducts() {
        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "The total cost of products is £635.0\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(11)
    void testShopRestore() {
        menu = new Menu();
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "|        ID|                Name|      Cost|     Stock|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "|         1|      Test Product 1|     £10.5|         8|\n" +
                        "|         2|      Test Product 2|     £11.5|         9|\n" +
                        "|         3|      Test Product 3|     £12.5|        10|\n" +
                        "|         4|      Test Product 4|     £13.5|        11|\n" +
                        "|         5|      Test Product 5|     £14.5|        12|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(12)
    void testInvalidInputForSearch() {
        String input = "3\ngg\n\n5.8\n-89\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you're looking for:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you're looking for:\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(13)
    void testInvalidInputForDelete() {
        String input = "5\ngg\n\n5.8\n-89\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "You have not entered a valid ID. ID must be a whole positive number.\n" +
                        "Please enter the ID of the product you'd like to delete:\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(14)
    void deleteProduct() {
        String input = "5\n3\n2";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "The product have been deleted successfully\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n" +
                        "|        ID|                Name|      Cost|     Stock|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "|         1|      Test Product 1|     £10.5|         8|\n" +
                        "|         2|      Test Product 2|     £11.5|         9|\n" +
                        "|         4|      Test Product 4|     £13.5|        11|\n" +
                        "|         5|      Test Product 5|     £14.5|        12|\n" +
                        "|----------|--------------------|----------|----------|\n" +
                        "\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(15)
    void deleteNonexistentProduct() {
        String input = "5\n3";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        out.reset();
        try {
            menu.displayMenu();
        } catch (NoSuchElementException ignored) {
        }
        assertEquals((Menu.MENU + "\n" +
                        "Please enter the ID of the product you'd like to delete:\n" +
                        "There is no product in the catalogue with this id.\n" +
                        Menu.LINE_SEPARATOR + "\n" +
                        Menu.MENU + "\n"
                ).replaceAll("\\n|\\r\\n", System.lineSeparator()),
                out.toString().replaceAll("\\n|\\r\\n", System.lineSeparator())
        );
    }

    @Test
    @Order(16)
    void sellProduct() {

    }
}