package org.project.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.project.document.User;
import org.project.helper.Indices;
import org.project.mapper.UserMapper;
import org.project.payload.request.UpdateUserRequest;
import org.project.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
	PersonRepo personRepo;
	ElasticsearchClient client;
	UserMapper userMapper;

	public Boolean save(final User user) {
		try {
			personRepo.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public User findById(final String id) {
		return personRepo.findById(id).orElse(null);
	}

	public Object update(final UpdateUserRequest request) {
		try {
			User user = userMapper.toUser(request);

			UpdateResponse<User> response = client.update(
					i -> i.index(Indices.PERSON_INDEX)
							.id(request.getId())
							.doc(user)
							.upsert(user),
					User.class);

			return personRepo.save(user);
		} catch (IOException e) {
			return "Failed to update user";
		}
	}
}
