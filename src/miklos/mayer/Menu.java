package miklos.mayer;

import javafx.application.Application;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Menu for the shop-catalogue program.
 */
public class Menu extends Application {

    /**
     * A list of eligible menu options for displaying purposes
     */
    public static final String MENU = "1) Add a new product to your shop\n" +
            "2) Print shop catalogue\n" +
            "3) Find product\n" +
            "4) Print total cost of products\n" +
            "5) Delete product\n" +
            "6) Sell product from stock\n" +
            "7) Increase stock\n" +
            "0) Exit\n";

    /**
     * The shop of the program.
     */
    private Shop shop;

    /**
     * TODO
     */
    private FloatProperty totalCost;

    // TODO comments
    @FXML
    private TableView<Product> table;
    @FXML
    private Button btn_show_all;
    @FXML
    private Button btn_search;
    @FXML
    private TextField input_search;
    @FXML
    private TextField input_id;
    @FXML
    private TextField input_name;
    @FXML
    private TextField input_cost;
    @FXML
    private TextField input_quantity;
    @FXML
    private Button btn_add;
    @FXML
    private Label lbl_total_cost;

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
     * @param args the starting arguments of the program.
     */
    public static void main(String[] args) {
        Menu menu = new Menu();
//        menu.displayMenu();
        Application.launch(Menu.class, args);
    }

    /**
     * TODO
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle("Shop");
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setMinWidth(778);
        primaryStage.setMinHeight(300);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * TODO
     */
    @FXML
    public void initialize() {
        // Initialize table
        Field[] fields = Product.class.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].toString();
        }

        fieldNames = Arrays.stream(fieldNames)
                .map(field -> {
                    String[] sub = field.split("\\.");
                    return sub[sub.length - 1];
                })
                .toArray(String[]::new);

        ObservableList<TableColumn<Product, ?>> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<Product, ?> column = columns.get(i);
            column.setCellValueFactory(new PropertyValueFactory<>(fieldNames[i]));
        }
        displayProducts(shop.getOrderedCatalogue());

        // Initialize total cost
        totalCost = new SimpleFloatProperty();
        lbl_total_cost.textProperty().bind(totalCost.asString("£%s"));
        totalCost.set(shop.getTotalCost());
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
//                    addProduct(in);
                    saveCatalogue();
                    break;
                case "2":
                    List<Product> productList = shop.getOrderedCatalogue();
//                    printProducts(productList);
                    break;
                case "3":
//                    findProduct(in);
                    break;
                case "4":
//                    printTotalCost();
                    break;
                case "5":
