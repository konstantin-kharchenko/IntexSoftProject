package by.kharchenko.intexsoftproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationDataDto {
    private List<ProductListDto> productListDtoList;
    private int page;
    private int limit;
    private int total;
}
