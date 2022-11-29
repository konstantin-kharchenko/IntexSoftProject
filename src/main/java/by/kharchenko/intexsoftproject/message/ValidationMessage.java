package by.kharchenko.intexsoftproject.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationMessage {
    private String fieldName;
    private String message;
}
