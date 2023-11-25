package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p.id, content, post_like, title, user_id, date FROM post as p LEFT JOIN user as u on p.user_id = u.id WHERE p.date >= CURRENT_TIMESTAMP - INTERVAL '7' DAY ORDER BY p.date DESC", nativeQuery = true)
    List<Post> findByUserOrderByDate(User user);

}
