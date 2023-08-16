package com.example.dummy_name;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;


import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.view.View;

import com.example.dummy_name.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

import kotlin.Pair;
import androidx.core.app.PermissionManager;

public class MainActivity extends AppCompatActivity {

    /**
     * some code I found that may help but can't test it yet because not set up
     */
//    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//mNotificationManager.setNotificationPolicy(
//        new NotificationManager.Policy(NotificationManager.Policy.PRIORITY_CATEGORY_CALLS | NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES,
//    NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS,
//    NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS));
//
//mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
    private ActivityMainBinding binding;
    private TimePicker startTime;
    private TimePicker endTime;

    private static final String START_TIME = "start_time";

    private static final String END_TIME = "end_time";

    private Pair<Integer, Integer> startHourMinute,endHourMinute;

    NotificationManager notificationManager;

    Context context;

    String[] permissions = {Manifest.permission.SCHEDULE_EXACT_ALARM, Manifest.permission.SET_ALARM,Manifest.permission.ACCESS_NOTIFICATION_POLICY};

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(START_TIME, new Pair<>(startTime.getHour(), startTime.getMinute()));
        outState.putSerializable(END_TIME, new Pair<>(endTime.getHour(), endTime.getMinute()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        startHourMinute = (Pair<Integer, Integer>) savedInstanceState.getSerializable(START_TIME);
        endHourMinute = (Pair<Integer, Integer>) savedInstanceState.getSerializable(END_TIME);
        assert startHourMinute != null;
        assert endHourMinute != null;
        startTime.setHour(startHourMinute.getFirst());
        startTime.setMinute(startHourMinute.getSecond());
        endTime.setHour(endHourMinute.getFirst());
        endTime.setMinute(endHourMinute.getSecond());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.fab.setOnClickListener(view -> FABClickAction(view));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void FABClickAction(View view) {
        //checkPermissions(permissions);
        Snackbar.make(view, "Added a new DND rule", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show();
        startHourMinute = new Pair<>(startTime.getHour(), startTime.getMinute());
        endHourMinute = new Pair<>(endTime.getHour(), endTime.getMinute());
        newDNDRule(startHourMinute,endHourMinute);
    }
    // Function to check and request permission.
    public void checkPermissions(String[] permissions) {
        boolean temp = true;
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                    temp = false;
            }

            }
                 if (temp) {
                     ActivityCompat.requestPermissions(MainActivity.this, permissions, 5);
                 }
        }



    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }


    private void newDNDRule(Pair<Integer, Integer> startTime, Pair<Integer, Integer> endTime) {
        context = getApplicationContext();
         notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Schedule DND from 10pm to 7am daily
        if(!notificationManager.isNotificationPolicyAccessGranted()){
            PermissionManager permissionManager = context.getSystemService(PermissionManager.class);
            
            ActivityCompat.requestPermissiona(Manifest.permission.ACCESS_NOTIFICATION_POLICY, null);
        }
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);

        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

// Schedule starting at 10pm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

// Schedule ending at 7am
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}