package com.example.app.utils;
import cn.hutool.core.util.RandomUtil;
public class Number {

    public static String getRandomId() {
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 20; i++) {
            int number = RandomUtil.randomInt(0, 9);
            id.append(number);
        }

        return "-" + id;
    }

}
