package miklos.mayer;

/**
 * Represents a product in the shop
 */
public class Product {

    /**
     * The ID of the product.
     */
    private final int id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The cost of the product.
     */
    private float cost;

    /**
     * The stock level of the product.
     */
    private int stock;

    /**
     * Base constructor.
     *
     * @param id The ID of the product
     * @param name The name of the product
     * @param cost The cost of the product
     * @param quantity The initial stock level of the product
     */
    public Product(int id, String name, float cost, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.stock = quantity;
    }

    /**
     * Getter of the product's ID.
     *
     * @return the ID of the product.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of the name of the product.
     *
     * @return the name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of the name of the product.
     *
     * @param name the new name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of the cost of the product.
     *
     * @return the cost of the product.
     */
    public float getCost() {
        return cost;
    }

    /**
     * Setter of the cost of the product.
     *
     * @param cost the new cost of the product.
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Increase the stock level of the product.
     *
     * @param quantity The quantity to increase the stock level with.
     * @return the stock level after the increase
     */
    public int increaseStock(int quantity) {
        this.stock += quantity;
        return this.stock;
    }

    /**
     * Decrease the stock level of the product
     *
     * @param quantity The quantity to increase the stock level with
     * @return the stock level after the decrease
     */
    public int decreaseStock(int quantity) {
        this.stock -= quantity;
        return this.stock;
    }

    /**
     * Getter of the stock level of the product
     *
     * @return the stock level of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * Two Products are equals if all of their fields are equals.
     *
     * @param obj the other object to compare to
     * @return true if the two objects are equals
     */
    @Override
    public boolean equals(Object obj) {
        Product other;
        try {
            other = (Product) obj;
        } catch (ClassCastException e) {
            return false;
        }
        if (id != other.getId() || !name.equals(other.getName()) || cost != other.getCost() || stock != other.getStock()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
