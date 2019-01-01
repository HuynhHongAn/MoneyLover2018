package com.nmcnpm.nhom10.moneylover;

public class TransactionModel {
    String name;
    String date;
    Float amount;
    String iconScr;
    String note;
    Integer wallet;
    Boolean isNegative = true;

    public TransactionModel(String name, String date, Float amount, String iconScr, String note) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.iconScr = iconScr;
        this.note = note;

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Float getAmount() {
        return amount;
    }

    public String getIcon() {
        return iconScr;
    }

    public String getNote() { return note; }
}
