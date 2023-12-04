package com.alseinn.socialmedia.entity.listeners;

import com.alseinn.socialmedia.entity.item.Item;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class ModifiedDateEntityListener {
    @PrePersist
    @PreUpdate
    public void prePersistOrUpdateOrRemove(Item item) {
        item.setTimeModified(new Date(System.currentTimeMillis()));
    }
}