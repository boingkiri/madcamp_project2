package com.example.application1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGameActivity extends FragmentActivity {
    View include;
    TextView remaining;
    TextView cur_time;
    RelativeLayout[] button = new RelativeLayout[16];
    ImageView[] button_img = new ImageView[16];

    private List<Integer> Cards;
    private List<Boolean> found;
    private boolean itemSelected;
    private int firstSelectedPos;
    private int secondSelectedPos;
    private boolean everClick;
    private int remaining_cards;
    private long taken_time;
    private double real_time;
    private TimerTask timerTask;
    private Timer timer;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remaining_cards = 16;
        everClick = false;
        itemSelected = false;
        firstSelectedPos = -1;
        secondSelectedPos = -1;
        Cards = new ArrayList<Integer>();
        long seed = System.nanoTime();
        for (int a=0; a<2; a++) {
            Cards.add(R.drawable.f12);
            Cards.add(R.drawable.f7);
            Cards.add(R.drawable.f9);
            Cards.add(R.drawable.f15);
            Cards.add(R.drawable.f19);
            Cards.add(R.drawable.f17);
            Cards.add(R.drawable.f11);
            Cards.add(R.drawable.f2);
        }
        Collections.shuffle(Cards, new Random(seed));
        found = new ArrayList<Boolean>();
        for (int b=0; b<Cards.size(); b++) {
            found.add(false);
        }

        setContentView(R.layout.memgameactivity);
        all_view();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (!foundAll(found))
                    updateTime();
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
        remaining.setText("남은 개수 : " + Integer.toString(remaining_cards));
    }

    private void all_view() {
        include = findViewById(R.id.include_memgame_view);
        remaining = (TextView)findViewById(R.id.remaining);
        cur_time = (TextView)findViewById(R.id.curtime);
        button[0] = (RelativeLayout)findViewById(R.id.imgbutton1);
        button[1] = (RelativeLayout)findViewById(R.id.imgbutton2);
        button[2] = (RelativeLayout)findViewById(R.id.imgbutton3);
        button[3] = (RelativeLayout)findViewById(R.id.imgbutton4);
        button[4] = (RelativeLayout)findViewById(R.id.imgbutton5);
        button[5] = (RelativeLayout)findViewById(R.id.imgbutton6);
        button[6] = (RelativeLayout)findViewById(R.id.imgbutton7);
        button[7] = (RelativeLayout)findViewById(R.id.imgbutton8);
        button[8] = (RelativeLayout)findViewById(R.id.imgbutton9);
        button[9] = (RelativeLayout)findViewById(R.id.imgbutton10);
        button[10] = (RelativeLayout)findViewById(R.id.imgbutton11);
        button[11] = (RelativeLayout)findViewById(R.id.imgbutton12);
        button[12] = (RelativeLayout)findViewById(R.id.imgbutton13);
        button[13] = (RelativeLayout)findViewById(R.id.imgbutton14);
        button[14] = (RelativeLayout)findViewById(R.id.imgbutton15);
        button[15] = (RelativeLayout)findViewById(R.id.imgbutton16);

        button_img[0] = (ImageView)findViewById(R.id.button_img_1);
        button_img[1] = (ImageView)findViewById(R.id.button_img_2);
        button_img[2] = (ImageView)findViewById(R.id.button_img_3);
        button_img[3] = (ImageView)findViewById(R.id.button_img_4);
        button_img[4] = (ImageView)findViewById(R.id.button_img_5);
        button_img[5] = (ImageView)findViewById(R.id.button_img_6);
        button_img[6] = (ImageView)findViewById(R.id.button_img_7);
        button_img[7] = (ImageView)findViewById(R.id.button_img_8);
        button_img[8] = (ImageView)findViewById(R.id.button_img_9);
        button_img[9] = (ImageView)findViewById(R.id.button_img_10);
        button_img[10] = (ImageView)findViewById(R.id.button_img_11);
        button_img[11] = (ImageView)findViewById(R.id.button_img_12);
        button_img[12] = (ImageView)findViewById(R.id.button_img_13);
        button_img[13] = (ImageView)findViewById(R.id.button_img_14);
        button_img[14] = (ImageView)findViewById(R.id.button_img_15);
        button_img[15] = (ImageView)findViewById(R.id.button_img_16);
    }
    private void all_found() {
        for (int i = 0; i < 16; i++) {
            final int j = i;
            button[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (remaining_cards > 0) {
                        if (!everClick) {
                            everClick = true;
                        }
                        if (!found.get(j)) {
                            if (!itemSelected) {
                                firstSelectedPos = j;
                                itemSelected = true;
                                button_img[j].setImageResource(Cards.get(j));
                            } else if (firstSelectedPos != -1){
                                secondSelectedPos = j;
                                if (firstSelectedPos == secondSelectedPos) {
                                    firstSelectedPos = -1;
                                    secondSelectedPos = -1;
                                    itemSelected = false;
                                }
                                else {
                                    button_img[j].setImageResource(Cards.get(j));
                                    if (firstSelectedPos != -1 && secondSelectedPos != -1) {
                                        if (Cards.get(firstSelectedPos) - Cards.get(secondSelectedPos) == 0) {
                                            found.set(firstSelectedPos, true);
                                            found.set(secondSelectedPos, true);
                                            remaining_cards -= 2;
                                            remaining.setText("남은 개수 : " + Integer.toString(remaining_cards));
                                        } else {
                                            Handler delayHandler = new Handler();
                                            delayHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    button_img[firstSelectedPos].setImageResource(R.drawable.question);
                                                    button_img[secondSelectedPos].setImageResource(R.drawable.question);
                                                    firstSelectedPos = -1;
                                                    secondSelectedPos = -1;
                                                }
                                            }, 500);
                                        }
                                    }
                                    itemSelected = false;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public boolean foundAll(List<Boolean> found) {
        for (int i=0; i<found.size(); i++) {
            if (!found.get(i))
                return false;
        }
        return true;
    }
}
