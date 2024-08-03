package org.project.repo;

import org.project.document.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepo extends ElasticsearchRepository<User, String> {
}
