package org.project.mapper;

import org.mapstruct.Mapper;
import org.project.document.User;
import org.project.payload.request.UpdateUserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User toUser(UpdateUserRequest user);
}
