package com.aatesting.bugtracker.fragments.Main.GridFragment;

public class GridFragmentSettings {
    public static String BOARD_TAG = "BoardView";
    public static String ARROW_MAIN = "Arrow"; //should help help with readiblility in the listeners
    public static String ARROW_VIEW_TAG = ARROW_MAIN + "View"; //the full arrow
    public static String ARROW_HEAD_TAG = ARROW_MAIN + "HeadView";
    public static String ARROW_BACK_TAG = ARROW_MAIN + "BackView";
    public static String ARROW_BODY_TAG = ARROW_MAIN + "BodyView";

    public static float spacing =                10f * GridFragment.dp;

    public static final float BOARD_LAYER =      .500f;
    public static final float ARROW_HEAD_LAYER = .012f;
    public static final float ARROW_BACK_LAYER = .011f;
    public static final float ARROW_BODY_LAYER = .010f;

    /*
        each view has to have a different tag, so I am using an universal id for all of them
     */
    public static long curId = 0;
}
