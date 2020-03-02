package miklos.mayer;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Represents a Menu for the shop-catalogue program.
 */
public class Menu {

    /**
     * Content separator line for displaying purposes
     */
    public static final String LINE_SEPARATOR = "-------------------------------------";

    /**
     * A list of eligible menu options for displaying purposes
     */
    public static final String MENU = "1) Add a new product to your shop\n" +
            "2) Print shop catalogue\n" +
            "3) Find product\n" +
            "4) Print total cost of products\n" +
            "5) Delete product\n" +
            "6) Sell product from stock\n" +
            "7) Replace stock\n" +
            "0) Exit\n";

    /**
     * The shop of the program.
     */
    private Shop shop;

    /**
     * Base constructor
     */
    public Menu() {
        shop = new Shop();
        restoreCatalogue();
    }

    /**
     * The entry point of the program. Displays the menu to the user in the first place.
     *
     * @param args the starting algorithms of the program.
     */
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMenu();
    }

    /**
     * Displays the menu to the user and execute methods based on their choice.
     * Save the catalogue after every modifications made to it.
     */
    public void displayMenu() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(MENU);
            // Get menu chose from the user
            switch (in.nextLine()) {
                case "0":
                    System.out.println("Goodbye!");
                    System.exit(1);
                    saveCatalogue();
                case "1":
                    addProduct(in);
                    saveCatalogue();
                    break;
                case "2":
                    List<Product> productList = shop.getOrderedCatalogue();
                    printProducts(productList);
                    break;
                case "3":
                    findProduct(in);
                    break;
                case "4":
                    printTotalCost();
                    break;
                case "5":
                    deleteProduct(in);
                    saveCatalogue();
                    break;
                case "6":
                    sellProduct(in);
                    saveCatalogue();
                    break;
                case "7":
                    increaseStock(in);
                    saveCatalogue();
                    break;
                default:
                    System.out.println("Not a valid command!");
            }
            System.out.println(LINE_SEPARATOR);
        }
    }

    /**
     * Takes input from the user for a new product, validates it and add it to the shop.
     *
     * @param in The Scanner through the user communicates with the program
     *
     */
    private void addProduct(Scanner in) {
        int id = getIntInput(in,
                "Please enter an ID for the new product:",
                "You have not entered a whole number for the new ID or the shop already has a product with the ID.",
                integer -> integer > 0 && !shop.containsId(integer)
        );
        String name = getTextInput(in,
                "Please enter the name of the product:",
                "",
                s -> !s.isBlank()
        );
        float cost = getFloatInput(in,
                "Please enter the cost of the product:",
                "Please enter a valid amount",
                aFloat -> aFloat >= 0
        );
        int quantity = getIntInput(in,
                "Please enter the initial quantity of the product:",
                "Please enter a not negative, whole number!",
                integer -> integer >= 0
        );
        try {
            shop.addNewProduct(id, name, cost, quantity);
        } catch (IllegalArgumentException e) {
            // There shouldn't be any error after the validation
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints a list of products to the screen.
     *
     * @param productList The list of products to print out.
     */
    private void printProducts(List<Product> productList) {
        System.out.printf("|%10s|%20s|%10s|%10s|\n", "ID", "Name", "Cost", "Stock");
        System.out.println(String.format("|%10s|%20s|%10s|%10s|", "", "", "", "").replace(" ", "-"));
        productList.forEach(product ->
                System.out.printf("|%10s|%20s|%10s|%10s|\n", product.getId(), product.getName(), "£" + product.getCost(), product.getStock())
        );
        System.out.println(String.format("|%10s|%20s|%10s|%10s|", "", "", "", "").replace(" ", "-"));
        System.out.println();
    }

    /**
     * Gets input from the user for a Product ID, search for it in the catalogue and print it to the user.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void findProduct(Scanner in) {
        int id = getIntInput(in,
                "Please enter the ID of the product you're looking for:",
                "You have not entered a valid ID. ID must be a whole positive number.",
                integer -> integer > 0
        );
        Product product = shop.findProduct(id);
        if (product == null) {
            System.out.println("The required product is not in the shop");
        } else {
            printProducts(List.of(product));
        }
    }

    /**
     * Prints the total cost of all the Products in the catalogue.
     */
    private void printTotalCost() {
        System.out.println("The total cost of products is £" + shop.getTotalCost());
    }

    /**
     * Gets input from the user for Product ID and delete it from the shop catalogue.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void deleteProduct(Scanner in) {
        int id = getIntInput(in,
                "Please enter the ID of the product you'd like to delete:",
                "You have not entered a valid ID. ID must be a whole positive number.",
                integer -> integer > 0
        );
        if (shop.deleteProduct(id)) {
            System.out.println("The product have been deleted successfully");
        } else {
            System.out.println("There is no product in the catalogue with this id.");
        }
    }

    /**
     * Gets input from the user for a Product ID and a quantity of how much they has sold of it,
     * then decrease the stock level in the catalogue.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void sellProduct(Scanner in) {
        int id;
        Product product;
        while (true) {
            id = getIntInput(in,
                    "Please enter the ID of the product you've sold:",
                    "You have not entered a valid ID. ID must be a whole positive number.",
                    integer -> integer > 0
            );
            product = shop.findProduct(id);
            if (product == null) {
                System.out.println("The required product is not in the shop");
            } else {
                break;
            }
        }
        final Product finalProduct = product;
        int quantity = getIntInput(in,
                "Please enter the quantity of the purchase:",
                "Please enter a whole number between 1 and the stock of the product (" + finalProduct.getStock() + ")!",
                integer -> integer > 0 && integer < finalProduct.getStock()
        );
        product.decreaseStock(quantity);
    }
    /**
     * Gets input from the user for a Product ID and a quantity of how much they would like to increase the stock,
     * then increase the product's stock.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void increaseStock(Scanner in) {
        int id;
        Product product;
        while (true) {
            id = getIntInput(in,
                    "Please enter the ID of the product whose stock you increased:",
                    "You have not entered a valid ID. ID must be a whole positive number.",
                    integer -> integer > 0
            );
            product = shop.findProduct(id);
            if (product == null) {
                System.out.println("The required product is not in the shop");
            } else {
                break;
            }
        }
        int quantity = getIntInput(in,
                "Please enter the additional quantity of the product:",
                "Please enter a whole number bigger than 0",
                integer -> integer > 0
        );
        product.increaseStock(quantity);
    }

    /**
     * Gets a validated String input from the user. Repeats the asking until the user provides a valid input.
     *
     * @param in The Scanner through the user communicates with the program
     * @param question The text to display to the user before getting the input
     * @param errorMessage The message to display to the user after a failed validation
     * @param validation the condition for validation
     * @return The validated input from the user.
     */
    private String getTextInput(Scanner in, String question, String errorMessage, Predicate<String> validation) {
        String input;
        while (true) {
            System.out.println(question);
            input = in.nextLine();
            if (validation.test(input)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }

    /**
     * Gets a validated float input from the user. Repeats the asking until the user provides a valid input.
     *
     * @param in The Scanner through the user communicates with the program
     * @param question The text to display to the user before getting the input
     * @param errorMessage The message to display to the user after a failed validation
     * @param validation the condition for validation
     * @return The validated input from the user.
     */
    private float getFloatInput(Scanner in, String question, String errorMessage, Predicate<Float> validation) {
        float input;
        while (true) {
            System.out.println(question);
            try {
                input = Float.parseFloat(in.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println(errorMessage);
                continue;
            }
            if (validation.test(input)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }

    /**
     * Gets a validated integer input from the user. Repeats the asking until the user provides a valid input.
     *
     * @param in The Scanner through the user communicates with the program
     * @param question The text to display to the user before getting the input
     * @param errorMessage The message to display to the user after a failed validation
     * @param validation the condition for validation
     * @return The validated input from the user.
     */
    private int getIntInput(Scanner in, String question, String errorMessage, Predicate<Integer> validation) {
        int input;
        while (true) {
            System.out.println(question);
            try {
                input = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println(errorMessage);
                continue;
            }
            if (validation.test(input)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }

    /**
     * Getter of the shop object.
     *
     * @return the shop object.
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * Save the state of the catalogue into a txt file.
     */
    private void saveCatalogue() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("shop.txt"))) {
            for (Product product : shop.getCatalogueForSave()) {
                writer.println(product.getId() + "," + product.getName() + "," + product.getCost() + "," + product.getStock());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restore the shop catalogue from the previously saved txt file.
     */
    private void restoreCatalogue() {
        try (Scanner scanner = new Scanner(new File("shop.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                shop.addNewProduct(Integer.parseInt(data[0]), data[1], Float.parseFloat(data[2]), Integer.parseInt(data[3]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset the shop, delete all the products in it.
     */
    public void resetShop() {
        shop = new Shop();
    }
}
