package miklos.mayer;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Represents the main user interface for the shop-catalogue program.
 */
public class Menu {

    /**
     * Error message to display when the id input is invalid
     */
    private static final String ERROR_ID_INPUT = "ID > 0";

    /**
     * Error message to display when the given id is already exist
     */
    private static final String ERROR_ID_TAKEN = "ID already taken";

    /**
     * Error message to display when the name field remained empty
     */
    private static final String ERROR_NAME_INPUT = "Name mustn't be blank";

    /**
     * Error message to display when the cost input is invalid
     */
    private static final String ERROR_COST_INPUT = "Cost > 0";

    /**
     * Error message to display when the quantity input is invalid.
     */
    private static final String ERROR_QUANTITY_INPUT = "Quan.>=0";

    /**
     * The shop of the program.
     */
    private final Shop shop;

    /**
     * The property which holds the value of the products' total cost.
     */
    private FloatProperty totalCost;

    /**
     * The table in which the products displayed
     */
    @FXML
    private TableView<Product> table;

    /**
     * The button which can show all products
     */
    @FXML
    private Button btn_show_all;

    /**
     * Input field for searching products
     */
    @FXML
    private TextField input_search;

    /**
     * Input field for an id of a new product
     */
    @FXML
    private TextField input_id;

    /**
     * Input field for a name for a nuw product
     */
    @FXML
    private TextField input_name;

    /**
     * Input field for the cost of a new product
     */
    @FXML
    private TextField input_cost;

    /**
     * Input field for the initial stock level of a new product
     */
    @FXML
    private TextField input_quantity;

    /**
     * Label to display the total cost of the products
     */
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
     * Called when the stage is initialized. Initialize all the Nodes
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
            if (column.getId() != null && column.getId().equals("column_stock")) {
                // Custom column for stock levels with spinner to edit it
                TableColumn<Product, Void> columnStock = new TableColumn<>("Stock");
                columnStock.setCellFactory(clmn -> new TableCell<>() {
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product product = table.getItems().get(getIndex());
                            Spinner<Integer> spinner = new Spinner<>(0, Integer.MAX_VALUE, product.getStock());
                            spinner.setEditable(true);
                            spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
                            spinner.setMaxWidth(90);

                            spinner.valueProperty().addListener((observable, oldNum, newNum) -> {
                                if (newNum >= 0) {
                                    product.setStock(newNum);
                                    totalCost.set(shop.getTotalCost());
                                    saveCatalogue();
                                }
                            });

                            setGraphic(spinner);
                        }
                    }
                });
                columns.set(i, columnStock);
            } else {
                column.setCellValueFactory(new PropertyValueFactory<>(fieldNames[i]));
            }
        }

        // Create the delete product column with delete button
        TableColumn<Product, Void> deleteColumn = new TableColumn<>();
        deleteColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Button btn = new Button("X");
                // Delete on click
                btn.setOnAction(actionEvent -> {
                    Product product = table.getItems().get(getIndex());
                    deleteProduct(product);
                });
                // Display the button
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        columns.add(deleteColumn);

        displayProducts(shop.getOrderedCatalogue());

        // Initialize total cost displaying
        totalCost = new SimpleFloatProperty();
        lbl_total_cost.textProperty().bind(totalCost.asString("£%s"));
        totalCost.set(shop.getTotalCost());
    }

    /**
     * Takes input from the user for a new product, validates it and add it to the shop.
     *
     * @param e The event which called this method
     */
    @FXML
    public void addProduct(Event e) {
        // Initial, invalid values
        int id = -1;
        String name = "";
        float cost = -1;
        int quantity = -1;

        Predicate<Integer> idValidation = i -> i > 0 && !shop.containsId(i);
        Predicate<String> nameValidation = s -> !s.isBlank();
        Predicate<Float> costValidation = f -> f > 0;
        Predicate<Integer> quantityValidation = i -> i >= 0;

        // Get id
        try {
            id = Integer.parseInt(input_id.getText());
            if (!idValidation.test(id)) {
                displayError(input_id, ERROR_ID_INPUT);
            } else if (shop.containsId(id)) {
                displayError(input_id, ERROR_ID_TAKEN);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_id, ERROR_ID_INPUT);
        }

        // Get name
        name = input_name.getText();
        if (!nameValidation.test(name)) {
            displayError(input_name, ERROR_NAME_INPUT);
        }

        // Get cost
        try {
            cost = Float.parseFloat(input_cost.getText());
            if (!costValidation.test(cost)) {
                displayError(input_cost, ERROR_COST_INPUT);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_cost, ERROR_COST_INPUT);
        }

        // Get initial stock level
        try {
            quantity = Integer.parseInt(input_quantity.getText());
            if (!quantityValidation.test(quantity)) {
                displayError(input_quantity, ERROR_QUANTITY_INPUT);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_quantity, ERROR_QUANTITY_INPUT);
        }

        // If all the inputs are valid, add the new product
        if (idValidation.test(id) && nameValidation.test(name) && costValidation.test(cost) && quantityValidation.test(quantity)) {
            try {
                shop.addNewProduct(id, name, cost, quantity);
                input_id.clear();
                input_id.setPromptText("");
                input_name.clear();
                input_name.setPromptText("");
                input_cost.clear();
                input_cost.setPromptText("");
                input_quantity.clear();
                input_quantity.setPromptText("");
                displayProducts(shop.getOrderedCatalogue());
                totalCost.set(shop.getTotalCost());
                saveCatalogue();
            } catch (IllegalArgumentException exception) {
                // There shouldn't be any error after the validation
                System.out.println(exception.getMessage());
            }
        }
    }

    /**
     * Prints a list of products to the screen.
     *
     * @param e The event which called this method
     */
    @FXML
    private void displayProducts(Event e) {
        if (e.getSource() instanceof Button && e.getSource() == btn_show_all) {
            displayProducts(shop.getOrderedCatalogue());
        }
    }

    /**
     * Prints a list of products in the table
     *
     * @param products The list of products to display
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
     * Displays an empty table to the user with the given message
     *
     * @param emptyTableMessage The empty message to display
     */
    private void displayEmptyTable(String emptyTableMessage) {
        table.getItems().clear();
        table.setPlaceholder(new Label(emptyTableMessage));
    }

    /**
     * Gets input from the user for a Product ID, search for it in the catalogue and display it in the table.
     *
     * @param e The event which called this method
     */
    @FXML
    private void findProduct(Event e) {
        String input = input_search.getText();
        int id = 0;
        try {
            id = Integer.parseInt(input);
            if (id <= 0) {
                throw new NumberFormatException();
            }
            input_search.clear();
            input_search.setPromptText("Product ID");
            Product product = shop.findProduct(id);
            if (product == null) {
                displayEmptyTable("There is no product with the given ID");
            } else {
                displayProducts(List.of(product));
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_search, ERROR_ID_INPUT);
        }
    }

    /**
     * Delete a product from the shop's catalogue
     *
     * @param product The product to delete.
     */
    @FXML
    private void deleteProduct(Product product) {
        shop.deleteProduct(product.getId());
        displayProducts(shop.getOrderedCatalogue());
        totalCost.set(shop.getTotalCost());
        saveCatalogue();
    }

    /**
     * Displays an error to the user.
     *
     * @param target The area where the message should be displayed.
     * @param errorMessage The message to display
     */
    private void displayError(TextInputControl target, String errorMessage) {
        target.clear();
        target.setPromptText(errorMessage);
    }

    /**
     * Save the state of the catalogue into a txt file.
     */
    public void saveCatalogue() {
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
}
