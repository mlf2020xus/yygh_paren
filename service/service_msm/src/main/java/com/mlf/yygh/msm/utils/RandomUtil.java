package com.mlf.yygh.msm.utils;

import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2021/10/7.
 *
 * 是我们来生成的验证码来传给阿里云服务
 *
 *  专门生成验证码的类
 */
@Configuration
public class RandomUtil {
    private static final Random random = new Random();

    private static final DecimalFormat fourdf = new DecimalFormat("0000");

    private static final DecimalFormat sixdf = new DecimalFormat("000000");

    //生成四位数的验证码
    public static String getFourBitRandom() {
        return fourdf.format(random.nextInt(10000));
    }
    //生成六位数的验证码
    public static String getSixBitRandom() {
        return sixdf.format(random.nextInt(1000000));
    }

    /**
     * 给定数组，抽取n个数据
     * @param list
     * @param n
     * @return
     */
    public static ArrayList getRandom(List list, int n) {
        Random random = new Random();
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        // 生成随机数字并存入HashMap
        for (int i = 0; i < list.size(); i++) {
            int number = random.nextInt(100) + 1;
            hashMap.put(number, i);
        }
        // 从HashMap导入数组
        Object[] robjs = hashMap.values().toArray();
        ArrayList r = new ArrayList();
        // 遍历数组并打印数据
        for (int i = 0; i < n; i++) {
            r.add(list.get((int) robjs[i]));
            System.out.print(list.get((int) robjs[i]) + "\t");
        }
        System.out.print("\n");
        return r;
    }
}
