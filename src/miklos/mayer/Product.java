package miklos.mayer;

import java.util.function.Predicate;

public class Product implements Comparable<Product> {

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
    public int compareTo(Product otherProduct) {
        return Integer.compare(this.id, otherProduct.getId());
    }

    @Override
    public boolean equals(Object obj) {
        try {
            obj = (Product) obj;
        } catch (ClassCastException e) {
            return false;
        }
        if (id != ((Product) obj).getId() || !name.equals(((Product) obj).getName()) || cost != ((Product) obj).getCost() || stock != ((Product) obj).getStock()) {
            return false;
        } else {
            return true;
        }
    }
}
