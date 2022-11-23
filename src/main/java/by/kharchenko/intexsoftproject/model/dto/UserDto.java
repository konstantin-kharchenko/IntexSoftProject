package by.kharchenko.intexsoftproject.model.dto;

import by.kharchenko.intexsoftproject.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    private byte[] photo;
}
