package by.kharchenko.intexsoftproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardNumberDto {
    private String email;
    private Integer number;
}
