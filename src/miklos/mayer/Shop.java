package miklos.mayer;

import java.util.List;

public class Shop {

    private BinaryTree<Product> catalogue;

    public Shop() {
        this.catalogue = new BinaryTree<>();
    }

    public List<Product> getCatalogue() {
        return catalogue.traverseInOrder();
    }

    public void addNewProduct(int id, String name, float price, int quantity) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be bigger than 0!");
        } else if (catalogue.contains(new Product(id))) {
            throw new IllegalArgumentException("Id must be unique!");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Product name mustn't be empty!");
        } else if (price <= 0) {
            throw new IllegalArgumentException("Price should be bigger than 0!");
        } else if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can't be a negative number!");
        }
        try {
            catalogue.add(new Product(id, name, price, quantity));
        } catch (DuplicateItemException e) {
            throw new IllegalArgumentException("Product is already in the catalogue!");
        }
    }

    public boolean containsId(int id) {
        return catalogue.contains(new Product(id));
    }
}
