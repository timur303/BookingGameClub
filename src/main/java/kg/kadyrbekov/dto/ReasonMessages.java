package kg.kadyrbekov.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@ApiModel
@Getter
@Setter
public class ReasonMessages {

    private String messages;

    private String code;

}
