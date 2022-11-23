package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.dto.ProductPageDto;
import by.kharchenko.intexsoftproject.model.entity.Product;
import by.kharchenko.intexsoftproject.model.mapper.ProductMapper;
import by.kharchenko.intexsoftproject.model.repository.ProductRepository;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import by.kharchenko.intexsoftproject.util.filereadwrite.FileReaderWriter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private FileReaderWriter fileReaderWriter;

    @Override
    public List<ProductDto> findByCurrentPage(ProductPageDto productPageDto) throws ServiceException {
        int offSet = (productPageDto.getPage() - 1) * productPageDto.getCountItemInPage();
        Pageable pageable = PageRequest.of(offSet, productPageDto.getCountItemInPage());
        List<Product> products = productRepository.findByCurrentPage(pageable);
        List<ProductDto> productsDto = ProductMapper.INSTANCE.listProductToListProductDto(products);
        for (int i = 0; i < products.size(); i++) {
            productsDto.get(i).setPhoto(fileReaderWriter.readFile(products.get(i).getPhotoPath()));
            productsDto.get(i).setInstruction(fileReaderWriter.readFile(products.get(i).getInstructionPath()));
        }
        return productsDto;
    }
}
