package com.nmcnpm.nhom10.moneylover;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    String id;
    String date;
    Double amount;
    String name;
    String iconScr;
    String note;
    String wallet;

    public TransactionModel(String id, Double amount, String name, String date, String note, String wallet) {
        this.id = id;
        this.wallet = wallet;
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.note = note;

    }

    public Double getAmount() {
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


    public String getId() {return id;}
}
