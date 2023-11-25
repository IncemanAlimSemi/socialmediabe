package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.LikeActionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeActionRepository extends JpaRepository<LikeAction, LikeActionKey> {

}
