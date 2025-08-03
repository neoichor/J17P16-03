package jdbc02_2;

public class ItemDTO {
    private int itemId;
    private String itemName;

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "ItemDTO{"
                + "itemId=" + itemId +
                ", itemName='" + itemName + "'" + // Corrected: removed unnecessary escape for single quote
                '}';
    }
}
