package miklos.mayer;

public class Product {

    private final int id;

    private String name;

    private float cost;

    private int stock;

    public Product(int id, String name, float cost, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.stock = quantity;
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

    public int increaseStock(int quantity) {
        this.stock += quantity;
        return this.stock;
    }

    public int decreaseStock(int quantity) {
        this.stock -= quantity;
        return this.stock;
    }

    public int getStock() {
        return stock;
    }

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
