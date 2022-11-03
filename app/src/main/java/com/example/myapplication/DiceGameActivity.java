package com.example.myapplication;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DiceGameActivity extends AppCompatActivity {
    //    sensor listener
    ShakeListener mShakeListener = null;
    TemperatureListener mTemperatureListener = null;
    LightListener mLightListener = null;
    Vibrator vibrator = null;
    SoundPool soundPool = null;

    //  sensor switch boolean value
    boolean soundAble;
    boolean vibrationSensor;
    boolean lightSensor;

    ImageView diceCupImg;
    Button btnOpen;


    String selectParameter;
    Map<String, Integer> currentResultMap;
    Boolean is_cover = true;

    Integer[] matchImgList = {R.drawable.empty_dice, R.drawable.dice_1, R.drawable.dice_2,
            R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};

    Integer[] TextBlkList = {R.id.result_text0, R.id.result_text1, R.id.result_text2,
            R.id.result_text3, R.id.result_text4, R.id.result_text5};

    String[] TextStrList = {"One", "Two", "Three", "Four", "Five", "Six"};

    Integer[] imgBlkList = {R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3,
            R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8,
            R.id.imageView9, R.id.imageView10, R.id.imageView11};

    ArrayList<Integer> imgBlkStartList = new ArrayList<>();
    ArrayList<Integer> imgBlkTopList = new ArrayList<>();


    //    change the cup image
    public void updateDiceCup() {
        ImageView dicecupV = findViewById(R.id.imageDiceCup);
        dicecupV.setImageResource(SettingsActivity.selectedCup);
    }

    @Override
    protected void onStart() {
        super.onStart();
//       base on the sensor switch boolean value to start or stop
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            soundAble = extras.getBoolean("soundAble");
            vibrationSensor = extras.getBoolean("vibrationSensor");
            lightSensor = extras.getBoolean("lightSensor");
            if (lightSensor == false) {
                mLightListener.stop();
            } else {
                mLightListener.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mShakeListener.stop();
        mLightListener.stop();
        mTemperatureListener.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Dice Game", "Start!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_game);

//        set listener
        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new shakeLitener());

        mTemperatureListener = new TemperatureListener(this);
        mTemperatureListener.setOnTemperatureListener(new temperatureLitener());

        mLightListener = new LightListener(this);
        mLightListener.setOnLightListener(new lightChangeListener());

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        set the shake sound on the soundpool
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.shakesound, 1);

//           the possible dice place
        for (int imgBlk : imgBlkList) {
            MarginLayoutParams imgBlkPara = (MarginLayoutParams) findViewById(imgBlk).getLayoutParams();
            int imgBlkStart = imgBlkPara.leftMargin;
            imgBlkStartList.add(imgBlkStart);
            int imgBlkTop = imgBlkPara.topMargin;

            imgBlkTopList.add(imgBlkTop);
        }

        // Update Dice Cup
        updateDiceCup();

//        roll the dice
        TextView showNumDices = findViewById(R.id.text_num_dice);
        selectParameter = getIntent().getStringExtra("selectParameter");
        showNumDices.setText("Playing with " + selectParameter + " Dices");
        currentResultMap = rollDice();

//        open the cup to see the results
        diceCupImg = findViewById(R.id.imageDiceCup);
//        TextView diceCupText = findViewById(R.id.shake_instruction);
        btnOpen = findViewById(R.id.open_btn);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_cover) {
                    displayStat(currentResultMap);
                    diceCupImg.setImageAlpha(0);
//                    diceCupText.setVisibility(View.INVISIBLE);
                    btnOpen.setText(R.string.cover_button);
                    is_cover = false;
                } else {
                    hideTextView();
                    diceCupImg.setImageAlpha(255);
//                    diceCupText.setVisibility(View.VISIBLE);
                    btnOpen.setText(R.string.open_button);
                    is_cover = true;
                }
            }
        });

