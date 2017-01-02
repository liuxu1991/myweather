package com.ibm.myweather.gson;

/**
 * Created by 刘旭 on 2017/1/1.
 */

public class Forecast {
    public String date;
    public Cond cond;
    public Tmp tmp;
    public class Cond{
        public String text_d;
    }

    public class Tmp {
        public String max;
        public String min;
    }
}
