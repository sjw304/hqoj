package xyz.lizhaorong.queoj.util.converter;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Component
public class DateConverter implements Converter<String, Date> {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String dateFormata = "yyyy/MM/dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String shortDateFormata = "yyyy/MM/dd";
    private static final String timeStampFormat = "^\\d+$";

    @Override
    public Date convert(String value) {
        if(StrUtil.isEmpty(value)) {
            return null;
        }
        value = value.trim();
        try {
            if (value.contains("-")) {
                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    //yyyy-MM-dd HH:mm:ss 格式
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    //yyyy-MM-dd 格式
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                return formatter.parse(value);
            } else if (value.matches(timeStampFormat)) {
                //时间戳
                Long lDate = Long.valueOf(value);
                return new Date(lDate);
            }else if (value.contains("/")){
                SimpleDateFormat formatter;
                if (value.contains(":")) {
//                    yyyy/MM/dd HH:mm:ss 格式
                    formatter = new SimpleDateFormat(dateFormata);
                } else {
//                    yyyy/MM/dd 格式
                    formatter = new SimpleDateFormat(shortDateFormata);
                }
                return formatter.parse(value);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", value));
    }
}
