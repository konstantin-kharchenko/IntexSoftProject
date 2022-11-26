package by.kharchenko.intexsoftproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {

    @NotEmpty(message = "page haven't to be empty")
    @Pattern(regexp = "^+\\d$", message = "Invalid")
    private int page;

    @NotEmpty(message = "limit haven't to be empty")
    @Pattern(regexp = "^+\\d$", message = "Invalid")
    private int countItemInPage;
}
