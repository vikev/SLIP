package net.vikev.android.plates.entities;

public class Scale {
    private String id;
    private String name;
    private Integer quantity;
    private Item item;

    public String getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isRunningEmpty() {
        return quantity < 70;
    }
}
