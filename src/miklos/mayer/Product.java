package miklos.mayer;

public class Product implements Comparable<Product> {

    private final int id;

    private String name;

    private float cost;

    private int stock;

    public Product(int id, String name, float cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int decreaseStock(int quantity) {
        this.stock -= quantity;
        return this.stock;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    @Override
    public int compareTo(Product otherProduct) {
        return Integer.compare(this.id, otherProduct.getId());
    }
}
