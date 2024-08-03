package org.project.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.project.helper.Indices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "INDEX-SERVICE")
public class IndexService {
	List<String> INDICES_TO_CREATE = List.of(Indices.VEHICLE_INDEX);
	ElasticsearchClient client;

	@PostConstruct
	public void init() {
		tryToCreateIndex(false);
	}

	public void tryToCreateIndex(boolean deleteExisting) {

		for (String str : INDICES_TO_CREATE) {
			try (InputStream input = new FileInputStream("./src/main/resources/static/mappings/vehicle.json");
					InputStream settings = new FileInputStream("./src/main/resources/static/es-setting.json"))
			{
				BooleanResponse indexExist = client.indices().exists( exist
						-> exist.index(str));

				if (indexExist.value()) {
					if (deleteExisting) continue;

					client.indices().delete(
							delete -> delete.index(str));
				}

				CreateIndexRequest request = CreateIndexRequest.of(
						builder -> builder
								.index(str)
								.settings(setting -> setting.withJson(settings))
								.mappings(map -> map.withJson(input))
				);

				CreateIndexResponse response = client.indices().create(request);
				boolean ack = Boolean.TRUE.equals(response.acknowledged());

				if (!ack) {
					log.error("Failed to create index with name {}", str);
				}

			} catch (final IOException e) {
				log.error(e.getMessage());
			}
		}
	}

}
