package com.example.dummy_name;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.Settings;
import android.view.View;
import com.example.dummy_name.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import java.util.Calendar;
import kotlin.Pair;


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


    private void newDNDRule(Pair<Integer, Integer> startTime, Pair<Integer, Integer> endTime) {
        context = getApplicationContext();
         notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Schedule DND from 10pm to 7am daily
        if(!notificationManager.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);

        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

// Schedule starting at 10pm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

// Schedule ending at 7am
        calendar.set(Calendar.HOUR_OF_DAY, 16);
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