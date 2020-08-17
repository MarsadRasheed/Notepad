package com.hamlet.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hamlet.notes.ui.home.HomeFragment;

public class DetailActivity extends AppCompatActivity {

    private EditText editText;

    private RelativeLayout delete;
    private RelativeLayout favorite;
    private RelativeLayout share;
    private RelativeLayout save;

    private DatabaseHelper databaseHelper;

    private int id = -1;
    private String note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();

        note = getIntent().getStringExtra("NOTE");
        id = getIntent().getIntExtra("ID",-1);

        editText.setText(note);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteData(note,id);
                Toast.makeText(DetailActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("Data",note);
                intent.setType("text/plain");
                intent = Intent.createChooser(intent,"Share Via");
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNote = editText.getText().toString();
                databaseHelper.updateData(newNote,id,note);
                Toast.makeText(DetailActivity.this, "Noted saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initViews(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        delete = findViewById(R.id.deleteLayout);
        favorite = findViewById(R.id.favoriteLayout);
        share = findViewById(R.id.shareLayout);
        editText = findViewById(R.id.detailEditText);
        save = findViewById(R.id.saveLayout);
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseHelper.updateData(editText.getText().toString(),id,note);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(intent);
    }

}