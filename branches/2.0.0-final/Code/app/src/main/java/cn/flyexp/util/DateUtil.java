package cn.flyexp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class DateUtil {

    public static long date2Long(String dateStr) {
        if (dateStr == null || dateStr.equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
        }
        return date.getTime();
    }

    public static String long2Date(long t) {
        return long2Date(t, "yyyy-MM-dd");
    }

    public static String long2Date(long t, String format) {
        if (t < 0)
            return "";
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        Date date = null;
        String dateStr = null;
        try {
            date = new Date();
            date.setTime(t);
            dateStr = myFormatter.format(date);
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static String dateFormat(String date, String format) {
        return long2Date(date2Long(date), format);
    }

    //转换距离多长时间
    public static String getStandardDate(long t) {
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

//        Log.e("date", "time=" + time + " mill=" + mill + " minute=" + minute + " hour=" + hour + " day=" + day);
        if (day - 4 > 0) {
            return long2Date(t);
        } else if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static long addOneDay(long t) {
        long day = 24 * 60 * 60 * 1000;
        return t + day;
    }

    //转换距离多长时间
    public static String countDown(long t) {
        String str = "";
        long time = t - System.currentTimeMillis();
        long between = time / 1000;//除以1000是为了转换成秒
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        if (day == 0) {
            str = hour + "小时" + minute + "分";
            if (hour == 0) {
                str = minute + "分";
            }
        } else {
            str = day + "天" + hour + "小时" + minute + "分";
        }
        if (str.charAt(0) == '-') {
            str = "已过时";
        }
        return str;
    }
}
