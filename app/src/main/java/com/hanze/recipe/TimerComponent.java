package com.hanze.recipe;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerComponent extends LinearLayout {
    private Button startPauseButton;
    private TextView timerView;
    private long startSeconds;
    private long seconds;
    private Timer timer;

    public static final int RUNNING = 0;
    public static final int PAUSED = 1;

    private int status = PAUSED;


    public TimerComponent(Context context, int seconds) {
        super(context);
        this.startSeconds = seconds;
        this.seconds = seconds;
        setOrientation(LinearLayout.HORIZONTAL);

        timerView = new TextView(getContext());
        timerView.setText(createTimeString(this.seconds));
        timerView.setTextSize(24);


        startPauseButton = new Button(context);
        startPauseButton.setText("Start");
        Timer timer = this.timer;
        startPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == PAUSED) {
                    start();
                } else {
                    pause();
                }
            }
        });
        addView(startPauseButton);


        Button stopButton = new Button(context);
        stopButton.setText("stop");
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        addView(stopButton);


        addView(timerView);

    }

    public void start() {
        this.status = RUNNING;
        this.startPauseButton.setText("Pause");
        this.timer = new Timer(seconds, timerView);
        this.timer.start();
        this.timer.setText("test");
    }


    public void stop() {
        this.status = PAUSED;
        this.seconds = this.startSeconds;
        this.timer.stop();
        this.startPauseButton.setText("Start");
        this.timerView.setText(createTimeString(this.seconds));
    }

    public void pause() {
        this.status = PAUSED;
        this.startPauseButton.setText("Start");

        this.seconds = this.timer.getSeconds();
        this.timer.stop();

        this.timerView.setText(createTimeString(this.seconds));
    }


    private String createTimeString(long seconds) {
        int minutes = (int) Math.floor((double) seconds / 60);
        seconds = seconds % 60;
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


    public Timer getTimer() {
        return timer;
    }
}
