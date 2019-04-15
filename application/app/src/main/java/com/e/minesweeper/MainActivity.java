package com.e.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    public static Boolean flagEnabled = false;
    private static CountDownTimer countDownTimer = null;
    public static int flagsCount = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create app timer
        createCountdownTimer();
        final MainActivity activity = this;
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainLayout);

        //set reset action for resetbutton
        Button buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GameEngine.getInstance().createGrid(activity);

                //reset timer and reset flags
                countDownTimer.cancel();
                createCountdownTimer();
                resetFlags();

                Snackbar restartSnackbar = Snackbar.make(linearLayout, "Game restarted", Snackbar.LENGTH_LONG);
                restartSnackbar.show();
            }
        });


        //set flag for flag if button is clicked or return to normal mode
        final Button buttonFlag = (Button) findViewById(R.id.buttonFlag);
        buttonFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = "";

                //return to normal mode
                if (flagEnabled) {
                    flagEnabled = false;
                    text = "Flag disabled";
                    buttonFlag.setText("SET FLAG");
                }
                //set mode to flag mode
                else {
                    flagEnabled = true;
                    text = "Flag enabled";
                    buttonFlag.setText("DISABLE FLAG");
                }

                Snackbar flagSnackbar = Snackbar.make(linearLayout, text, Snackbar.LENGTH_LONG);
                flagSnackbar.show();
            }
        });

        GameEngine.getInstance().createGrid(this);
    }

    public static void setFlagsCount(int count) {
        flagsCount = count;
    }

    public static int getFlagsCount() {
        return flagsCount;
    }

    private void resetFlags() {
        flagsCount = 10;
        TextView textViewFlags = (TextView) findViewById(R.id.textViewFlags);
        textViewFlags.setText("Flags remaining: 10");
    }

    /**
     * create timer for app
     */
    private void createCountdownTimer() {
        countDownTimer = new CountDownTimer(999999999, 1000)
        {
            int time = 0;
            public void onTick(long millisUntilFinished)
            {
                TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
                textViewTime.setText("Time: " + time);
                time++;
                TextView textViewFlags = (TextView) findViewById(R.id.textViewFlags);
                textViewFlags.setText("Flags remaining: " + flagsCount);
            }

            public void onFinish()
            {
                // finish off when we're all dead !
            }
        }.start();
    }
}
