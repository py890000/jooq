package org.wefine.spring.jooq.config.beetl;

public class ClasspathResourceLoader extends org.beetl.core.resource.ClasspathResourceLoader {
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    @Override
    public boolean exist(String key) {

        return classLoader.getClass().getResource(key) != null;
    }

}
