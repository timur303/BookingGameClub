package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.City;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ChooseCity {

    @Enumerated(EnumType.STRING)
    private City city;
}
