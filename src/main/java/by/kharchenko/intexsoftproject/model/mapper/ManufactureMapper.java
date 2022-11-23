package by.kharchenko.intexsoftproject.model.mapper;

import by.kharchenko.intexsoftproject.model.dto.ManufactureDto;
import by.kharchenko.intexsoftproject.model.entity.Manufacture;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ManufactureMapper {
    ManufactureMapper INSTANCE = Mappers.getMapper(ManufactureMapper.class);

    ManufactureDto manufactureToManufactureDto(Manufacture manufacture);
}
