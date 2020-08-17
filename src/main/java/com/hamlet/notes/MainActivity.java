package com.hamlet.notes;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int LOW_PRIORITY = 0;
    private static int HIGH_PRIORITY = 1;
    private FloatingActionButton floatingActionButton;

    private EditText editText;
    private ImageButton notification;
    private ImageButton priority;
    private Button save;

    private Boolean priorityButtonClicked = false;
    DatabaseHelper databaseHelper;

    String dateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        databaseHelper = new DatabaseHelper(this);
        floatingActionButton = findViewById(R.id.floatingButton);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navView.getSelectedItemId() == R.id.navigation_home) {
                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(intent);
                } else {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout);

                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    editText = dialog.findViewById(R.id.todo);
                    notification = dialog.findViewById(R.id.notificationImage);
                    priority = dialog.findViewById(R.id.priorityImage);
                    save = dialog.findViewById(R.id.saveButton);

                    save.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            boolean b;
                            if (priorityButtonClicked) {
                                b = databaseHelper.addToDo(editText.getText().toString(), HIGH_PRIORITY,dateTime);
                                createNotificationChennel(editText.getText().toString(),1);
                                settingAlarm(dateTime,editText.getText().toString(),1);
                            } else {
                                b = databaseHelper.addToDo(editText.getText().toString(), LOW_PRIORITY,dateTime);
                                createNotificationChennel(editText.getText().toString(),0);
                                settingAlarm(dateTime,editText.getText().toString(),0);
                            }

                            if (b) {
                                Toast.makeText(MainActivity.this, "To-DO added", Toast.LENGTH_SHORT).show();
                                refresh(dialog);
                            } else {
                                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dateTimeDialoge();
                        }
                    });

                    priority.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPriorityPressed();
                        }
                    });

                    dialog.show();
                }
            }
        });
    }

    public void onPriorityPressed() {
        if (priorityButtonClicked) {
            priority.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_priority));
            priorityButtonClicked = false;
        } else {
            priority.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_priority_high));
            priorityButtonClicked = true;
        }
    }

    public void dateTimeDialoge() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, final int month, final int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        editText.setText(simpleDateFormat.format(calendar.getTime()));
                        dateTime = simpleDateFormat.format(calendar.getTime());

                        Toast.makeText(MainActivity.this, dateTime, Toast.LENGTH_SHORT).show();
                    }
                };
                new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(MainActivity.this, listener, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void refresh(Dialog dialog) {
        dialog.dismiss();
        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.navigation_dashboard);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChennel(String description, int pri){
        CharSequence name = "TO-DO Remainder";
        int importance;
        NotificationChannel channel;

        if(pri == 1){
            importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel("ToDoChennel",name,importance);
            channel.setDescription(description);
        } else {
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("ToDoChennel",name,importance);
            channel.setDescription(description);
        }

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    public void settingAlarm(String dateTime,String description,int priority){

        Intent intent = new Intent(this,Receiver.class);
        intent.putExtra("DESCRIPTION",description);
        intent.putExtra("PRIORITY",priority);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Toast.makeText(this, String.valueOf(cal.getTime()), Toast.LENGTH_SHORT).show();
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
    }

}