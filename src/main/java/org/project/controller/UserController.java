package org.project.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.project.document.User;
import org.project.payload.request.UpdateUserRequest;
import org.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
	UserService personService;

	@PostMapping("/save")
	public Object save(@RequestBody User user) {
		return personService.save(user);
	}

	@GetMapping("/{id}")
	public Object get(@PathVariable("id") String id) {
		return personService.findById(id);
	}

	@PutMapping("/update")
	public Object update(@RequestBody UpdateUserRequest request) {
		return personService.update(request);
	}
}
