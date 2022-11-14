package by.kharchenko.intexsoftproject.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomTokenDto {

    private String accessToken;
    private String refreshToken;
}
