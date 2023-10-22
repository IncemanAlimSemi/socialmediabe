package com.alseinn.socialmedia.service.user;

import com.alseinn.socialmedia.entity.user.User;

public interface UserService {
    User findByUsername(String username);


}
