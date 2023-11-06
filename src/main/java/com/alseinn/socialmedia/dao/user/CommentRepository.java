package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
