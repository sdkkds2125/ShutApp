package com.example.dummy_name;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.dummy_name.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

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

        binding.fab.setOnClickListener(view -> FABClickAction(view));
    }

    private void FABClickAction(View view) {
        Snackbar.make(view, "Added a new DND rule", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show();
        startHourMinute = new Pair<>(startTime.getHour(), startTime.getMinute());
        endHourMinute = new Pair<>(endTime.getHour(), endTime.getMinute());
        newDNDRule(startHourMinute,endHourMinute);
    }

    private void newDNDRule(Pair<Integer, Integer> startTime, Pair<Integer, Integer> endTime) {

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