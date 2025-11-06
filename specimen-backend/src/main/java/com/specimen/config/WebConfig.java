package com.specimen.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT拦截器 - 需要登录的接口
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**", "/admin/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/specimens",
                        "/api/specimens/**",
                        "/api/map/**",
                        "/api/announcements",
                        "/api/announcements/**",
                        "/admin/auth/login"
                );

        // 管理员权限拦截器
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/auth/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置CORS跨域
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
