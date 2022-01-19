package com.aatesting.bugtracker.Legacy;

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

    public List<TextView> clockTexts = new ArrayList<TextView>();
    public List<ImageView> clockImages = new ArrayList<ImageView>();

    public final List<String> clockHourStrings = new ArrayList<String>();
    public final List<String> clockMinuteStrings = new ArrayList<String>();

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
            clockHourStrings.add(i + "");
        clockHourStrings.set(0, "12");

        clockMinuteStrings.add("00");
        clockMinuteStrings.add("05");
        clockMinuteStrings.add("10");
        clockMinuteStrings.add("15");
        clockMinuteStrings.add("20");
        clockMinuteStrings.add("25");
        clockMinuteStrings.add("30");
        clockMinuteStrings.add("35");
        clockMinuteStrings.add("40");
        clockMinuteStrings.add("45");
        clockMinuteStrings.add("50");
        clockMinuteStrings.add("55");
    }

    public List<String> GetHourList(){
        return clockHourStrings;
    }

    public List<String> GetMinuteList(){
        return clockMinuteStrings;
    }

    public void DateTimeMinutes(){
        ObjectAnimator fadeOut;
        final ObjectAnimator[] fadeIn = new ObjectAnimator[1];
        int duration = 175;
        float size = 1.71f;
        float smallSize = 1.43f;
        float clockWidth = clockTexts.get(0).getWidth();
        float clockHeight = clockTexts.get(0).getHeight();

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(0),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(0).setText(clockMinuteStrings.get(0));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(0),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //1
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(1),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(1).setText(clockMinuteStrings.get(1));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(1),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //2
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(2),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(2).setText(clockMinuteStrings.get(2));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(2),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //3

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(3),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(3).setText(clockMinuteStrings.get(3));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(3),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //4
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(4),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(4).setText(clockMinuteStrings.get(4));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(4),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //5
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(5),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(5).setText(clockMinuteStrings.get(5));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(5),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //6

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(6),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",   clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clockTexts.get(6).setText(clockMinuteStrings.get(6));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(6),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //7
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(7),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(7).setText(clockMinuteStrings.get(7));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(7),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //8
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(8),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(8).setText(clockMinuteStrings.get(8));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(8),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //9
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(9),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(9).setText(clockMinuteStrings.get(9));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(9),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });


        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(10),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(10).setText(clockMinuteStrings.get(10));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(10),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //11
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(11),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(11).setText(clockMinuteStrings.get(11));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(11),
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
        float clockWidth = clockTexts.get(0).getWidth();
        float clockHeight = clockTexts.get(0).getHeight();

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(0),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",  0, clockHeight / size));


        DateTimeStartAnim(fadeOut, duration);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clockTexts.get(0).setText(clockHourStrings.get(0));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(0),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY",  -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //1
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(1),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(1).setText(clockHourStrings.get(1));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(1),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //2
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(2),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(2).setText(clockHourStrings.get(2));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(2),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //3

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(3),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clockTexts.get(3).setText(clockHourStrings.get(3));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(3),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //4
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(4),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(4).setText(clockHourStrings.get(4));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(4),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //5
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(5),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   -clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(5).setText(clockHourStrings.get(5));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(5),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });
        //6
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(6),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clockTexts.get(6).setText(clockHourStrings.get(6));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(6),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //7
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(7),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(7).setText(clockHourStrings.get(7));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(7),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / size, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / smallSize, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //8
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(8),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, -clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(8).setText(clockHourStrings.get(8));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(8),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //9
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(9),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                clockTexts.get(9).setText(clockHourStrings.get(9));

                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(9),

                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", 0, -clockWidth / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //10

        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(10),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / smallSize),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / size));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(10).setText(clockHourStrings.get(10));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(10),
                        PropertyValuesHolder.ofFloat("alpha", 0, 1),
                        PropertyValuesHolder.ofFloat("translationX", -clockWidth / smallSize, 0),
                        PropertyValuesHolder.ofFloat("translationY",   -clockHeight / size, 0));

                DateTimeStartAnim(fadeIn[0], duration);
            }
        });

        //11
        fadeOut = ObjectAnimator.ofPropertyValuesHolder(
                clockTexts.get(11),
                PropertyValuesHolder.ofFloat("alpha", 1, 0),
                PropertyValuesHolder.ofFloat("translationX",   clockWidth / size),
                PropertyValuesHolder.ofFloat("translationY", 0, clockHeight / smallSize));

        DateTimeStartAnim(fadeOut, duration);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clockTexts.get(11).setText(clockHourStrings.get(11));
                fadeIn[0] = ObjectAnimator.ofPropertyValuesHolder(
                        clockTexts.get(11),
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
