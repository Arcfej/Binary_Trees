package miklos.mayer;

import javafx.beans.InvalidationListener;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * Represents a Menu for the shop-catalogue program.
 */
public class Menu {

    // TODO comments
    private static final String ERROR_ID_INPUT = "ID >= 0";
    private static final String ERROR_ID_TAKEN = "ID already taken";
    private static final String ERROR_NAME_INPUT = "Name mustn't be blank";
    private static final String ERROR_COST_INPUT = "Cost >= 0";
    private static final String ERROR_QUANTITY_INPUT = "Quantity >= 0";

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
            if (column.getId() != null && column.getId().equals("column_stock")) {
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

        // Create the delete product column
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

        // Initialize total cost
        totalCost = new SimpleFloatProperty();
        lbl_total_cost.textProperty().bind(totalCost.asString("£%s"));
        totalCost.set(shop.getTotalCost());
    }

    /**
     * Takes input from the user for a new product, validates it and add it to the shop.
     * TODO
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
        Predicate<Float> costValidation = f -> f >= 0;
        Predicate<Integer> quantityValidation = i -> i >= 0;

        try {
            id = Integer.parseInt(input_id.getText());
            if (id < 0) {
                displayError(input_id, ERROR_ID_INPUT);
            } else if (shop.containsId(id)) {
                displayError(input_id, ERROR_ID_TAKEN);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_id, ERROR_ID_INPUT);
        }

        name = input_name.getText();
        if (!nameValidation.test(name)) {
            displayError(input_name, ERROR_NAME_INPUT);
        }

        try {
            cost = Float.parseFloat(input_cost.getText());
            if (!costValidation.test(cost)) {
                displayError(input_cost, ERROR_COST_INPUT);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_cost, ERROR_COST_INPUT);
        }

        try {
            quantity = Integer.parseInt(input_quantity.getText());
            if (!quantityValidation.test(quantity)) {
                displayError(input_quantity, ERROR_QUANTITY_INPUT);
            }
        } catch (NumberFormatException numberFormatException) {
            displayError(input_quantity, ERROR_QUANTITY_INPUT);
        }

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
        int id = 0;
        try {
            id = Integer.parseInt(input);
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
     * Gets input from the user for Product ID and delete it from the shop catalogue.
     * TODO
     */
    @FXML
    private void deleteProduct(Product product) {
        shop.deleteProduct(product.getId());
        displayProducts(shop.getOrderedCatalogue());
        totalCost.set(shop.getTotalCost());
        saveCatalogue();
    }

    /**
     * TODO
     * @param target
     * @param errorMessage
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
