package com.stock.pycurrent.config;

import com.stock.pycurrent.filter.NoCacheFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @author fzc
 * @date 2024/6/25 11:51
 * @description
 */
@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        FilterRegistrationBean<NoCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoCacheFilter());
        registrationBean.addUrlPatterns("/*"); // 这里设置需要应用过滤器的URL模式
        registrationBean.setOrder(1); // 设置过滤器顺序
        return registrationBean;
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> stars = List.of("*");
        configuration.setAllowedOriginPatterns(stars);
        configuration.setAllowedMethods(stars);
        configuration.setAllowedHeaders(stars);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
