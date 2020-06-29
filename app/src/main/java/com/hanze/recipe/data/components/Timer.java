package com.hanze.recipe.data.components;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;

import static androidx.core.content.ContextCompat.getSystemService;

class Timer extends android.os.CountDownTimer {
    private Context context;
    private TextView view;
    private long secondsToGo;
    private boolean stopped;

    /**
     * @param context
     * @param seconds The number of millis in the future from the call
     *                to {@link #start()} until the countdown is done and {@link #onFinish()}
     */
    public Timer(Context context, long seconds, TextView view) {
        super(seconds * 1000, 1000);
        this.context = context;
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
        int reqCode = 1;
        Intent intent = new Intent(context, MainActivity.class);
        showNotification(context, "Timer afgelopen!", intent, reqCode);
    }

    public void showNotification(Context context, String title, Intent intent, int reqCode) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent).setPriority(2).setDefaults(Notification.DEFAULT_ALL);

        notificationBuilder.setVibrate(new long[]{1000, 1000});

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
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
