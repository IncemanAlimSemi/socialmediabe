package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {

    Optional<LikeAction> findByUsernameAndActionObjectAndActionObjectId(String username, ActionObjectEnum type, Long id);
}
