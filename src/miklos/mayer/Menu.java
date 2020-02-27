package miklos.mayer;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Menu {

    private static final String LINE_SEPARATOR = "-----------------------";

    private Shop shop;

    public Menu() {
        shop = new Shop();
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMenu();
    }

    public void displayMenu() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("1) Add a new product to your shop");
            System.out.println("2) Print shop catalogue");
            System.out.println("0) Exit");

            switch (in.nextLine()) {
                case "0":
                    System.out.println("Goodbye!");
                    System.exit(1);
                case "1":
                    addProduct(in);
                    break;
                case "2":
                    List<Product> productList = shop.getCatalogue();
                    System.out.printf("|%10s|%20s|%10s|%10s|\n", "ID", "Name", "Cost", "Stock");
                    System.out.println(String.format("|%10s|%20s|%10s|%10s|", "", "", "", "").replace(" ", "-"));
                    productList.forEach(product ->
                            System.out.printf("|%10s|%20s|%10s|%10s|\n", product.getId(), product.getName(), "£" + product.getCost(), product.getStock())
                    );
                    System.out.println(String.format("|%10s|%20s|%10s|%10s|", "", "", "", "").replace(" ", "-"));
                    System.out.println();
                    break;
                default:
                    System.out.println("Not a valid command!");
            }
            System.out.println(LINE_SEPARATOR);
        }
    }

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
            System.out.println(e.getMessage());
        }
    }

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

    public Shop getShop() {
        return shop;
    }
}