//      shake button will roll the dices and play the sound and vibrate
        Button btnShake = findViewById(R.id.shake_btn);
        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showContent.setText(R.string.test_text_3);
                is_cover = true;
                hideTextView();
                diceCupImg.setImageAlpha(255);
                // diceCupText.setVisibility(View.VISIBLE);
                btnOpen.setText(R.string.open_button);
                currentResultMap = rollDice();
                if (vibrationSensor)
                    vibrator.vibrate(250);
                if (soundAble)
                    soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v ->
                finish()
        );
    }

    //    hide the result dice text
    private void hideTextView() {
        for (int textBlk : TextBlkList) {
            TextView textBlkV = findViewById(textBlk);
            textBlkV.setVisibility(View.INVISIBLE);
        }
    }

    //set dice random position and display the corresponding dice image
    private void display(int rollNumber, ImageView diceImage, int imgIndex) {
        LayoutParams standard_params = findViewById(R.id.imageBase0).getLayoutParams();
        MarginLayoutParams params = (MarginLayoutParams) diceImage.getLayoutParams();
        if (rollNumber > 0) {
            int max_bound = 5;
            int min_bound = -5;
            Random rand = new Random();
            int size_change = rand.nextInt((max_bound - min_bound) + 1) + min_bound;
            int horizontal_change = rand.nextInt((max_bound - min_bound) + 1) + min_bound;
            int vertical_change = rand.nextInt((max_bound - min_bound) + 1) + min_bound;
            params.width = standard_params.width + (size_change * 6);
            params.height = standard_params.height + (size_change * 4);
            params.leftMargin = imgBlkStartList.get(imgIndex) + (horizontal_change);
            params.topMargin = imgBlkTopList.get(imgIndex) + (vertical_change);
            diceImage.requestLayout();
        }
        diceImage.setImageResource(matchImgList[rollNumber]);
    }

    //    display the result text
    private void displaySubStat(Integer id, String s, Map<String, Integer> map) {
        TextView textVSub = findViewById(id);
        textVSub.setVisibility(View.VISIBLE);
        textVSub.setText(s + ": " + map.get(s));
        if (map.get(s) == 0) {
            textVSub.setTextColor(this.getResources().getColor(R.color.cream_100));
            textVSub.setTypeface(Typeface.create(textVSub.getTypeface(), Typeface.NORMAL));
        }
        if (map.get(s) != 0) {
            textVSub.setTextColor(this.getResources().getColor(R.color.cream_200));
            textVSub.setTypeface(Typeface.create(textVSub.getTypeface(), Typeface.BOLD));
        }
    }

    //    display the result text
    private void displayStat(Map<String, Integer> map) {
        for (int i = 0; i < TextStrList.length; i++) {
            displaySubStat(TextBlkList[i], TextStrList[i], map);
        }
    }

//    the structure to store the dice number and dice image
    private Map<String, Integer> rollDice() {
        int numberOfDice = Integer.valueOf(selectParameter);
        DiceCup diceCup = new DiceCup();
        diceCup.setDice(numberOfDice);

        ArrayList<Integer> topList = diceCup.getOnTops();
        Map<String, Integer> resultMap = diceCup.getResultMap();

        // i is the index of the dice in dice cup
        for (int i = 0; i < 12; i++) {
            int top = topList.get(i);
            ImageView diceImage;
            diceImage = findViewById(imgBlkList[i]);
            display(top, diceImage, i);
        }
        // displayStat(resultMap);
        return resultMap;
    }


    //accelerator implements interface
    private class shakeLitener implements ShakeListener.OnShakeListener {
//        the funciton will call after shake
        @Override
        public void AfterShake() {
            // TODO Auto-generated method stub
            currentResultMap = rollDice();
//            soundPool.pause(1);
        }
//        when start shake this function will call
        public void StartShake() {
            // TODO Auto-generated method stub
            if (soundAble) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }

            is_cover = true;
            hideTextView();
            diceCupImg.setImageAlpha(255);
            btnOpen.setText(R.string.open_button);
        }

        public void OnShaking() {
            // TODO Auto-generated method stub
//            tv.setText("摇一摇中！");
            if (vibrationSensor) {
                vibrator.vibrate(250);
            }


        }
    }

    //temperatureLitener implement interface
    private class temperatureLitener implements TemperatureListener.TemperatureChangeListener {
        @Override
        public void ChangeTemperature(float temp) {
//            temperaturetextView.setText("temperature:" + temp + "℃");
            if (temp > 100) {
                Toast.makeText(DiceGameActivity.this, "Temperature is too high.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //light sensor implement interface
    private class lightChangeListener implements LightListener.LightChangeListener {
        ConstraintLayout DiceGameLayout = findViewById(R.id.dicegame_layout);

//        when the light is smaller than threshold it will change to the night mode and change the text color
        @Override
        public void ChangeLight(SensorEvent temp) {
            float acc = temp.accuracy;
            float lux = temp.values[0];
            String s = Float.toString(lux);
            System.out.print("Lux is " + lux);
            try {
                if (lightSensor) {
                    Log.i("Lux", s);
                    if (lux >= 50) {
                        Log.i("Theme", "Day");
                        //  change the light mode of the app
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        DiceGameLayout.setBackgroundResource(R.color.cream_000);
                        for (Integer textid : TextBlkList) {
                            TextView SubStat = findViewById(textid);
                            String a = SubStat.getText().toString();
                            char last = a.charAt(a.length() - 1);
                            if (last == '0') {
                                SubStat.setTextColor(getResources().getColor(R.color.cream_100));
                            } else {
                                SubStat.setTextColor(getResources().getColor(R.color.cream_200));
                            }

                        }

                    } else {
                        Log.i("Theme", "Night");
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        DiceGameLayout.setBackgroundResource(R.color.blue_700);
                        for (Integer textid : TextBlkList) {
                            TextView SubStat = findViewById(textid);
                            String a = SubStat.getText().toString();
                            char last = a.charAt(a.length() - 1);
                            if (last == '0') {
                                SubStat.setTextColor(getResources().getColor(R.color.blue_500));
                            } else {
                                SubStat.setTextColor(getResources().getColor(R.color.blue_100));
                            }
                        }

//                    recreate();


                    }
                }
            } catch (Exception e) {

            }


        }
    }
}
