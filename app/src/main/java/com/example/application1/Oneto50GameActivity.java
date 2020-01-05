package com.example.application1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Oneto50GameActivity extends FragmentActivity {
    View include;
    TextView next_num;
    TextView cur_time;
    RelativeLayout[] button = new RelativeLayout[25];
    TextView[] button_txt = new TextView[25];

    private int nextNumber;
    private List<Integer> Remains; //uniform index
    private List<Boolean> found; //uniform index
    private Handler handler;
    private TimerTask timerTask;
    private Timer timer;
    private long taken_time;
    private double real_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextNumber = 1;
        Remains = new ArrayList<Integer>();
        for (int a=1; a<26; a++) {
            Remains.add(a);
        }
        found = new ArrayList<Boolean>();
        for (int b=1; b<26; b++) {
            found.add(false);
        }
        long seed = System.nanoTime();
        Collections.shuffle(Remains, new Random(seed));

        setContentView(R.layout.one50gameactivity);
        all_view();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (nextNumber < 26) {
                    updateTime();
                }
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                taken_time += 100;
                real_time = taken_time/1000.;
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 100);
        include.setVisibility(View.VISIBLE);
        game();
        all_found();
    }
    private void updateTime() {
        cur_time.setText(String.format("소요 시간 : %.1f", real_time));
    }

    private void game() {
        next_num.setText(String.format("%d 을(를) 눌러주세요", nextNumber));
        for (int i=0; i<Remains.size(); i++) {
            button_txt[i].setText(Integer.toString(Remains.get(i)));
        }
    }

    private void all_view() {
        include = findViewById(R.id.include_150game_view);
        next_num = (TextView) findViewById(R.id.nextnum);
        cur_time = (TextView) findViewById(R.id.curtime_150);
        button[0] = (RelativeLayout)findViewById(R.id.button_1);
        button[1] = (RelativeLayout)findViewById(R.id.button_2);
        button[2] = (RelativeLayout)findViewById(R.id.button_3);
        button[3] = (RelativeLayout)findViewById(R.id.button_4);
        button[4] = (RelativeLayout)findViewById(R.id.button_5);
        button[5] = (RelativeLayout)findViewById(R.id.button_6);
        button[6] = (RelativeLayout)findViewById(R.id.button_7);
        button[7] = (RelativeLayout)findViewById(R.id.button_8);
        button[8] = (RelativeLayout)findViewById(R.id.button_9);
        button[9] = (RelativeLayout)findViewById(R.id.button_10);
        button[10] = (RelativeLayout)findViewById(R.id.button_11);
        button[11] = (RelativeLayout)findViewById(R.id.button_12);
        button[12] = (RelativeLayout)findViewById(R.id.button_13);
        button[13] = (RelativeLayout)findViewById(R.id.button_14);
        button[14] = (RelativeLayout)findViewById(R.id.button_15);
        button[15] = (RelativeLayout)findViewById(R.id.button_16);
        button[16] = (RelativeLayout)findViewById(R.id.button_17);
        button[17] = (RelativeLayout)findViewById(R.id.button_18);
        button[18] = (RelativeLayout)findViewById(R.id.button_19);
        button[19] = (RelativeLayout)findViewById(R.id.button_20);
        button[20] = (RelativeLayout)findViewById(R.id.button_21);
        button[21] = (RelativeLayout)findViewById(R.id.button_22);
        button[22] = (RelativeLayout)findViewById(R.id.button_23);
        button[23] = (RelativeLayout)findViewById(R.id.button_24);
        button[24] = (RelativeLayout)findViewById(R.id.button_25);

        button_txt[0] = (TextView)findViewById(R.id.button_text_1);
        button_txt[1] = (TextView)findViewById(R.id.button_text_2);
        button_txt[2] = (TextView)findViewById(R.id.button_text_3);
        button_txt[3] = (TextView)findViewById(R.id.button_text_4);
        button_txt[4] = (TextView)findViewById(R.id.button_text_5);
        button_txt[5] = (TextView)findViewById(R.id.button_text_6);
        button_txt[6] = (TextView)findViewById(R.id.button_text_7);
        button_txt[7] = (TextView)findViewById(R.id.button_text_8);
        button_txt[8] = (TextView)findViewById(R.id.button_text_9);
        button_txt[9] = (TextView)findViewById(R.id.button_text_10);
        button_txt[10] = (TextView)findViewById(R.id.button_text_11);
        button_txt[11] = (TextView)findViewById(R.id.button_text_12);
        button_txt[12] = (TextView)findViewById(R.id.button_text_13);
        button_txt[13] = (TextView)findViewById(R.id.button_text_14);
        button_txt[14] = (TextView)findViewById(R.id.button_text_15);
        button_txt[15] = (TextView)findViewById(R.id.button_text_16);
        button_txt[16] = (TextView)findViewById(R.id.button_text_17);
        button_txt[17] = (TextView)findViewById(R.id.button_text_18);
        button_txt[18] = (TextView)findViewById(R.id.button_text_19);
        button_txt[19] = (TextView)findViewById(R.id.button_text_20);
        button_txt[20] = (TextView)findViewById(R.id.button_text_21);
        button_txt[21] = (TextView)findViewById(R.id.button_text_22);
        button_txt[22] = (TextView)findViewById(R.id.button_text_23);
        button_txt[23] = (TextView)findViewById(R.id.button_text_24);
        button_txt[24] = (TextView)findViewById(R.id.button_text_25);
    }

    private void all_found() {
        for (int i=0; i<25; i++) {
            final int j=i;
            button[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nextNumber != 26) {
                        int number = j;
                        if (nextNumber == Remains.get(number)) {
                            nextNumber++;
                            button_txt[number].setVisibility(View.GONE);
                        }
                        if (nextNumber != 26) {
                            next_num.setText(String.format("%d 을(를) 눌러주세요", nextNumber));
                        }
                    }
                }
            });
        }
    }
}
