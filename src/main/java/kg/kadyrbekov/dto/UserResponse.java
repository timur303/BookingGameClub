package kg.kadyrbekov.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kg.kadyrbekov.model.enums.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@ApiModel
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class UserResponse {

    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private Role role;


}
