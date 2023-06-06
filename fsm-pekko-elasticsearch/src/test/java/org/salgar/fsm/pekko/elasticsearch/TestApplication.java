package org.salgar.fsm.pekko.elasticsearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class TestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }
}