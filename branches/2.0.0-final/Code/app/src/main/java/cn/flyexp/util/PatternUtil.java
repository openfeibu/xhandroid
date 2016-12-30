package cn.flyexp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class PatternUtil {

    public static boolean validateUserPwd(String pwd) {
        return matcher("[a-zA-Z0-9]{6,16}", pwd);
    }

    public static boolean validatePhone(String phone) {
        return matcher("1\\d{10}", phone);
    }

    public static boolean validateNumberId(String numberid) {
        return matcher("[0-9]{17}([0-9]|X|x))|([0-9]{14}([0-9]|X|x))", numberid);
    }

    private static boolean matcher(String regex, String input) {
        return Pattern.compile(regex).matcher(input).matches();
    }


    final static String REGEX = "((http|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    final static String REPLACER = "<a href=\"$0\">链接<a>";

    public static String regexReplaceURL(String str) {
        if (str == null) {
           return str;
        }
        return str.replaceAll(REGEX, REPLACER);
    }
}
