package com.fengquanwei.muse.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * spring 应用上下文配置
 *
 * @author fengquanwei
 * @create 2018/12/8 22:40
 **/
@Configuration
@ComponentScan(basePackages = {"com.fengquanwei.muse.web"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class SpringConfig {
}
