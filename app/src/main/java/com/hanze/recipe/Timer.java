package com.hanze.recipe;

import android.widget.TextView;

class Timer extends android.os.CountDownTimer {
    private TextView view;
    private boolean pause;

    /**
     * @param seconds The number of millis in the future from the call
     *                to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                is called.
     */
    public Timer(long seconds, TextView view) {
        super(seconds*1000, 1000);
        System.out.println(seconds);
        seconds *= 1000;
        this.view = view;
    }

    @Override
    public void onTick(long seconds) {
        System.out.println("test");
        if (!pause) {
            this.view.setText(createTimeString((int) seconds));
        }

    }

    private String createTimeString(int seconds) {
        int minutes = ((int) Math.floor((double) seconds / 60)) / 1000;
        seconds = (seconds % 60) / 1000;

        return minutes + ":" + seconds;
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

    }

    public void pause() {
        this.pause = true;
    }
}
