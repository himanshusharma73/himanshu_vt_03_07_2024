package com.url_shortner.controller;

import com.url_shortner.entity.ShortenedUrl;
import com.url_shortner.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ShortenedUrl shortenUrl(@RequestBody String destinationUrl) {
        return service.shortenUrl(destinationUrl);
    }

    @PostMapping("/update")
    public boolean updateShortUrl(@RequestParam String shortUrl, @RequestParam String destinationUrl) {
        return service.updateShortUrl(shortUrl, destinationUrl);
    }

    @GetMapping("/destination")
    public String getDestinationUrl(@RequestParam String shortUrl) {
        return service.getDestinationUrl(shortUrl);
    }

    @PostMapping("/expiry")
    public boolean updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        return service.updateExpiry(shortUrl, daysToAdd);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortUrl) {
        try {
            String destinationUrl = service.getDestinationUrl("http://localhost:8080/" + shortUrl);
            response.sendRedirect(destinationUrl);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found or expired", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }
}
