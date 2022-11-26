package by.kharchenko.intexsoftproject.model.mapper;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.dto.ProductListDto;
import by.kharchenko.intexsoftproject.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    List<ProductListDto> listProductToListProductListDto(List<Product> products) throws ServiceException;

    ProductDto productToProductDto(Product product) throws ServiceException;

}
