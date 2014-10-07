package net.vikev.android.plates.entities;

public class Item {
    private String id;
    private String name;
    private String image_location;
    private int quantity;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImage_location() {
        return image_location;
    }
    
    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
