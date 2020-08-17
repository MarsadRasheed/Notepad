package com.hamlet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private EditText noteText;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setContentView(R.layout.activity_note);
        databaseHelper = new DatabaseHelper(this);
        initViews();

    }

    public void initViews(){
        noteText = findViewById(R.id.noteEditText);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_options,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_note){
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    public void saveNote() {
        String data = noteText.getText().toString();
        if (!data.isEmpty()) {
            boolean result = databaseHelper.addNote(data);
            if (result) {
                showToast("Note inserted.");
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                showToast("Something went wrong.");
            }
        }
    }
}
