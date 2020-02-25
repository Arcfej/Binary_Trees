package miklos.mayer;

public class ShopItem {

    private final String id;

    private String name;

    private float cost;

    public ShopItem(String id, String name, float cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public String getId() {
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
}
