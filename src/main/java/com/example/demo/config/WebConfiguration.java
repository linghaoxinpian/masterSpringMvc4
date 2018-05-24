package com.example.demo.config;

import com.example.demo.dates.Zh_LocalDateFormatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.time.LocalDate;

//对spring mvc进行自定义配置的类
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class,new Zh_LocalDateFormatter());
    }
}