//                    deleteProduct(in);
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
//            System.out.println(LINE_SEPARATOR);
        }
    }

    /**
     * Takes input from the user for a new product, validates it and add it to the shop.
     * TODO
     */
    @FXML
    public void addProduct(Event e) {
//        int id = getIntInput(in,
//                "Please enter an ID for the new product:",
//                "You have not entered a whole number for the new ID or the shop already has a product with the ID.",
//                integer -> integer > 0 && !shop.containsId(integer)
//        );
//        String name = getTextInput(in,
//                "Please enter the name of the product:",
//                "",
//                s -> !s.isBlank()
//        );
//        float cost = getFloatInput(in,
//                "Please enter the cost of the product:",
//                "Please enter a valid amount",
//                aFloat -> aFloat >= 0
//        );
//        int quantity = getIntInput(in,
//                "Please enter the initial quantity of the product:",
//                "Please enter a not negative, whole number!",
//                integer -> integer >= 0
//        );
//        try {
//            shop.addNewProduct(id, name, cost, quantity);
//        } catch (IllegalArgumentException e) {
//            // There shouldn't be any error after the validation
//            System.out.println(e.getMessage());
//        }
    }

    /**
     * Prints a list of products to the screen.
     * TODO
     */
    @FXML
    private void displayProducts(Event e) {
        if (e.getSource() instanceof Button && e.getSource() == btn_show_all) {
            displayProducts(shop.getOrderedCatalogue());
        }
    }

    /**
     * TODO
     * @param products
     */
    private void displayProducts(List<Product> products) {
        table.getItems().clear();
        if (products != null) {
            if (products.size() == 0) {
                displayEmptyTable("No products to display");
            } else {
                table.getItems().addAll(products);
            }
        }
    }

    /**
     * TODO
     * @param emptyTableMessage
     */
    private void displayEmptyTable(String emptyTableMessage) {
        table.getItems().clear();
        table.setPlaceholder(new Label(emptyTableMessage));
    }

    /**
     * Gets input from the user for a Product ID, search for it in the catalogue and print it to the user.
     * TODO
     */
    @FXML
    private void findProduct(Event e) {
        String input = input_search.getText();
        input_search.clear();
        int id = 0;
        try {
            id = Integer.parseInt(input);

            Product product = shop.findProduct(id);
            if (product == null) {
                displayEmptyTable("There is no product with the given ID");
            } else {
                displayProducts(List.of(product));
                input_search.setPromptText("Product ID");
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_search, "ID is whole & positive number");
        }
    }

    /**
     * Prints the total cost of all the Products in the catalogue.
     * TODO
     */
    @FXML
    private void displayTotalCost(Event e) {
        input_cost.setText("£" + shop.getTotalCost());
    }

    /**
     * Gets input from the user for Product ID and delete it from the shop catalogue.
     * TODO
     */
    @FXML
    private void deleteProduct(Event e) {
//        int id = getIntInput(in,
//                "Please enter the ID of the product you'd like to delete:",
//                "You have not entered a valid ID. ID must be a whole positive number.",
//                integer -> integer > 0
//        );
//        if (shop.deleteProduct(id)) {
//            System.out.println("The product have been deleted successfully");
//        } else {
//            System.out.println("There is no product in the catalogue with this id.");
//        }
    }

    /**
     * Gets input from the user for a Product ID and a quantity of how much they has sold of it,
     * then decrease the stock level in the catalogue.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void sellProduct(Scanner in) {
//        int id;
//        Product product;
//        while (true) {
//            id = getIntInput(in,
//                    "Please enter the ID of the product you've sold:",
//                    "You have not entered a valid ID. ID must be a whole positive number.",
//                    integer -> integer > 0
//            );
//            product = shop.findProduct(id);
//            if (product == null) {
//                System.out.println("The required product is not in the shop");
//            } else {
//                break;
//            }
//        }
//        final Product finalProduct = product;
//        int quantity = getIntInput(in,
//                "Please enter the quantity of the purchase:",
//                "Please enter a whole number between 1 and the stock of the product (" + finalProduct.getStock() + ")!",
//                integer -> integer > 0 && integer < finalProduct.getStock()
//        );
//        product.decreaseStock(quantity);
    }

    /**
     * Gets input from the user for a Product ID and a quantity of how much they would like to increase the stock,
     * then increase the product's stock.
     *
     * @param in The Scanner through the user communicates with the program
     */
    private void increaseStock(Scanner in) {
//        int id;
//        Product product;
//        while (true) {
//            id = getIntInput(in,
//                    "Please enter the ID of the product whose stock you increased:",
//                    "You have not entered a valid ID. ID must be a whole positive number.",
//                    integer -> integer > 0
//            );
//            product = shop.findProduct(id);
//            if (product == null) {
//                System.out.println("The required product is not in the shop");
//            } else {
//                break;
//            }
//        }
//        int quantity = getIntInput(in,
//                "Please enter the additional quantity of the product:",
//                "Please enter a whole number bigger than 0",
//                integer -> integer > 0
//        );
//        product.increaseStock(quantity);
    }

    /**
     * TODO
     * @param target
     * @param errorMessage
     */
    private void displayError(Labeled target, String errorMessage) {
        target.setText(errorMessage);
    }

    /**
     * TODO
     * @param target
     * @param errorMessage
     */
    private void displayError(TextInputControl target, String errorMessage) {
        target.setPromptText(errorMessage);
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
