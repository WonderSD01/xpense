package com.example.xpensebudget;

import java.io.Serializable;

public class ExpenseModel implements Serializable {
    private String expenseId;
    private String description;
    private String category;
    private String type;
    private long amount;
    private long time;

    public ExpenseModel() {
    }

    public ExpenseModel(String description, String expenseId, String category, String type, long amount, long time) {
        this.description = description;
        this.expenseId = expenseId;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
