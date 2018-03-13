package com.example.vatsal.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class AddNote extends AppCompatActivity {
    EditText editText;
    boolean isNew;
    SharedPreferences sharedPreferences;
    ArrayList<String> arrayList;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editText = (EditText) findViewById(R.id.editText);
        isNew = getIntent().getBooleanExtra("isNew", true);
        arrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        try {
            arrayList = (ArrayList<String>) ObjectSerializer.deserialize(
                    sharedPreferences.getString(
                            "data",
                            ObjectSerializer.serialize(
                                    new ArrayList<String>()
                            )
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!isNew) {
            position = getIntent().getIntExtra("position", 0);
            String text = arrayList.get(position);
            editText.setText(text);
        }

    }

    @Override
    public void onBackPressed() {
        String text = editText.getText().toString();
        if (!isNew)
            arrayList.set(position, text);
        else
            arrayList.add(text);
        try {
            sharedPreferences.edit().putString("data", ObjectSerializer.serialize(arrayList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
