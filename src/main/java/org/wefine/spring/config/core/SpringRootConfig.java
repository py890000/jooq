package org.wefine.spring.config.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ImportResource(locations = {
        "classpath*:/META-INF/spring/*-config.xml",
        "classpath*:spring-datasource.xml"
})
@PropertySource({
        "classpath:application.properties"
})
@ComponentScan({
        "org.wefine.spring.dao"
})
public class SpringRootConfig {

}
