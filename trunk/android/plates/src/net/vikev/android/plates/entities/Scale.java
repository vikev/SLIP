package net.vikev.android.plates.entities;

public class Scale {
    private String id;
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
        return quantity<20;
    }
}
