package com.nmcnpm.nhom10.moneylover;

public class TransactionModel {
    String name;
    String date;
    String amount;
    String iconScr;
    Boolean isNegative = true;

    public TransactionModel(String name, String date, String amount, String iconScr) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.iconScr = iconScr;

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getIcon() {
        return iconScr;
    }

}
