package com.hamlet.notes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoDetailActivity extends AppCompatActivity {
    private static int LOW_PRIORITY = 0;
    private static int HIGH_PRIORITY = 1;

    private RelativeLayout remainderLayout;

    private RadioButton radioButton;
    private EditText toDoText;
    private EditText remarksEditText;
    private TextView dateTimeTextView;

    private RelativeLayout priorityLayout;
    private ImageView priorityImage;
    private Switch prioritySwitch;

    private RelativeLayout remarksLayout;

    private RelativeLayout shareLayout;
    private RelativeLayout deleteLayout;
    private RelativeLayout saveLayout;

    private TextView priorityText;

    private DatabaseHelper databaseHelper;

    private int toDoId;
    private String todo;
    private String description;
    private int priority;

    private boolean isPriority = false;
    String dateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);
        initViews();

        final int id = getIntent().getIntExtra("ItemId", 0);
        final String toDo = getIntent().getStringExtra("ToDo");
        final String desc = getIntent().getStringExtra("DESCRIPTION");
        final int pri = getIntent().getIntExtra("priority",0);
        final String dateNTime = getIntent().getStringExtra("DATENTIME");
        toDoId = id;
        todo = toDo;
        description = desc;
        priority = pri;
        dateTime = dateNTime;

        setDateTimeTextView(dateTime);
        checkPriority(priority);

//        Toast.makeText(this, String.valueOf(priority), Toast.LENGTH_SHORT).show();

        toDoText.setText(toDo);
        remarksEditText.setText(description);

        prioritySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setImportant();
                } else {
                    setUnImportant();
                }
            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("TODO", toDo);
                intent.setType("text/plain");
                intent = Intent.createChooser(intent, "Share Via");
                startActivity(intent);
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteToDo(toDo, id);
                Toast.makeText(ToDoDetailActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(ToDoDetailActivity.this, R.id.nav_host_fragment)
                        .navigate(R.id.navigation_dashboard);
                refreshLayout();
            }
        });

        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPriority){
                    databaseHelper.updateToDo(toDoText.getText().toString(), id, toDo, remarksEditText.getText().toString(),HIGH_PRIORITY,dateTimeTextView.getText().toString());
                } else {
                    databaseHelper.updateToDo(toDoText.getText().toString(), id, toDo, remarksEditText.getText().toString(),LOW_PRIORITY,dateTimeTextView.getText().toString());
                }
                Toast.makeText(ToDoDetailActivity.this, "Note Updated!", Toast.LENGTH_SHORT).show();
                refreshLayout();
            }
        });

        priorityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPriority) {
                    setImportant();
                } else {
                    setUnImportant();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isPriority){
            databaseHelper.updateToDo(toDoText.getText().toString(), toDoId, todo, remarksEditText.getText().toString(),HIGH_PRIORITY,dateTimeTextView.getText().toString());
        } else {
            databaseHelper.updateToDo(toDoText.getText().toString(), toDoId, todo, remarksEditText.getText().toString(),LOW_PRIORITY,dateTimeTextView.getText().toString());
        }
        refreshLayout();
        Toast.makeText(this, "To-Do updated !", Toast.LENGTH_SHORT).show();
    }

    public void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        remainderLayout = findViewById(R.id.remainderRL);
        radioButton = findViewById(R.id.radioButton);
        toDoText = findViewById(R.id.toDoEdit);
        remarksEditText = findViewById(R.id.remarksTextView);

        priorityLayout = findViewById(R.id.importantRL);
        priorityImage = findViewById(R.id.priImage);
        prioritySwitch = findViewById(R.id.importantSwitch);

        remainderLayout = findViewById(R.id.remarksRL);

        shareLayout = findViewById(R.id.shareLayoutToDo);
        deleteLayout = findViewById(R.id.deleteLayoutToDo);
        saveLayout = findViewById(R.id.saveLayoutToDo);
        priorityText = findViewById(R.id.priText);
        dateTimeTextView = findViewById(R.id.notifiyText);
        databaseHelper = new DatabaseHelper(this);
    }

    public void refreshLayout() {
        Navigation.findNavController(ToDoDetailActivity.this, R.id.nav_host_fragment).navigate(R.id.navigation_dashboard);
    }

    public void checkPriority(int priority){
        if(priority == 1){
            setImportant();
        } else {
            setUnImportant();
        }
    }

    public void setImportant(){
        prioritySwitch.setChecked(true);
        isPriority = true;
        priorityImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_priority_high));
        priorityText.setText("Mark as un-important");
    }

    public void setUnImportant(){
        isPriority = false;
        prioritySwitch.setChecked(false);
        priorityImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_priority));
        priorityText.setText("Mark as important");
    }

    public void setDateTimeTextView(String date){
        if(date != null){
            dateTimeTextView.setText(date);
        } else {
            dateTimeTextView.setText("Add remainder");
        }
    }

}
