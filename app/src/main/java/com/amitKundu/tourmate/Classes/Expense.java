package com.amitKundu.tourmate.Classes;

public class Expense {
    private String expenseId;
    private String expenseType;
    private String expenseAmount;
    private String expenseDate;

    public Expense() {

    }

    public Expense(String expenseId, String expenseType, String expenseAmount, String expenseDate) {
        this.expenseId = expenseId;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    public Expense(String expenseType, String expenseAmount, String expenseDate) {
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }
}


