package miklos.mayer;

import java.util.List;

/**
 * Represents the shop in the program. Store its product catalogue in a binary search tree.
 */
public class Shop {

    /**
     * The product catalogue of the shop, stored in a binary tree.
     */
    private BinaryTree<Integer, Product> catalogue;

    /**
     * Base constructor
     */
    public Shop() {
        this.catalogue = new BinaryTree<>();
    }

    /**
     * Provides a list of all the products ordered by id.
     *
     * @return the ordered list of products.
     */
    public List<Product> getOrderedCatalogue() {
        return catalogue.traverseInOrder();
    }

    /**
     * Provides a list of all the products ordered in a way which make restoring of the shop from a backup effective.
     *
     * @return the ordered list of products.
     */
    public List<Product> getCatalogueForSave() {
        return catalogue.traversePreOrder();
    }

    /**
     * Add a new product to the catalogue.
     *
     * @param id the ID of the new product
     * @param name the name of the new product
     * @param price the price of the new product
     * @param quantity the initial stock level of the new product
     * @throws IllegalArgumentException if the provided product details are not valid
     *                                  * ID is negative or already used
     *                                  * product name is blank
     *                                  * price is a negative number
     *                                  * quantity is a negative number
     */
    public void addNewProduct(int id, String name, float price, int quantity) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be bigger than 0!");
        } else if (containsId(id)) {
            throw new IllegalArgumentException("Id must be unique!");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Product name mustn't be empty!");
        } else if (price <= 0) {
            throw new IllegalArgumentException("Price should be bigger than 0!");
        } else if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can't be a negative number!");
        }
        try {
            catalogue.add(id, new Product(id, name, price, quantity));
        } catch (DuplicateItemException e) {
            throw new IllegalArgumentException("Product is already in the catalogue!");
        }
    }

    /**
     * Returns if the catalogue contains a product with the provided ID or not.
     *
     * @param id the ID to search for in the catalogue
     * @return true if a product has been found in the catalogue with the provided ID
     */
    public boolean containsId(int id) {
        return catalogue.contains(id);
    }

    /**
     * Finds a product in the catalogue with the provided ID
     *
     * @param id The id of the product to search for.
     * @return the found product or null if there isn't any product in the database with the ID
     */
    public Product findProduct(int id) {
        return catalogue.find(id);
    }

    /**
     * Deletes the product in the catalogue with the provided ID
     *
     * @param id the ID of the product to delete.
     * @return true if there was a product in the catalogue with the provided ID.
     */
    public boolean deleteProduct(int id) {
        return catalogue.delete(id);
    }

    /**
     * Provides the total cost of all the products in the catalogue.
     *
     * @return the cost of all the products in the catalogue.
     */
    public float getTotalCost() {
        List<Product> products = getOrderedCatalogue();
        float total = 0;
        for (Product product : products) {
            total += product.getCost() * product.getStock();
        }
        return total;
    }
}
