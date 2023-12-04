package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p.id, p.content, p.post_like, p.title, p.user_id, p.time_created, p.time_modified FROM post as p LEFT JOIN user as u on p.user_id = u.id WHERE p.time_created >= CURRENT_TIMESTAMP - INTERVAL '7' DAY ORDER BY p.time_created DESC", nativeQuery = true)
    List<Post> findByUserOrderByDate(User user);

}
