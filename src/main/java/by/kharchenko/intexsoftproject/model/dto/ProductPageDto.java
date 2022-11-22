package by.kharchenko.intexsoftproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageDto {
    private int page;
    private int countItemInPage;
}
