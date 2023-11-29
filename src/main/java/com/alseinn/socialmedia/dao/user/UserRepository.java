package com.alseinn.socialmedia.dao.user;

import com.alseinn.socialmedia.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT COUNT(f) > 0 FROM User u JOIN u.followings f WHERE u.id = :userId AND f.id = :followId")
    boolean checkIsFollowExist(@Param("userId") Long userId, @Param("followId") Long followId);

}
