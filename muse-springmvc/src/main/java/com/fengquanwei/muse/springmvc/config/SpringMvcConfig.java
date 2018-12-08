package com.fengquanwei.muse.springmvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * spring mvc 应用上下文配置
 *
 * @author fengquanwei
 * @create 2018/12/8 22:40
 **/
@Configuration
@EnableWebMvc
@ComponentScan("com.fengquanwei.muse.springmvc")
public class SpringMvcConfig {
}
