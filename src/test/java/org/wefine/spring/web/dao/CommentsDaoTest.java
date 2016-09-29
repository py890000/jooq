package org.wefine.spring.web.dao;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.wefine.spring.AbstractSpringTest;
import org.wefine.spring.jooq.tables.pojos.Comments;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class CommentsDaoTest extends AbstractSpringTest {

    @Resource
    private CommentsDao dao;

    @Test
    public void testFindAll() {
        List<Comments> list = dao.findAll();

        log.info("size=" + list.size());
    }
}
