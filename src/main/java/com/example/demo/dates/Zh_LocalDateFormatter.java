package com.example.demo.dates;

import org.springframework.format.Formatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Zh_LocalDateFormatter implements Formatter<LocalDate> {

    public static final String US_PATTERN="MM/dd/yyyy"; //中国以外的地区
    public static final String ZH_PATTERN="dd/MM/yyyy"; //中国地区

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        System.out.println("--------------------------------------------------------------注入Locale："+locale);
        return LocalDate.parse(text,DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }

    /**
     * 判断当前客户端处于哪个地区
     * @param locale Locale实例
     * @return  对应地区的日期格式，MM/dd/yyyy或dd/MM/yyyy
     */
    public static String getPattern(Locale locale){
        return Locale.SIMPLIFIED_CHINESE.getCountry().equals(locale.getCountry())?ZH_PATTERN:US_PATTERN;
    }
}
