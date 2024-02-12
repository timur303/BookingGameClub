package kg.kadyrbekov.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class Error401 {

    @ApiModelProperty
    private String reason;
}