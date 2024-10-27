package com.example.xpensebudget;

import java.io.Serializable;

public class ExpenseModel implements Serializable {
    private String expenseId;
    private String description;
    private String category;
    private String type;
    private long amount;
    private long time;
    private String imageUri; // New field for image URI

    // Default constructor
    public ExpenseModel(String expenseId, String string, String s, String type, long amount, long timeInMillis) {}

    // Parameterized constructor
    public ExpenseModel(String expenseId, String description, String category, String type, long amount, long time, String imageUri) {
        this.expenseId = expenseId;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.imageUri = imageUri; // Initialize the image URI
    }

    // Getters and setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    // Getter for imageUri
    public String getImageUri() {
        return imageUri;
    }

    // Setter for imageUri
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
