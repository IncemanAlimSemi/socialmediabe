package com.alseinn.socialmedia.entity.image;

import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.listeners.ModifiedDateEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ModifiedDateEntityListener.class)
@Entity
@Table(name = "image")
public class Image extends Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Lob
    @Column(name = "image_data", length = 100000)
    private byte[] imageData;
}
