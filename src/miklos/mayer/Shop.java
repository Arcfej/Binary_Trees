package miklos.mayer;

import java.util.List;

public class Shop {

    private BinaryTree<Integer, Product> catalogue;

    public Shop() {
        this.catalogue = new BinaryTree<>();
    }

    public List<Product> getOrderedCatalogue() {
        return catalogue.traverseInOrder();
    }

    public List<Product> getCatalogueForSave() {
        return catalogue.traversePreOrder();
    }

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

    public boolean containsId(int id) {
        return catalogue.contains(id);
    }

    public Product findProduct(int id) {
        return catalogue.find(id);
    }

    public boolean deleteProduct(int id) {
        return catalogue.delete(id);
    }

    public float getTotalCost() {
        List<Product> products = getOrderedCatalogue();
        float total = 0;
        for (Product product : products) {
            total += product.getCost() * product.getStock();
        }
        return total;
    }
}
