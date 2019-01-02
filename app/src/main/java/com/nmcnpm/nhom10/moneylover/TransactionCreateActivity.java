package com.nmcnpm.nhom10.moneylover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TransactionCreateActivity extends AppCompatActivity {

    private static final String TAG = "TransactionCreateActivity";
    EditText etName, etNote, etDate, etWallet, etAmount;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);
        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Create a new user with a first and last name


        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float amount = Float.valueOf(etAmount.getText().toString());
                String name = etName.getText().toString();
                String note = etNote.getText().toString();
                String date = etDate.getText().toString();
                String wallet = etWallet.getText().toString();

                createTransaction(amount, name, note, date, wallet);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
                        Intent intent = new Intent(TransactionCreateActivity.this, TransactionsActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void init(){
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etNote = findViewById(R.id.etNote);
        etDate = findViewById(R.id.etDate);
        etAmount = findViewById(R.id.etAmount);
        etWallet = findViewById(R.id.etWallet);
    }
}
