package jdbc05;

import java.sql.Date;

/**
 * 【家計簿DTO（cashbookテーブル1レコード分）】
 * N+1問題解決のため、item_nameフィールドを直接保持します。
 */
public class CashbookDTO {

    private int cashbookId;
    private Date actOn;
    private int itemId;
    private String itemName; // JOINで取得した費目名を格納
    private String note;
    private int cashIn;
    private int cashOut;

    // --- Getters and Setters ---

    public int getCashbookId() {
        return cashbookId;
    }

    public void setCashbookId(int cashbookId) {
        this.cashbookId = cashbookId;
    }

    public Date getActOn() {
        return actOn;
    }

    public void setActOn(Date actOn) {
        this.actOn = actOn;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCashIn() {
        return cashIn;
    }

    public void setCashIn(int cashIn) {
        this.cashIn = cashIn;
    }

    public int getCashOut() {
        return cashOut;
    }

    public void setCashOut(int cashOut) {
        this.cashOut = cashOut;
    }
}
