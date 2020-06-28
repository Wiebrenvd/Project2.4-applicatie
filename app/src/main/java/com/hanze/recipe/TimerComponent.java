package com.hanze.recipe;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerComponent extends LinearLayout {

    private final int seconds;
    private Timer timer;
    private boolean running = false;


    public TimerComponent(Context context, int seconds) {
        super(context);
        this.seconds = seconds;
        setOrientation(LinearLayout.HORIZONTAL);

        TextView timerView = new TextView(getContext());
        this.timer = new Timer(seconds, timerView);



        Button button = new Button(context);
        TimerOnClickListener listener = new TimerOnClickListener(this);
        button.setOnClickListener(listener);

        addView(timerView);
        addView(button);
    }

    public void start() {
        this.running = true;
        this.timer.start();
    }


    public void stop() {
        this.running = false;
        this.timer.cancel();
    }

    public void pause() {
        this.running = false;
        this.timer.pause();
    }


    public boolean isRunning() {
        return running;
    }
}
