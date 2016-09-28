package org.wefine.spring.jooq.config.beetl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExt implements WebRenderExt {

    @Override
    public void modify(Template template,
                       GroupTemplate groupTemplate,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        // 可以通过模板设置一些可全局访问的属性
        template.binding("version", "1.0.1");

    }

}
