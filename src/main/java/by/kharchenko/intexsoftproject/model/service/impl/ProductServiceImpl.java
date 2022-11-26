package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.PaginationDataDto;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.dto.PageDto;
import by.kharchenko.intexsoftproject.model.entity.Product;
import by.kharchenko.intexsoftproject.model.mapper.ProductMapper;
import by.kharchenko.intexsoftproject.model.repository.ProductRepository;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import by.kharchenko.intexsoftproject.model.specifiaction.SearchOperation;
import by.kharchenko.intexsoftproject.model.specifiaction.SpecificationsBuilder;
import by.kharchenko.intexsoftproject.util.filereadwrite.FileReaderWriter;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    private final String photoPath;
    private final String instructionPath;
    private final int maxLimit;
    private final ProductRepository productRepository;
    private final FileReaderWriter fileReaderWriter;

    public ProductServiceImpl(@Value("${product.photo.path}") String photoPath
            , @Value("${product.instruction.path}") String instructionPath
            , @Value("${max.product.limit}") int maxLimit
            , ProductRepository productRepository, FileReaderWriter fileReaderWriter) {
        this.photoPath = photoPath;
        this.instructionPath = instructionPath;
        this.maxLimit = maxLimit;
        this.productRepository = productRepository;
        this.fileReaderWriter = fileReaderWriter;
    }

    @Override
    public PaginationDataDto findByCurrentPage(PageDto pageDto, String search) throws ServiceException {
        int limit;
        if (pageDto.getCountItemInPage() > maxLimit) {
            limit = maxLimit;
        } else {
            limit = pageDto.getCountItemInPage();
        }
        int page = pageDto.getPage();
        Pageable pageable = PageRequest.of(page - 1, limit);
        SpecificationsBuilder builder = new SpecificationsBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }
        Specification<Product> spec = builder.build();
        Page<Product> products = productRepository.findAll(spec, pageable);
        int total = products.getTotalPages();
        if (total == 0) {
            return new PaginationDataDto(new ArrayList<>(), 1, limit, 1);
        }
        if (products.getContent().size() == 0) {
            pageable = PageRequest.of(total - 1, limit);
            products = productRepository.findAll(spec, pageable);
            page = total;
        }

        return new
                PaginationDataDto(ProductMapper.INSTANCE.listProductToListProductListDto(products.getContent())
                , page
                , limit
                , total);
    }

    @Override
    public ProductDto findById(Long id) throws ServiceException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.get();
        return ProductMapper.INSTANCE.productToProductDto(product);
    }

    @Override
    public Resource getPictureById(Long id) throws ServiceException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.get();
        return fileReaderWriter.readFile(photoPath, product.getPhotoName());
    }

    @Override
    public Resource getInstructionById(Long id) throws ServiceException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.get();
        return fileReaderWriter.readFile(instructionPath, product.getInstructionName());
    }
}
