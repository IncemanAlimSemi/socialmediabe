package com.alseinn.socialmedia.dao.image;

import com.alseinn.socialmedia.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
