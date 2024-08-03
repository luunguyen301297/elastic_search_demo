package org.project.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.project.document.Vehicle;
import org.project.dto.VehicleDto;
import org.project.helper.Indices;
import org.project.mapper.VehicleMapper;
import org.project.payload.request.SearchCondition;
import org.project.payload.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "VEHICLE-SERVICE")
public class VehicleService {
	ElasticsearchClient client;
	VehicleMapper vehicleMapper;

	public Boolean save(VehicleDto vehicleDto) {
		try {
			Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);

			IndexResponse response = client.index(
					i -> i.index(Indices.VEHICLE_INDEX)
							.id(vehicle.getId())
							.document(vehicle));

			log.info("Indexed with version {}", response.version());
			return true;
		} catch (JsonProcessingException e) {
			log.error("Error while parsing vehicle to Json string", e);
		} catch (IOException | ElasticsearchException e) {
			log.error("Error while save vehicle to elastic search", e);
		}

		return false;
	}

	public Vehicle getById(String id) {
		try {
			GetResponse<Vehicle> response = client.get(
					g -> g.index(Indices.VEHICLE_INDEX)
							.id(id),
					Vehicle.class);

			if (response.found()) {
				Vehicle vehicle = response.source();
				if (vehicle != null) {
					return vehicle;
				}
			} else {
				log.info ("Vehicle with id {} not found", id);
			}
		} catch (IOException e) {
			log.error("Error while getting vehicle from elasticsearch", e);
		}
		return null;
	}

	public List<VehicleDto> findByCondition(SearchRequest request) {
		try {
			Map<String, JsonData> params = new HashMap<>();

			for (SearchCondition condition : request.getConditionList()) {
				params.put(condition.getColumn(), JsonData.of(condition.getValue()));
			}

			SearchTemplateResponse<Vehicle> response = client.searchTemplate(
					res -> res
							.index(Indices.VEHICLE_INDEX)
							.id("query-script")
							.params(params),
					Vehicle.class
			);

			List<Hit<Vehicle>> hits = response.hits().hits();

			List<VehicleDto> vehicles = new ArrayList<>();

			for (Hit<Vehicle> hit: hits) {
				Vehicle product = hit.source();
				VehicleDto dto = vehicleMapper.toDto(product);
				vehicles.add(dto);
			}

			return vehicles;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
