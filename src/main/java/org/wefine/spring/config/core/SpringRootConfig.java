package org.wefine.spring.config.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {
        "classpath*:/META-INF/spring/*-config.xml",
        "classpath*:spring-*.xml"
})
public class SpringRootConfig {

}
