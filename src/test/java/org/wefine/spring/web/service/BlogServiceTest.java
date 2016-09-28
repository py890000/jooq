package org.wefine.spring.web.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wefine.spring.config.core.SpringMvcConfig;
import org.wefine.spring.config.core.SpringRootConfig;
import org.wefine.spring.web.dto.Comment;
import org.wefine.spring.web.dto.Post;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        SpringRootConfig.class,
        SpringMvcConfig.class
})
public class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Test
    public void findAllPosts() {
        List<Post> posts = blogService.getAllPosts();
        assertNotNull(posts);
        assertTrue(!posts.isEmpty());
        posts.forEach(System.err::println);
    }

    @Test
    public void findPostById() {
        Post post = blogService.getPost(1);
        assertNotNull(post);
        System.out.println(post);
        List<Comment> comments = post.getComments();
        System.out.println(comments);
    }

    @Test
    public void createPost() {
        Post post = new Post(0, "My new Post",
                "This is my new test post",
                new Timestamp(System.currentTimeMillis()));
        Post savedPost = blogService.createPost(post);
        Post newPost = blogService.getPost(savedPost.getId());
        assertEquals("My new Post", newPost.getTitle());
        assertEquals("This is my new test post", newPost.getContent());
    }

    @Test
    public void createComment() {
        Integer postId = 1;
        Comment comment = new Comment(0, postId, "User4",
                "user4@gmail.com", "This is my new comment on post1",
                new Timestamp(System.currentTimeMillis()));
        Comment savedComment = blogService.createComment(comment);
        Post post = blogService.getPost(postId);
        List<Comment> comments = post.getComments();
        assertNotNull(comments);
        for (Comment comm : comments) {
            if (savedComment.getId() == comm.getId()) {
                assertEquals("User4", comm.getName());
                assertEquals("user4@gmail.com", comm.getEmail());
                assertEquals("This is my new comment on post1", comm.getContent());
            }
        }
    }
}
