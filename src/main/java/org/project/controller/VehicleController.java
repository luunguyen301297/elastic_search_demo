package org.project.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.project.dto.VehicleDto;
import org.project.payload.request.SearchRequest;
import org.project.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleController {
	VehicleService vehicleService;

	@PostMapping("/save")
	public Object save(@RequestBody VehicleDto vehicleDto) {
		return vehicleService.save(vehicleDto);
	}

	@GetMapping("/vehicle/{id}")
	public Object get(@PathVariable("id") String id) {
		return vehicleService.getById(id);
	}

	@PostMapping("/search")
	public Object search(@RequestBody SearchRequest request) {
		return vehicleService.findByCondition(request);
	}
}
