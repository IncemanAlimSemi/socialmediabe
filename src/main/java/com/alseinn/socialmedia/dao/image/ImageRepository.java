package com.alseinn.socialmedia.dao.image;

import com.alseinn.socialmedia.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
