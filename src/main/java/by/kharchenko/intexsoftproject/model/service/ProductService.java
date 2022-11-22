package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductPageDto;
import by.kharchenko.intexsoftproject.model.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findByCurrentPage(ProductPageDto productPageDto) throws ServiceException;
}
