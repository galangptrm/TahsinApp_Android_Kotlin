package com.example.galang.tahsin_beta_kotlin.algorithm;

import java.io.CharArrayReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Test {

    public  Test() {
    }

    ArrayList list = new ArrayList();



    public static void main(String[] args) throws MalformedURLException {
        URL obx = new URL("https://ekrut.com/join");
        String obj = "abcdefg";
        int length = obj.length();
        char c[] = new char[length];
        obj.getChars(0, length, c, 0);

        CharArrayReader input1 = new CharArrayReader(c);
        CharArrayReader input2 = new CharArrayReader(c, 1, 4);

        int i;
        int j;

            System.out.print(obx.toExternalForm());

    }

}


