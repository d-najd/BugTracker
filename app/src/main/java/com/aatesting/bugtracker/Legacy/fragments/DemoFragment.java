package com.aatesting.bugtracker.Legacy.fragments;

public class DemoFragment {
      /*
    private void DemoListeners(View root){
        Button button = root.findViewById(R.id.button);
        Button button1 = root.findViewById(R.id.button1);
        Button button2 = root.findViewById(R.id.button2);
        Button button3 = root.findViewById(R.id.button3);

        accountFragment = this;

        RadioGroupDialog radioGroupDialog = new RadioGroupDialog();

        ArrayList<String> values = new ArrayList<String>();
        values.add("Don't remind me");
        values.add("Remind me when due");
        values.add("Remind me in advance");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime2(v);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectCreateActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupDialog.StartDialog(v, getContext(), accountFragment, values, 1, Color.YELLOW, getContext().getString(R.string.reminder));
            }
        });
    }

    public void DateTime2(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        AnimationHandler animationHandler = new AnimationHandler();
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_hour, viewGroup, false);

        TextView hours_txt = dialogView.findViewById(R.id.hours);
        TextView minutes_txt = dialogView.findViewById(R.id.minutes);
        TextView am_txt = dialogView.findViewById(R.id.am);
        TextView pm_txt = dialogView.findViewById(R.id.pm);

        Calendar c = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
        String curDate = df.format(c.getTime());

        hourSelected = true;

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hour = "null";
                        String minutes = "null";

                        for (int i = 0; i < clockHourStrings.size(); i++){
                            if (hours_txt.getText().toString().equals(clockHourStrings.get(i)))
                                hour = hours_txt.getText().toString();
                            if (minutes_txt.getText().toString().equals(clockMinuteStrings.get(i)))
                                minutes = minutes_txt.getText().toString();
                        }

                        if (!hour.equals("null") && !minutes.equals("null")){
                            if (am_txt.getCurrentTextColor() != getContext().getColor(R.color.darkerWhite))
                                curTime = hour + ":" + minutes + "am";
                            else
                                curTime = hour + ":" + minutes + "pm";
                        }
                        else
                            Toast.makeText(getContext(), "something went wrong with setting getting" +
                                    "the hours_txt or minutes_txt", Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });;

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);

        animationHandler.DateTimeListsToAdd();

        //region clockClickListeners

        //Toast.makeText(mcontext, clock_images.size() + "", Toast.LENGTH_SHORT).show();
        //put it under clockimages line
        clockImages.clear();
        clockTexts.clear();

        clockImages.add(dialogView.findViewById(R.id.clock00));
        clockImages.add(dialogView.findViewById(R.id.clock01));
        clockImages.add(dialogView.findViewById(R.id.clock02));
        clockImages.add(dialogView.findViewById(R.id.clock03));
        clockImages.add(dialogView.findViewById(R.id.clock04));
        clockImages.add(dialogView.findViewById(R.id.clock05));
        clockImages.add(dialogView.findViewById(R.id.clock06));
        clockImages.add(dialogView.findViewById(R.id.clock07));
        clockImages.add(dialogView.findViewById(R.id.clock08));
        clockImages.add(dialogView.findViewById(R.id.clock09));
        clockImages.add(dialogView.findViewById(R.id.clock10));
        clockImages.add(dialogView.findViewById(R.id.clock11));

        clockTexts.add(dialogView.findViewById(R.id.clock00Text));
        clockTexts.add(dialogView.findViewById(R.id.clock01Text));
        clockTexts.add(dialogView.findViewById(R.id.clock02Text));
        clockTexts.add(dialogView.findViewById(R.id.clock03Text));
        clockTexts.add(dialogView.findViewById(R.id.clock04Text));
        clockTexts.add(dialogView.findViewById(R.id.clock05Text));
        clockTexts.add(dialogView.findViewById(R.id.clock06Text));
        clockTexts.add(dialogView.findViewById(R.id.clock07Text));
        clockTexts.add(dialogView.findViewById(R.id.clock08Text));
        clockTexts.add(dialogView.findViewById(R.id.clock09Text));
        clockTexts.add(dialogView.findViewById(R.id.clock10Text));
        clockTexts.add(dialogView.findViewById(R.id.clock11Text));

        animationHandler.clockImages = clockImages;
        animationHandler.clockTexts = clockTexts;

        clockMinuteStrings = animationHandler.clockMinuteStrings;
        clockHourStrings = animationHandler.clockHourStrings;

        hours_txt.setText(clockHourStrings.get(activeHourBtn));
        minutes_txt.setText(clockMinuteStrings.get(activeMinuteBtn));

        if (!am_pm_Selected)
        {
            pm_txt.setTextColor(getContext().getColor(R.color.white));
            am_txt.setTextColor(getContext().getColor(R.color.darkerWhite));
        }

        hours_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours_txt.getCurrentTextColor() == getContext().getColor(R.color.darkerWhite)) {
                    hourSelected = true;
                    hours_txt.setTextColor(getContext().getColor(R.color.white));
                    minutes_txt.setTextColor(getContext().getColor(R.color.darkerWhite));

                    animationHandler.DateTimeHours();
                    animationHandler.PulseAnim(hours_txt);

                    RevealFAB(clockImages.get(activeHourBtn), true);
                    HideFAB(clockImages.get(activeMinuteBtn));
                }
            }
        });

        minutes_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutes_txt.getCurrentTextColor() == getContext().getColor(R.color.darkerWhite)){
                    hourSelected = false;
                    minutes_txt.setTextColor(getContext().getColor(R.color.white));
                    hours_txt.setTextColor(getContext().getColor(R.color.darkerWhite));

                    animationHandler.DateTimeMinutes();
                    animationHandler.PulseAnim(minutes_txt);

                    RevealFAB(clockImages.get(activeMinuteBtn), true);
                    HideFAB(clockImages.get(activeHourBtn));
                }
            }
        });

        am_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!am_pm_Selected){
                    am_pm_Selected = true;
                    am_txt.setTextColor(getContext().getColor(R.color.white));
                    pm_txt.setTextColor(getContext().getColor(R.color.darkerWhite));
                }
            }
        });

        pm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am_pm_Selected){
                    am_pm_Selected = false;
                    pm_txt.setTextColor(getContext().getColor(R.color.white));
                    am_txt.setTextColor(getContext().getColor(R.color.darkerWhite));
                }
            }
        });

        for (int i = 0; i < clockTexts.size(); i++){
            int finalI = i;
            if (finalI == activeHourBtn) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        int cx = clockImages.get(finalI).getWidth() / 2;
                        int cy = clockImages.get(finalI).getHeight() / 2;
                        float finalRadius = (float) Math.hypot(cx, cy);
                        Animator anim = ViewAnimationUtils.createCircularReveal(clockImages.get(finalI), cx, cy, 0, finalRadius);
                        clockImages.get(finalI).setVisibility(View.VISIBLE);
                        anim.start();
                    }
                });
            }
            clockTexts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hours_txt.getCurrentTextColor() == getContext().getColor(R.color.white))
                        hours_txt.setText(clockHourStrings.get(finalI));
                    else
                        minutes_txt.setText(clockMinuteStrings.get(finalI));
                    RevealFAB(clockImages.get(finalI), false);
                }
            });
        }

        //endregion
    }

    private void RevealFAB(ImageView imageView, boolean specialPass) {
        if ((imageView != clockImages.get(activeHourBtn) && hourSelected) ||
                (imageView != clockImages.get(activeMinuteBtn) && !hourSelected) || specialPass) {

            if (!specialPass) {
                HideFAB(clockImages.get(activeMinuteBtn));
                HideFAB(clockImages.get(activeHourBtn));
            }

            int imageViewId = Integer.parseInt(imageView.toString().substring
                    (imageView.toString().length() - 3, imageView.toString().length() - 1));
            if (hourSelected) {
                activeHourBtn = imageViewId;
            }
            else {
                activeMinuteBtn = imageViewId;
            }
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
            imageView.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    private void HideFAB(ImageView imageView) {
        int cx = imageView.getWidth() / 2;
        int cy = imageView.getHeight() / 2;
        float initialRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

     */
}
