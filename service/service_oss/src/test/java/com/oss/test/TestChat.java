package com.oss.test;

/**
 * Created by Administrator on 2022/4/11.
 */
public class TestChat {
    public static void main(String[] args) {
        char c = '9';
        int i = c - '0' ;
        System.out.println(i);
        String s = String.valueOf(c);
        int in = Integer.parseInt(s);
        double v = Double.parseDouble(s);
        System.out.println(in);
        System.out.println(v);
    }
}
