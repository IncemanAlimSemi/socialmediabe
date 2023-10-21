package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
