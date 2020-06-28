package com.hanze.recipe;

import android.view.View;

class TimerOnClickListener implements View.OnClickListener {

    private TimerComponent timer;

    public TimerOnClickListener(TimerComponent timer) {
        this.timer = timer;

    }

    @Override
    public void onClick(View v) {
        this.timer.start();


    }
}
