package miklos.mayer;

import java.util.Scanner;
import java.util.function.Predicate;

public class Menu {

    private static final String LINE_SEPARATOR = "-----------------------";

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMenu();
    }

    private void displayMenu() {
        while (true) {
            System.out.println("1) Add a product to your shop");
            System.out.println("0) Exit");
            Scanner in = new Scanner(System.in);

            switch (in.nextLine()) {
                case "0":
                    System.out.println("Goodbye!");
                    System.exit(1);
                case "1":
                    addProduct(in);
                    break;
                default:
                    System.out.println("Not a valid command!");
                    System.out.println(LINE_SEPARATOR);
            }
        }
    }

    private void addProduct(Scanner in) {
        String id = getTextInput(in,
                "Please enter an ID for the new product:",
                "",
                String::isBlank
        );
        String name = getTextInput(in,
                "Please enter the name of the product:",
                "",
                String::isBlank
        );
        float cost = getFloatInput(in,
                "Please enter the cost of the product:",
                "Please enter a valid amount",
                aFloat -> aFloat >= 0
        );
        ShopItem product = new ShopItem(id, name, cost);
        // TODO add tree node
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
}
