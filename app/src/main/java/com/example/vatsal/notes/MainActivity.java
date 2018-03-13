package com.example.vatsal.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list;
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote) {
            Intent intent = new Intent(this, AddNote.class);
            intent.putExtra("isNew", true);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.myListView);
        final SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        try {
            list = (ArrayList<String>) ObjectSerializer
                    .deserialize(
                            sharedPreferences
                                    .getString(
                                            "data",
                                            ObjectSerializer.serialize(new ArrayList<String>())
                                    )
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                list
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    Intent intent = new Intent(getApplicationContext(), AddNote.class);
                    intent.putExtra("isNew", false);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
        );
        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, final int position, long id) -> {
            new AlertDialog.Builder(getApplicationContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete Note")
                    .setMessage("Are You Sure you want to delete the note")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            list = (ArrayList) ObjectSerializer
                                    .deserialize(
                                            sharedPreferences
                                                    .getString(
                                                            "data",
                                                            ObjectSerializer
                                                                    .serialize(
                                                                            new ArrayList<String>()
                                                                    )
                                                    )
                                    );
                            list.remove(position);
                            sharedPreferences
                                    .edit()
                                    .putString
                                            (
                                                    "data",
                                                    ObjectSerializer.serialize(list)
                                            )
                                    .apply();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arrayAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    })
                    .show();
            return true;

        });
    }
}
