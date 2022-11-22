package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductPageDto;
import by.kharchenko.intexsoftproject.model.entity.Product;
import by.kharchenko.intexsoftproject.model.repository.ProductRepository;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> findByCurrentPage(ProductPageDto productPageDto) throws ServiceException {
        int offSet = (productPageDto.getPage() - 1) * productPageDto.getCountItemInPage();
        Pageable pageable = PageRequest.of(offSet, productPageDto.getCountItemInPage());
        return productRepository.findByCurrentPage(pageable);
    }
}
