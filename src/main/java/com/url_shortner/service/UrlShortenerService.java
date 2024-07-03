package com.url_shortner.service;

import com.url_shortner.entity.ShortenedUrl;
import com.url_shortner.repo.ShortenedUrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortenedUrlRepository repository;

    private static final String PREFIX = "http://localhost:8080/";
    private static final int SHORT_URL_LENGTH = 8;

    public ShortenedUrl shortenUrl(String destinationUrl) {
        String shortUrl;
        do {
            shortUrl = PREFIX + RandomStringUtils.randomAlphanumeric(SHORT_URL_LENGTH);
        } while (repository.findByShortUrl(shortUrl).isPresent());

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setShortUrl(shortUrl);
        shortenedUrl.setDestinationUrl(destinationUrl);
        shortenedUrl.setExpiryDate(LocalDateTime.now().plusMonths(10));

        return repository.save(shortenedUrl);
    }

    public boolean updateShortUrl(String shortUrl, String newDestinationUrl) {
        Optional<ShortenedUrl> optional = repository.findByShortUrl(shortUrl);
        if (optional.isPresent()) {
            ShortenedUrl shortenedUrl = optional.get();
            shortenedUrl.setDestinationUrl(newDestinationUrl);
            repository.save(shortenedUrl);
            return true;
        }
        return false;
    }

    public String getDestinationUrl(String shortUrl) {
        Optional<ShortenedUrl> optional = repository.findByShortUrl(shortUrl);
        if (optional.isPresent()) {
            ShortenedUrl shortenedUrl = optional.get();
            if (shortenedUrl.getExpiryDate().isAfter(LocalDateTime.now())) {
                return shortenedUrl.getDestinationUrl();
            }
        }
        throw new NoSuchElementException("URL not found or expired");
    }

    public boolean updateExpiry(String shortUrl, int daysToAdd) {
        Optional<ShortenedUrl> optional = repository.findByShortUrl(shortUrl);
        if (optional.isPresent()) {
            ShortenedUrl shortenedUrl = optional.get();
            shortenedUrl.setExpiryDate(shortenedUrl.getExpiryDate().plusDays(daysToAdd));
            repository.save(shortenedUrl);
            return true;
        }
        return false;
    }
}
