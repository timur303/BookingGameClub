package kg.kadyrbekov.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "Описание ошибки")
@Getter
@Setter
public class ApiError {

    @ApiModelProperty(value = "Сообщение об ошибке", example = "string")
    private String reason;

}