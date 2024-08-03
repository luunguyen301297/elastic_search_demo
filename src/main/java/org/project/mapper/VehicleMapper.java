package org.project.mapper;

import org.mapstruct.Mapper;
import org.project.document.Vehicle;
import org.project.dto.VehicleDto;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
	VehicleDto toDto(Vehicle vehicle);
	Vehicle toVehicle(VehicleDto vehicleDto);
}
