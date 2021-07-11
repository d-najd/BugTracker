package com.example.bugtracker;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StringToList {
    public static ArrayList<String> StringToList(String data, String string) {
        ArrayList<String> list = new ArrayList<>();
        data = data.substring(1, data.length() - 1); //remove the [] at ends

        String[] parts = data.split(", ");

        for (int i = 0; i < parts.length; i++){
            list.add(parts[i]);
        }
        return list;
    }

    public static ArrayList<Integer> StringToList(String data, int integer){
        ArrayList<Integer> list = new ArrayList<>();
        data = data.substring(1, data.length() - 1); //remove the [] at ends

        String[] parts = data.split(", ");

        for (int i = 0; i < parts.length; i++){
            list.add(Integer.parseInt(parts[i]));
        }
        return list;
    }

}
