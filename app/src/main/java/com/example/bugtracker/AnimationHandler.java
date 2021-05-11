package com.example.bugtracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.util.ArrayList;
import java.util.List;

public class AnimationHandler {

    public List<TextView> clock_texts = new ArrayList<TextView>();
    public List<ImageView> clock_images = new ArrayList<ImageView>();

    private final List<String> clockHour_strings = new ArrayList<String>();
    private final List<String> clockMinute_strings = new ArrayList<String>();

    public void PulseAnim(TextView textView){
        ObjectAnimator pulseAnim = ObjectAnimator.ofPropertyValuesHolder(
                textView,
                PropertyValuesHolder.ofFloat("scaleX", 1.12f),
                PropertyValuesHolder.ofFloat("scaleY", 1.12f));
        pulseAnim.setDuration(200);

        pulseAnim.setInterpolator(new FastOutSlowInInterpolator());

        pulseAnim.setRepeatCount(ObjectAnimator.RESTART);
        pulseAnim.setRepeatMode(ObjectAnimator.REVERSE);
        pulseAnim.start();
    }

    public void DateTimeListsToAdd(){
        for (int i = 0; i < 12; i++)
            clockHour_strings.add(i + "");
        clockHour_strings.set(0, "12");

        clockMinute_strings.add("00");
        clockMinute_strings.add("05");
        clockMinute_strings.add("10");
        clockMinute_strings.add("15");
        clockMinute_strings.add("20");
        clockMinute_strings.add("25");
        clockMinute_strings.add("30");
        clockMinute_strings.add("35");
        clockMinute_strings.add("40");
        clockMinute_strings.add("45");
        clockMinute_strings.add("50");
        clockMinute_strings.add("55");
    }

    public List<String> GetHourList(){
        return clockHour_strings;
    }

    public List<String> GetMinuteList(){
        return clockMinute_strings;
    }

    public void DateTimeMinutes(){
        ObjectAnimator fadeOut;
        final ObjectAnimator[] fadeIn = new ObjectAnimator[1];
        int duration = 175;
        float size = 1.71f;
        float smallSize = 1.43f;
        float clockWidth = clock_texts.get(0).getWidth();
        float clockHeight = clock_texts.get(0).getHeight();

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(0),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(0).setText(clockMinute_strings.get(0));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(0),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //1
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(1),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(1).setText(clockMinute_strings.get(1));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(1),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //2
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(2),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(2).setText(clockMinute_strings.get(2));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(2),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //3

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(3),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(3).setText(clockMinute_strings.get(3));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(3),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //4
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(4),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(4).setText(clockMinute_strings.get(4));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(4),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //5
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(5),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(5).setText(clockMinute_strings.get(5));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(5),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //6

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(6),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",   clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clock_texts.get(6).setText(clockMinute_strings.get(6));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(6),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //7
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(7),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(7).setText(clockMinute_strings.get(7));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(7),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //8
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(8),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(8).setText(clockMinute_strings.get(8));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(8),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //9
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(9),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(9).setText(clockMinute_strings.get(9));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(9),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });


        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(10),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(10).setText(clockMinute_strings.get(10));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(10),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //11
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(11),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(11).setText(clockMinute_strings.get(11));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(11),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
    }

    public void DateTimeHours(){
        ObjectAnimator fadeOut;
        final ObjectAnimator[] fadeIn = new ObjectAnimator[1];
        int duration = 200;
        float size = 1.71f;
        float smallSize = 1.43f;
        float clockWidth = clock_texts.get(0).getWidth();
        float clockHeight = clock_texts.get(0).getHeight();

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(0),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",  0, clockHeight / size));


        DateTimeStartAnim(fadeOut, duration);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clock_texts.get(0).setText(clockHour_strings.get(0));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(0),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY",  -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //1
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(1),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(1).setText(clockHour_strings.get(1));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(1),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //2
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(2),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(2).setText(clockHour_strings.get(2));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(2),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //3

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(3),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clock_texts.get(3).setText(clockHour_strings.get(3));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(3),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //4
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(4),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(4).setText(clockHour_strings.get(4));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(4),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //5
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(5),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(5).setText(clockHour_strings.get(5));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(5),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //6
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(6),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clock_texts.get(6).setText(clockHour_strings.get(6));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(6),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //7
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(7),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(7).setText(clockHour_strings.get(7));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(7),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //8
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(8),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(8).setText(clockHour_strings.get(8));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(8),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //9
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(9),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clock_texts.get(9).setText(clockHour_strings.get(9));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(9),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //10

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(10),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(10).setText(clockHour_strings.get(10));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(10),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //11
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clock_texts.get(11),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clock_texts.get(11).setText(clockHour_strings.get(11));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clock_texts.get(11),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
    }

    private void DateTimeStartAnim(ObjectAnimator anim, int duration){
        anim.setInterpolator(new FastOutSlowInInterpolator());
        anim.setDuration(duration);
        anim.start();
    }
}
