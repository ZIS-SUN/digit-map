package com.specimen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 植物标本库管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.specimen.mapper")
public class SpecimenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpecimenApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("植物标本库管理系统启动成功！");
        System.out.println("接口文档地址: http://localhost:8080/doc.html");
        System.out.println("========================================\n");
    }
}
