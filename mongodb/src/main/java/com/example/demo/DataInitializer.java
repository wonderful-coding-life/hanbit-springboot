package com.example.demo;

import com.example.demo.dto.PharmacyJson;
import com.example.demo.model.Article;
import com.example.demo.model.Pharmacy;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(0)
@Slf4j
public class DataInitializer implements ApplicationRunner {
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    @Value("classpath:data/pharmacy.json")
    private Resource pharmacyJsonResource;
    @Value("classpath:data/article.json")
    private Resource articleJsonResource;
    private final PharmacyRepository pharmacyRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadPharmacies();
        loadArticles();
    }

    public void loadPharmacies() throws IOException {
        if (pharmacyRepository.count() > 0) {
            log.info("MongoDB has {} pharmacies", pharmacyRepository.count());
            return;
        }

        try (InputStream is = pharmacyJsonResource.getInputStream()) {
            List<PharmacyJson> pharmacyJsons = objectMapper.readValue(
                    is,
                    new TypeReference<List<PharmacyJson>>() {}
            );
            var pharmacies = pharmacyJsons.stream().map(this::toEntity).toList();
            pharmacyRepository.saveAll(pharmacies);
            log.info("MongoDB initialized {} pharmacies", pharmacies.size());
        }
    }

    private Pharmacy toEntity(PharmacyJson dto) {
        Point point = new Point(
                dto.getLocation().getX(), // 경도
                dto.getLocation().getY()  // 위도
        );

        return Pharmacy.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .location(point)
                .build();
    }

    private void loadArticles() throws IOException {
        if (articleRepository.count() > 0) {
            log.info("MongoDB has {} articles", articleRepository.count());
            return;
        }

        try (InputStream is = articleJsonResource.getInputStream()) {
            List<Article> articles = objectMapper.readValue(
                    is,
                    new TypeReference<List<Article>>() {}
            );
            articleRepository.saveAll(articles);
            log.info("MongoDB initialized {} articles", articles.size());
        }
    }
}
