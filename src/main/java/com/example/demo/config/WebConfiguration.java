package com.example.demo.config;

import com.example.demo.dates.Zh_LocalDateFormatter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

//对spring mvc进行自定义配置的类
@SpringBootApplication  //包含了Configuration注解
@EnableConfigurationProperties({PictureUploadProperties.class})
@EnableSwagger2
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class,new Zh_LocalDateFormatter());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        return new SessionLocaleResolver();
    }

    //拦截所有发往控制器的请求，并检查lang查询参数,如：http://xxxxx?lang=zh
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    //★处理Servlet容器级别的错误（Tomcat级别），比如MultipartException异常不会由控制器抛出，而是由Tomcat抛出
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory=new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addErrorPages(new ErrorPage(MultipartException.class,"/uploadError"));
        return tomcatServletWebServerFactory;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper=new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false); //移除URL中分号之后的内容，关闭
        configurer.setUrlPathHelper(urlPathHelper);
        configurer.setUseRegisteredSuffixPatternMatch(true);    //省略点号后面的内容，关闭
    }

    //设置swagger只暴露 "/api/" 这一个ApI,不配置则在浏览器显示所有API
    @Bean
    public Docket userApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(path->path.startsWith("/api/"))
                .build();
    }

    // 设置Spring Security允许分号url
    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}
