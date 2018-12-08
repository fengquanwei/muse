package com.fengquanwei.muse.springmvc.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * DispatcherServletInitializer
 *
 * @author fengquanwei
 * @create 2018/12/8 22:36
 **/
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 获取 spring 应用上下文配置
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    /**
     * 获取 spring mvc 应用上下文配置
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    /**
     * 获取 DispatcherServlet 映射
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
