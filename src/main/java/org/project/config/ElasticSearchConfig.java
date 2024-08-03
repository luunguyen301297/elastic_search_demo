package org.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.project.repo")
@ComponentScan(basePackages = { "org.project" })
public class ElasticSearchConfig {

	@Value("${spring.elasticsearch.uris}")
	private String url;
}
