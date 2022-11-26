package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.PaginationDataDto;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.dto.PageDto;
import org.springframework.core.io.Resource;

public interface ProductService {

    PaginationDataDto findByCurrentPage(PageDto pageDto, String search) throws ServiceException;

    ProductDto findById(Long id) throws ServiceException;

    Resource getPictureById(Long id) throws ServiceException;

    Resource getInstructionById(Long id) throws ServiceException;
}
