package com.nmcnpm.nhom10.moneylover;

public class TransactionModel {
    String date;
    Float amount;
    String name;
    String iconScr;
    String note;
    String wallet;
    Boolean isNegative = true;

    public TransactionModel(Float amount, String name, String date, String note, String wallet) {
        this.wallet = wallet;
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.note = note;
        this.isNegative = amount < 0;

    }

    public Float getAmount() {
        return amount;
    }

    public String getName() { return name; }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getWallet() { return wallet; }

    public Boolean getIsNegative() { return isNegative; }
}
