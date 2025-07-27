package jdbc02;

import java.sql.Date;

public class CashbookDTO {
    private int cashbookId;
    private Date actOn;
    private int itemId;
    private String note;
    private int cashIn;
    private int cashOut;

    // Getters and Setters
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