package com.robertradulescu.lingo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

// @EnableCaching activa el soporte de caché de Spring en toda la app.
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String TRANSLATIONS = "translations";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(TRANSLATIONS);
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))   // TTL: cada entrada caduca en 1 hora
                .maximumSize(1000)                        // máximo 1000 traducciones en caché
                .recordStats());                          // guarda estadísticas (hits/misses)
        return manager;
    }
}
