package jdbc05;

/**
 * 【品目DTO（itemテーブル1レコード分）】
 * itemテーブル1レコード分の情報を保持するデータクラス。
 */
public class ItemDTO {
	private int itemId;
	private String itemName;

	public ItemDTO() {
	}

	public ItemDTO(int itemId, String itemName) {
		this.itemId = itemId;
		this.itemName = itemName;
	}

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
		return "ItemDTO [id=" + itemId + ", name=" + itemName + "]";
	}
}
