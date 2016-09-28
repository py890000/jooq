package org.wefine.spring.jooq.config.beetl;

import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.ViewResolver;

@Profile("beetl")
@Configuration
public class ClasspathBeetlConfig {

    @Bean(initMethod = "init", name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {

        BeetlGroupUtilConfiguration configuration = new BeetlGroupUtilConfiguration();
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
        configuration.setResourceLoader(resourceLoader);

        //读取配置文件信息
        configuration.setConfigFileResource(new ClassPathResource("beetl.properties"));
        return configuration;
    }

    @Bean(name = "viewResolver")
    public ViewResolver viewResolver() {
        BeetlSpringViewResolver viewResolver = new BeetlSpringViewResolver();
        viewResolver.setOrder(0);
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");
        viewResolver.setContentType("text/html;charset=UTF-8");

        return viewResolver;
    }
}
