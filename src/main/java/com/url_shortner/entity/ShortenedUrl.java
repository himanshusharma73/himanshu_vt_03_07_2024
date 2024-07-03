package com.url_shortner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenedUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String shortUrl;
    private String destinationUrl;
    private LocalDateTime expiryDate;

}
