package com.nmcnpm.nhom10.moneylover;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionEditActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db;
    private static final String TAG = "TransactionEditActivity";
    EditText etName, etNote, etDate, etWallet, etAmount;
    TransactionModel transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        transaction = (TransactionModel) i.getSerializableExtra("transaction");
        Log.d(TAG, "hello world " + transaction.toString());
        setInputDatas(transaction);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void init(){
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etNote = findViewById(R.id.etNote);
        etDate = findViewById(R.id.etDate);
        etAmount = findViewById(R.id.etAmount);
        etWallet = findViewById(R.id.etWallet);
    }

    public void setInputDatas(TransactionModel transaction){
        etName.setText(transaction.getName());
        etAmount.setText(transaction.getAmount().toString());
        etDate.setText(transaction.getDate());
        etWallet.setText(transaction.getWallet());
        etNote.setText(transaction.getNote());
    }

    public void onClick(View v) {
        Intent intent = null;
        intent = new Intent(this, TransactionsActivity.class);
        switch(v.getId()) {
            case R.id.btnSave:
                //delete
                deleteTransaction(transaction.getId());

                //then create another object
                Float amount = Float.valueOf(etAmount.getText().toString());
                String name = etName.getText().toString();
                String note = etNote.getText().toString();
                String date = etDate.getText().toString();
                String wallet = etWallet.getText().toString();

                createTransaction(amount, name, note, date, wallet);


                intent.putExtra("message", "Updated successfully");
                break;
            case R.id.btnDelete:
                Log.d(TAG, "Transaction id: " + transaction.getId());
                deleteTransaction(transaction.getId());

                intent.putExtra("message", "Deleted successfully");
                break;
            default:
                break;
        }

        startActivity(intent);
    }

    public void deleteTransaction(String id){
        db.collection("transactions").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void createTransaction(Float amount, String name, String note, String date, String wallet){

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("amount", amount);
        transaction.put("name", name);
        transaction.put("note", note);
        transaction.put("date", date);
        transaction.put("wallet", wallet);

        // Add a new document with a generated ID
        db.collection("transactions")
                .add(transaction)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //TODO: make toast and return to listing transaction page
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}

