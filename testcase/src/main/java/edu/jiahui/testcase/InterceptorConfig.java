package edu.jiahui.testcase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 不需要登录拦截的url
     */
    final String[] notLoginInterceptPaths = {
            "/home"
            ,"/slowSqlIncrement"
            ,"/metrics"
            ,"/actuator/prometheus"
            ,"/rpc/**"
            ,"/erp/**"
            ,"/cmp_sp/**"
            ,"/report/week/**"
            ,"/user/login"
    };

    @Bean
    public HandlerInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(notLoginInterceptPaths);
    }
}
