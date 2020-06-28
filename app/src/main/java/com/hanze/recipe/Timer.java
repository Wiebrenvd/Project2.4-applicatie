package com.hanze.recipe;

import android.app.Notification;
import android.app.NotificationManager;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.content.ContextCompat.getSystemService;

class Timer extends android.os.CountDownTimer {
    private TextView view;
    private long secondsToGo;
    private boolean stopped;

    /**
     * @param seconds The number of millis in the future from the call
     *                to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                is called.
     */
    public Timer(long seconds, TextView view) {
        super(seconds * 1000, 1000);
        this.view = view;
    }

    @Override
    public void onTick(long ms) {
        if (stopped) {
            cancel();
        } else {
            this.secondsToGo = ms / 1000;
            this.view.setText(createTimeString(ms));
        }
    }

    private String createTimeString(long seconds) {
        seconds = seconds / 1000;
        int minutes = ((int) Math.floor((double) seconds / 60));
        seconds = (seconds % 60);

        return createUnitString(minutes) + ":" + createUnitString((int) seconds);
    }

    private String createUnitString(int nmbr) {
        if (nmbr == 0) {
            return "00";
        } else if (nmbr < 10) {
            return "0" + nmbr;
        } else {
            return "" + nmbr;
        }
    }

    @Override
    public void onFinish() {
        Notification notification = new NotificationCompat.Builder(this.view.getContext(), "Recipe")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Timer afgelopen!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = getSystemService(this.view.getContext(), NotificationManager.class);
        notificationManager.notify(0, notification);
    }


    public long getSeconds() {
        return this.secondsToGo;
    }

    public void stop() {
        this.stopped = true;
    }

    public void setText(String string) {
        this.view.setText(string);
    }
}
