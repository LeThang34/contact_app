package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    EditText edtName, edtNumber;
    Button btnInsert, btnDelete, btnUpdate, btnView ;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);

        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnView = findViewById(R.id.btnView);
        database=openOrCreateDatabase("contact.db",MODE_PRIVATE, null);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                insert();

                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();
                ContentValues myValues = new ContentValues();
                myValues.put("number", number);
                myValues.put("name", name);

                String msg = "";
                if (database.insert("dbcontact", null , myValues) ==  -1){
                    msg = "Fail to Insert Record!";
                } else {
                    msg = "Insert record Sucessfully";
                }
                Toast.makeText(AddContact.this,msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                int n = database.delete("dbcontact", "name = ?", new String[]{name});
                String msg = "";
                if (n == 0){
                    msg = "No record to Delete";
                }else {
                    msg = n + " record to Delete";
                }
                Toast.makeText(AddContact.this, msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();
                ContentValues myValues = new ContentValues();
                myValues.put("number", number);
                myValues.put("name", name);
                int n = database.update("dbcontact",myValues,"name = ?", new String[]{name});
                String msg = "";
                if (n == 0){
                    msg = "No record to Update";
                } else {
                    msg = n + " record to Update";
                }
                Toast.makeText(AddContact.this, msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}