package com.example.bugtracker;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayListToBytes {
    public static byte[] ArrayListoBytes(ArrayList<String> list, String string) {
    // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        for (String element : list) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();
        return bytes;

// read from byte array
        /*
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(bais);
        while (true) {
            try {
                if (!(in.available() > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String element = null;
            try {
                element = in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.wtf("testegeapgmeagpmeageaogom", element);
            System.out.println(element);
        }

         */
    }

    public static byte[] arrayListToBytes(ArrayList<Integer> list, Integer integer) {
        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        for (Integer element : list) {
            try {
                out.writeUTF(String.valueOf(element));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
}
