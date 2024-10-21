package com.aida.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class catApplication {
    public static void main(String[] args) {
        SpringApplication.run(catApplication.class, args);
        // SpringApplication 是 Spring Boot 提供的一个类，用于简化 Spring 应用的启动过程。
        // run 方法是 SpringApplication 类的一个静态方法，用于启动 Spring 应用。这个方法接受一个或多个参数：
        // 第一个参数是 Spring 应用的主类，该类包含 @SpringBootApplication 注解，
            // 该注解组合了 @Configuration、@EnableAutoConfiguration 和 @ComponentScan 注解，用于指示 Spring Boot 自动配置和组件扫描。
        // 第二个参数 args 是一个字符串数组，表示传递给应用程序的命令行参数。
            // 这些参数可以用于配置应用程序的行为，例如指定配置文件的位置或启用特定的配置属性。
    }
}