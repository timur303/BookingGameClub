package kg.kadyrbekov.mapper;

import io.swagger.annotations.ApiModel;
import kg.kadyrbekov.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@ApiModel
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class LoginResponse {

    private String token;

    private Long Id;

    @Enumerated(EnumType.STRING)
    private Role role;


}
