package net.vikev.android.plates.entities;

public class Item {
    private String id;
    private String name;
    private Integer image_index;
    private Integer quantity;
    private String barcode;
    
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
    
    public int getImage_location() {
        return image_index;
    }
    
    public void setImage_location(int image_index) {
        this.image_index = image_index;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
